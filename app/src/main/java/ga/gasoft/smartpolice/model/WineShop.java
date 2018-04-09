package ga.gasoft.smartpolice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;

/**
 * Created by prathibha on 11/16/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WineShop {

    public int WB_ID;

    public String WB_Name;

    public String WB_Place;

    public int cctv_present;

    public int cctv_working;

    public int secPerson;

    public double lattitude;

    public double longitude;

    public int checked;

    public Date timeStampData;

    @Override
    public String toString() {
        return "WineShop{" +
                "WB_ID=" + WB_ID +
                ", WB_Name='" + WB_Name + '\'' +
                ", WB_Place='" + WB_Place + '\'' +
                ", cctv_present=" + cctv_present +
                ", cctv_working=" + cctv_working +
                ", secPerson=" + secPerson +
                ", checked=" + checked +
                '}';
    }
}
