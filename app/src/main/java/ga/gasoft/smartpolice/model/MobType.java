package ga.gasoft.smartpolice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by prathibha on 12/27/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MobType {

    public int MOB_ID;

    public String MobName;

    public double latitude;

    public double longitude;

    public int checked;

    public int punished;

    @Override
    public String toString() {
        return "MobType{" +
                "MOB_ID=" + MOB_ID +
                ", MobName='" + MobName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", checked=" + checked +  ", punished=" + punished +
                '}';
    }
}
