package uk.ncl.giacomobergami.utils.shared_data.edge;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import uk.ncl.giacomobergami.utils.shared_data.abstracted.TimedObject;

import java.util.Objects;

// To be renamed as "EdgeDevice"

@JsonPropertyOrder({
        "tl_id",
        "x",
        "y",
        "simtime",
        "communication_radius",
        "max_vehicle_communication"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimedEdge implements TimedObject<TimedEdge> {
    @JsonProperty("id")
    public String id;

    @JsonProperty("x")
    public double x;

    @JsonProperty("y")
    public double y;

    @JsonProperty("simtime")
    public double simtime;

    @JsonProperty("communication_radius")
    public double communication_radius;

    @JsonProperty("max_vehicle_communication")
    public double max_vehicle_communication;

    @JsonIgnore
    public EdgeProgram program_rsu;

    public EdgeProgram getProgram_rsu() {
        return program_rsu;
    }

    public void setProgram_rsu(EdgeProgram program_rsu) {
        this.program_rsu = program_rsu;
    }

    public TimedEdge() {

    }

    public TimedEdge(String id, double x, double y, double communication_radius,
                     double max_vehicle_communication, double simtime) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.communication_radius = communication_radius;
        this.max_vehicle_communication = max_vehicle_communication;
        this.simtime = simtime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSimtime(double simtime) {
        this.simtime = simtime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public double getSimtime() {
        return simtime;
    }

    @Override
    public TimedEdge copy() {
        return new TimedEdge(id, x, y, communication_radius, max_vehicle_communication, simtime);
    }

    public void setTl_id(String tl_id) {
        this.id = tl_id;
    }

    public void setX(double tl_x) {
        this.x = tl_x;
    }
    public void setY(double tl_y) {
        this.y = tl_y;
    }

    public double getCommunication_radius() {
        return communication_radius;
    }

    public void setCommunication_radius(double communication_radius) {
        this.communication_radius = communication_radius;
    }

    public double getMax_vehicle_communication() {
        return max_vehicle_communication;
    }

    public void setMax_vehicle_communication(double max_vehicle_communication) {
        this.max_vehicle_communication = max_vehicle_communication;
    }

    @Override
    public String toString() {
        return "RSU{" +
                "tl_id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", communication_radius=" + communication_radius +
                ", max_vehicle_communication=" + max_vehicle_communication +
                '}';
    }

    //    public ConfiguationEntity.VMEntity asVMEntity(int pes,
//                                                  double mips,
//                                                  int ram,
//                                                  double storage,
//                                                  long bw,
//                                                  String cloudletPolicy) {
//        ConfiguationEntity.VMEntity result = new ConfiguationEntity.VMEntity();
//        result.setBw(bw);
//        result.setCloudletPolicy(cloudletPolicy);
//        result.setPes(pes);
//        result.setMips(mips);
//        result.setRam(ram);
//        result.setStorage(storage);
//        result.setName(tl_id);
//        return result;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimedEdge timedEdge = (TimedEdge) o;
        return Double.compare(timedEdge.x, x) == 0 && Double.compare(timedEdge.y, y) == 0 && Double.compare(timedEdge.communication_radius, communication_radius) == 0 && Double.compare(timedEdge.max_vehicle_communication, max_vehicle_communication) == 0 && Objects.equals(id, timedEdge.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, communication_radius, max_vehicle_communication);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }
}
