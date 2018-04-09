package ga.gasoft.smartpolice.model;

/**
 * Created by prathibha on 12/26/2016.
 */

public class StolenProperty {

    public String name;

    public String address;

    public String phoneNo;

    public int checked;

    public double latitude;

    public double longitude;

    @Override
    public String toString() {
        return "StolenProperty{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", checked=" + checked +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
