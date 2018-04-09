/**
 * Created by administrator on 11/9/16.
 */
package ga.gasoft.smartpolice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Atm {

    public int A_ID;

    public String ATM_ID;

    public String ATM_Address;

    public int cctv_present;

    public int cctv_working;

    public int atm_working;

    public int secPerson;

    public int checked;

    public Date timeStampData;

    public double lattitude;

    public double longitude;
    @Override
    public String toString() {
        return "Atm{" +
                "A_ID=" + A_ID +
                ", ATM_ID='" + ATM_ID + '\'' +
                ", ATM_Address='" + ATM_Address + '\'' +
                ", cctv_present=" + cctv_present +
                ", cctv_working=" + cctv_working +
                ", atm_working=" + atm_working +
                ", secPerson=" + secPerson +
                ", checked=" + checked +
                ", lattitude=" + lattitude +
                ", longitude=" + longitude +
                '}';
    }




}
