package ga.gasoft.smartpolice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;

/**
 * Created by administrator on 11/17/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reglious {

    public int R_ID;

    public String ReligiousPlace;

    public String ReligiousType;

    public int checked;

    public double lattitude;

    public double longitude;

    public Date timeStampData;

    @Override
    public String toString() {
        return "Reglious{" +
                "R_ID=" + R_ID +
                ", ReligiousPlace='" + ReligiousPlace + '\'' +
                ", ReligiousType='" + ReligiousType + '\'' +
                ", checked=" + checked +
                ", lattitude=" + lattitude +
                ", longitude=" + longitude +
                '}';
    }



}
