package uk.ncl.giacomobergami.utils.data;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class CSVMediator<T> {
    private final Class<T> clazz;
    CsvMapper csvMapper;
    CsvSchema csvSchema;

    public CSVMediator(Class<T> clazz) {
        this.clazz = clazz;
        csvMapper = new CsvMapper();
        csvSchema = csvMapper
                .schemaFor(clazz)
                .withHeader();
    }

    public boolean writeAll(File name, Collection<T> values) {
        var writer = beginCSVWrite(name);
        for (T x : values) writer.write(x);
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean readAll(File name, Collection<T> values) {
        var reader = beginCSVRead(name);
        while (reader.hasNext()) {
            values.add(reader.next());
        }
        try {
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public CSVWriter beginCSVWrite(File filename) {
        try {
            return new CSVWriter(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CSVReader beginCSVRead(File filename) {
        try {
            return new CSVReader(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class CSVReader implements AutoCloseable, Iterator<T> {
        MappingIterator<T> reader;
        CSVReader(File filename) throws IOException {
//            System.out.println(filename.getAbsolutePath());
            reader = csvMapper.readerFor(clazz)
                    .with(csvSchema)
                    .readValues(filename.getAbsoluteFile());
        }

        @Override
        public boolean hasNext() {
            return ((reader != null) && (reader.hasNext()));
        }

        @Override
        public T next() {
            if ((reader == null) || (!reader.hasNext())) return null;
            return reader.next();
        }

        @Override
        public void close() throws Exception {
            if (reader != null) {
                reader.close();
                reader = null;
            }
        }
    }

    public class CSVWriter implements AutoCloseable {
        SequenceWriter writer;
        CSVWriter(File filename) throws IOException {
            writer = csvMapper.writerFor(clazz)
                    .with(csvSchema)
                    .writeValues(filename.getAbsoluteFile());
        }

        public boolean write(T object) {
            if (writer == null) return false;
            try {
                writer.write(object);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        public void close() throws Exception {
            if (writer != null) {
                writer.close();
                writer = null;
            }
        }
    }


}
