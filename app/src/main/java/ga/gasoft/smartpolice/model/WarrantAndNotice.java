package ga.gasoft.smartpolice.model;

/**
 * Created by prathibha on 12/26/2016.
 */

public class WarrantAndNotice {

    public String name;

    public String warrantDetails;

    public int checked;

    public double latitude;

    public double longitude;

    @Override
    public String toString() {
        return "WarrantAndNotice{" +
                "name='" + name + '\'' +
                ", warrantDetails='" + warrantDetails + '\'' +
                ", checked=" + checked +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
