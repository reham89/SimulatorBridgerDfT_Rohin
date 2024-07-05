package uk.ncl.giacomobergami.SumoOsmosisBridger.traffic_converter;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import uk.ncl.giacomobergami.traffic_converter.abstracted.TrafficConverter;
import uk.ncl.giacomobergami.traffic_orchestrator.rsu_network.netgen.NetworkGenerator;
import uk.ncl.giacomobergami.traffic_orchestrator.rsu_network.netgen.NetworkGeneratorFactory;
import uk.ncl.giacomobergami.traffic_orchestrator.rsu_network.rsu.RSUUpdater;
import uk.ncl.giacomobergami.traffic_orchestrator.rsu_network.rsu.RSUUpdaterFactory;
import uk.ncl.giacomobergami.utils.data.YAML;
import uk.ncl.giacomobergami.utils.pipeline_confs.TrafficConfiguration;
import uk.ncl.giacomobergami.utils.shared_data.edge.TimedEdge;
import uk.ncl.giacomobergami.utils.shared_data.iot.TimedIoT;
import uk.ncl.giacomobergami.utils.structures.StraightforwardAdjacencyList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DfTConverterBolog extends TrafficConverter {
    // Ensure there is a public constructor

    private final SUMOConfiguration concreteConf;
    private long earliestTime;
    private final NetworkGenerator netGen;
    private final RSUUpdater rsuUpdater;
    private DocumentBuilder db;
    Document networkFile;
    StraightforwardAdjacencyList<String> connectionPath;
    HashMap<Double, List<TimedIoT>> timedIoTDevices;
    HashSet<TimedEdge> roadSideUnits;
    // private static Logger logger = LogManager.getRootLogger();
    List<String[]> data = new ArrayList<>();
    List<TimedIoT> timedIoTs = new ArrayList<>();
    List<TimedEdge> timedEdges = new ArrayList<>();
    List<String> rows = new ArrayList<>();
    List<Double> temporalOrdering;
    private static Logger logger = LogManager.getRootLogger();

    public DfTConverterBolog(TrafficConfiguration conf) {  super(conf);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            db = null;
        }
        concreteConf = YAML.parse(SUMOConfiguration.class, new File(conf.YAMLConverterConfiguration)).orElseThrow();
        temporalOrdering = new ArrayList<>();
        networkFile = null;
        timedIoTDevices = new HashMap<>();
        roadSideUnits = new HashSet<>();
        netGen = NetworkGeneratorFactory.generateFacade(concreteConf.generateRSUAdjacencyList);
        rsuUpdater = RSUUpdaterFactory.generateFacade(concreteConf.updateRSUFields,
                concreteConf.default_rsu_communication_radius,
                concreteConf.default_max_vehicle_communication);
        connectionPath = new StraightforwardAdjacencyList<>();
    }

    @Override
    protected boolean initReadSimulatorOutput() {
        connectionPath.clear();
        temporalOrdering.clear();
        timedIoTDevices.clear();
        networkFile = null;
        File file = new File(concreteConf.DfT_file_path);
        Document DfTFile = null;
//        try {
//            DfTFile = db.parse(file);
//        } catch (SAXException | IOException e) {
//            e.printStackTrace();
//            return false; }
        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            List<String[]> rows = reader.readAll();

            // Determining the indices of columns
            String[] header = rows.get(0);
            int timeColumnIndex = Arrays.asList(header).indexOf("time");
            int eastColumnIndex = Arrays.asList(header).indexOf("Easting");
            int northColumnIndex = Arrays.asList(header).indexOf("Northing");
            int idColumnIndex = Arrays.asList(header).indexOf("Count_point_id");
            int vehnumColumnIndex = Arrays.asList(header).indexOf("All_motor_vehicles");
            int laneColumnIndex = Arrays.asList(header).indexOf("Direction_of_travel");

            if (timeColumnIndex == -1 || eastColumnIndex == -1 || northColumnIndex == -1 ||
                    idColumnIndex == -1 || vehnumColumnIndex == -1 || laneColumnIndex == -1) {
                logger.error("One or more required columns are missing in the CSV header.");
                return false;
            }

            var body = rows.subList(1, rows.size());

//            Comparator<String[]> comparator = (o1, o2) -> {
//                try {
//                    double time1 = Double.parseDouble(o1[timeColumnIndex]);
//                    double time2 = Double.parseDouble(o2[timeColumnIndex]);
//                    int id1 = Integer.parseInt(o1[idColumnIndex].replaceAll("\\D", ""));
//                    int id2 = Integer.parseInt(o2[idColumnIndex].replaceAll("\\D", ""));
//                    return Comparator.comparingDouble((String[] row) -> Double.parseDouble(row[timeColumnIndex]))
//                            .thenComparingInt(row -> Integer.parseInt(row[idColumnIndex].replaceAll("\\D", "")))
//                            .compare(o1, o2);
//                } catch (NumberFormatException e) {
//                    logger.error("Error parsing row: " + Arrays.toString(o1) + " or " + Arrays.toString(o2), e);
//                    return 0;
//                }
//            };

//            body.sort(comparator);

            // Define the function 'f'
            Function<String, TimedEdge> f = id -> {
                for (String[] row : body) {
                    if (row[idColumnIndex].equals(id)) {
                        return new TimedEdge(
                                id,
                                Double.parseDouble(row[eastColumnIndex]),
                                Double.parseDouble(row[northColumnIndex]),
                                concreteConf.default_rsu_communication_radius,
                                concreteConf.default_max_vehicle_communication,
                                0
                        );
                    }
                }
                return null;
            };

            for (String[] row : body) {
                double x = Double.parseDouble(row[eastColumnIndex]);
                double y = Double.parseDouble(row[northColumnIndex]);
                double currTime = Double.parseDouble(row[timeColumnIndex]);
                String lane = row[laneColumnIndex];
                temporalOrdering.add(currTime);
                var ls = new ArrayList<TimedIoT>();
                timedIoTDevices.put(currTime, ls);
                int N = Integer.parseInt(row[vehnumColumnIndex]);
                for (int counter = 0; counter < N; counter++) {
                    TimedIoT rec = new TimedIoT();
                    rec.id = "id_" + counter;
                    rec.x = x;
                    rec.y = y;
                    rec.lane = lane;
                    rec.simtime = currTime;
                    ls.add(rec);
                }
            }

            Set<String> uniqueIds = body.stream()
                    .map(row -> row[idColumnIndex])
                    .collect(Collectors.toSet());

            for (String id : uniqueIds) {
                var el = f.apply(id);
                if (el != null) {
                    roadSideUnits.add(el);
                }
            }

            var tmp = netGen.apply(roadSideUnits);
            tmp.forEach((k, v) -> connectionPath.put(k.id, v.id));

        } catch (FileNotFoundException e) {
            logger.error("File not found: " + file, e);
        } catch (IOException e) {
            logger.error("Error reading file: " + file, e);
        } catch (CsvException e) {
            logger.error("Error parsing CSV file: " + file, e);
        }

        return true;
    }

    @Override
    protected List<Double> getSimulationTimeUnits() {
        return new ArrayList<>(new TreeSet<>(temporalOrdering));
    }

    @Override
    protected Collection<TimedIoT> getTimedIoT(Double tick) {
        return timedIoTDevices.get(tick);
    }

    @Override
    protected HashMap<Double, List<TimedIoT>> getAllTimedIoT() {
        return null;
    }

    @Override
    protected StraightforwardAdjacencyList<String> getTimedEdgeNetwork(Double tick) {
        return connectionPath;
    }

    @Override
    protected HashSet<TimedEdge> getTimedEdgeNodes(Double tick) {
        return roadSideUnits.stream().map(x -> {
            var ls = x.copy();
            ls.setSimtime(tick);
            return ls;
        }).collect(Collectors.toCollection(HashSet<TimedEdge>::new));
    }

    @Override
    protected void endReadSimulatorOutput() {
        data.clear();
        timedIoTs.clear();
        timedEdges.clear();
        temporalOrdering.clear();
        timedIoTDevices.clear();
        networkFile = null;
        connectionPath.clear();
    }

    @Override
    public boolean runSimulator(TrafficConfiguration conf) {
        File file = new File(concreteConf.DfT_file_path);
        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            List<String[]> rows = reader.readAll();
            String[] headers = rows.get(0);
            int dateColumnIndex = Arrays.asList(headers).indexOf("Count_date");
            int hourColumnIndex = Arrays.asList(headers).indexOf("hour");
            // convert the string to date and time
            // DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime earliestDateTime = LocalDateTime.MAX;
            LocalDateTime latestDateTime = LocalDateTime.MIN;
            // find the earliest and latest date
            //  for (int i = 1; i < rows.size(); i++) {
            //    String[] row = rows.get(i);
            //  String dateString = row[dateColumnIndex];
            //String hourString = row[hourColumnIndex];
            //  String dateTimeString = dateString + "  " + hourString;
            //  System.out.println("dateString" + dateString);
            //LocalDateTime dateTime = LocalDateTime.parse(dateString, dateFormatter);
            // dateTime = LocalDate.parse(dateString, dateFormatter).atStartOfDay();
            //int hour = Integer.parseInt(hourString);
            //dateTime = dateTime.withHour(hour); // add the time in "hour" to the date
            //if (dateTime.isBefore(earliestDateTime)) {
            //  earliestDateTime = dateTime;
            //}
            //    if (dateTime.isAfter(latestDateTime)) {
            //            latestDateTime = dateTime;
            //  }
        //}

        // Convert the earliest and latest date to seconds
        //  earliestTime = earliestDateTime.toEpochSecond(ZoneOffset.UTC);
        // long latestTime = latestDateTime.toEpochSecond(ZoneOffset.UTC);

        conf.begin = 0;
        conf.end = 3600;
        conf.step = 1;

        // calculate the event's new timestamp (the start time for the current row)
        //   List<String[]> filteredRows = new ArrayList<>();
        //   for (String[] row : rows) {
        //      if(row[dateColumnIndex].equals("Count_date")) {
        //        continue;
        //  }
        //    LocalDateTime dateTime = LocalDateTime.parse(row[dateColumnIndex], dateFormatter);
        //  int hour = Integer.parseInt(row[hourColumnIndex]);
        //dateTime = dateTime.withHour(hour);
        //   long timeInSeconds = dateTime.toEpochSecond(ZoneOffset.UTC) - earliestTime;
        // if (timeInSeconds >= conf.begin && timeInSeconds <= conf.end) {
        //   row[dateColumnIndex] = String.valueOf(timeInSeconds);
        //      filteredRows.add(row);
        //    }
        // }
        //  rows = filteredRows;
    } catch (FileNotFoundException e) {
        System.out.println("File not found: " + file);
        e.printStackTrace();
    } catch (IOException e) {
        System.out.println("Error reading file: " + file);
        e.printStackTrace();
    } catch (CsvException e) {
        System.out.println("Error parsing CSV file: " + file);
        e.printStackTrace();
    }
        return true;
}
}