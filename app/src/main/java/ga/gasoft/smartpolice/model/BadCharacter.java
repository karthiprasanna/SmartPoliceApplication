package ga.gasoft.smartpolice.model;

import ga.gasoft.smartpolice.R;

/**
 * Created by prathibha on 12/22/2016.
 */

public class BadCharacter {

    public String name;

    public String address;

    public int checked;

    public int userId;

    public double latitude;

    public double longitude;


    @Override
    public String toString() {
        return "BadCharacter{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", checked=" + checked +
                ", userId=" + userId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
