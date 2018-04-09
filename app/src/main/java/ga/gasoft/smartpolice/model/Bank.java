package ga.gasoft.smartpolice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;

/**
 * Created by administrator on 11/15/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bank {
    public int B_ID;

    public String B_Name;

    public String B_Place;

    public int cctv_present;

    public int cctv_working;

    public int secPerson;

    public int checked;

    public double lattitude;

    public double longitude;

    public Date timeStampData;

    @Override
    public String toString() {
        return "Bank{" +
                "B_ID=" + B_ID +
                ", B_Name='" + B_Name + '\'' +
                ", B_Place='" + B_Place + '\'' +
                ", cctv_present=" + cctv_present +
                ", cctv_working=" + cctv_working +
                ", secPerson=" + secPerson +
                ", checked=" + checked +
                '}';
    }



}
