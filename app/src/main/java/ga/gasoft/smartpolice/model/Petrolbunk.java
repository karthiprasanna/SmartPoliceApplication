package ga.gasoft.smartpolice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;

/**
 * Created by administrator on 11/16/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Petrolbunk {
    public int PB_ID;

    public String PB_NAME;

    public String PB_PLACE;

    public int cctv_present;

    public int cctv_working;

    public int secPerson;

    public int checked;

    public double lattitude;

    public double longitude;

    public Date timeStampData;

    @Override
    public String toString() {
        return "Petrolbunk{" +
                "PB_ID=" + PB_ID +
                ", PB_NAME='" + PB_NAME + '\'' +
                ", PB_PLACE='" + PB_PLACE + '\'' +
                ", cctv_present=" + cctv_present +
                ", cctv_working=" + cctv_working +
                ", secPerson=" + secPerson +
                ", checked=" + checked +
                ", lattitude=" + lattitude +
                ", longitude=" + longitude +
                '}';
    }





}
