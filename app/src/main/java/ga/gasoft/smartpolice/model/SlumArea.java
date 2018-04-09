package ga.gasoft.smartpolice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;

/**
 * Created by prathibha on 11/30/2016.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlumArea {

    public int SLM_ID;

    public String Slumheadname;

    public String Details;

    public int mobPresent;

    public int badCharacterPresent;

    public int rowdyPresent;

    public int checked;

    public Date timeStampData;

    public double latitude;

    public double longitude;

    @Override
    public String toString() {
        return "SlumArea{" +
                "SLM_ID=" + SLM_ID +
                ", Slumheadname='" + Slumheadname + '\'' +
                ", Details='" + Details + '\'' +
                ", mobPresent=" + mobPresent +
                ", badCharacterPresent=" + badCharacterPresent +
                ", rowdyPresent=" + rowdyPresent +
                ", checked=" + checked +
                ", timeStampData=" + timeStampData +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
