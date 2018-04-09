package ga.gasoft.smartpolice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;

/**
 * Created by prathibha on 11/15/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PgDetail {

    public int PG_ID;

    public String PG_Name;

    public String PG_Place;

    public int cctv_present;

    public int cctv_working;

    public int secPerson;

    public int entrollmentRegisterPresent;

    public int documentsVerified;

    public int checked;

    public double lattitude;

    public double longitude;

    public Date timeStampData;

    @Override
    public String toString() {
        return "PgDetail{" +
                "PG_ID=" + PG_ID +
                ", PG_Name='" + PG_Name + '\'' +
                ", PG_Place='" + PG_Place + '\'' +
                ", cctv_present=" + cctv_present +
                ", cctv_working=" + cctv_working +
                ", secPerson=" + secPerson +
                ", entrollmentRegisterPresent=" + entrollmentRegisterPresent +
                ", documentsVerified=" + documentsVerified +
                ", checked=" + checked +
                '}';
    }

}
