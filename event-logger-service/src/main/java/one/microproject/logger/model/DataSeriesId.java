package one.microproject.logger.model;

import java.util.Objects;

public class DataSeriesId {

    private final String groupId;
    private final String name;

    public DataSeriesId(String groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSeriesId that = (DataSeriesId) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, name);
    }

    @Override
    public String toString() {
        return "DataSeriesId{" +
                "groupId='" + groupId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String toStringId() {
        return groupId + "-" + name;
    }

    public static DataSeriesId from(String id) {
        String[] ids = id.split("-");
        return new DataSeriesId(ids[0], ids[1]);
    }

    public static DataSeriesId from(String groupId, String name) {
        return new DataSeriesId(groupId, name);
    }

}
