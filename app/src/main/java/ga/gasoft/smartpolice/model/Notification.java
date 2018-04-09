package ga.gasoft.smartpolice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by prathibha on 12/16/2016.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {

    public String Message;

    public String Type;

    public String DateTime;

    @Override
    public String toString() {
        return "Notification{" +
                "Message='" + Message + '\'' +
                ", Type='" + Type + '\'' +
                ", DateTime='" + DateTime + '\'' +
                '}';
    }
}
