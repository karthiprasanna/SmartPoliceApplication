package ga.gasoft.smartpolice.model;

/**
 * Created by prathibha on 12/13/2016.
 */

public class CheckPoint {

    public int CHK_ID;

    public String CheckType;

    public String VehicalType;

    public String RegNo;

    public String DriverName;

    public String Phone;

    public String JourneyFrom;

    public String JourneyTo;

    public int NoOfPass;

    public double latitude;

    public double longitude;

    public int checked;


    @Override
    public String toString() {
        return "CheckPoint{" +
                "CHK_ID=" + CHK_ID +
                ", CheckType='" + CheckType + '\'' +
                ", VehicalType='" + VehicalType + '\'' +
                ", RegNo='" + RegNo + '\'' +
                ", DriverName='" + DriverName + '\'' +
                ", Phone='" + Phone + '\'' +
                ", JourneyFrom='" + JourneyFrom + '\'' +
                ", JourneyTo='" + JourneyTo + '\'' +
                ", NoOfPass='" + NoOfPass + '\'' +
                '}';
    }
}
