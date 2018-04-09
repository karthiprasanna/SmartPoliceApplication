package ga.gasoft.smartpolice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by prathibha on 12/7/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bolo {

    public int id;

    public String boloname;

    public int checked;

    public double latitude;

    public double longitude;

    @Override
    public String toString() {
        return "Bolo{" +
                "id=" + id +
                ", boloname='" + boloname + '\'' +
                ", checked=" + checked +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
