package in.co.mpwin.rebilling.beans.xmlfilebean;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
public class DataEntityD301 {
    private String mechanism;
    private String datetime;
    private List<B3Entity> b3Entities = new ArrayList<>();
    private List<B4Entity> b4Entities = new ArrayList<>();
    private List<B5Entity> b5Entities = new ArrayList<>();
    private List<B6Entity> b6Entities = new ArrayList<>();

    // Getters and setters for mechanism and datetime

    public String getMechanism() {
        return mechanism;
    }

    public void setMechanism(String mechanism) {
        this.mechanism = mechanism;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    // Getters and setters for lists of B3, B4, B5, and B6 entities

    public List<B3Entity> getB3Entities() {
        return b3Entities;
    }

    public void setB3Entities(List<B3Entity> b3Entities) {
        this.b3Entities = b3Entities;
    }

    public void addB3(B3Entity b3Entity) {
        this.b3Entities.add(b3Entity);
    }

    public List<B4Entity> getB4Entities() {
        return b4Entities;
    }

    public void setB4Entities(List<B4Entity> b4Entities) {
        this.b4Entities = b4Entities;
    }

    public void addB4(B4Entity b4Entity) {
        this.b4Entities.add(b4Entity);
    }

    public List<B5Entity> getB5Entities() {
        return b5Entities;
    }

    public void setB5Entities(List<B5Entity> b5Entities) {
        this.b5Entities = b5Entities;
    }

    public void addB5(B5Entity b5Entity) {
        this.b5Entities.add(b5Entity);
    }

    public List<B6Entity> getB6Entities() {
        return b6Entities;
    }

    public void setB6Entities(List<B6Entity> b6Entities) {
        this.b6Entities = b6Entities;
    }

    public void addB6(B6Entity b6Entity) {
        this.b6Entities.add(b6Entity);
    }

    @Override
    public String toString() {
        return "DataEntityD301{" +
                "mechanism='" + mechanism + '\'' +
                ", datetime='" + datetime + '\'' +
                ", b3Entities=" + b3Entities +
                ", b4Entities=" + b4Entities +
                ", b5Entities=" + b5Entities +
                ", b6Entities=" + b6Entities +
                '}';
    }
}
