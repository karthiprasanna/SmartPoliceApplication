/**
 * Created by administrator on 11/9/16.
 */
package ga.gasoft.smartpolice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profile {

    public String firstname;

    public String lastname;

    public String username;

    public String gender;

    public String email;

    public String password;

    public String address;

    public String mobile;

    public int deptID;

    public int PS_ID;

    public String PS_NAME;

    public String SUB_NAME;

    public String DI_Name;

    public String DIS_NAME;

    public Date timeStampData;

    public String ImagesPath;

    @Override
    public String toString() {
        return "Profile{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", deptID=" + deptID +
                ", PS_ID=" + PS_ID +
                ", PS_NAME='" + PS_NAME + '\'' +
                ", SUB_NAME='" + SUB_NAME + '\'' +
                ", DI_Name='" + DI_Name + '\'' +
                ", DIS_NAME='" + DIS_NAME + '\'' +
                ", timeStampData=" + timeStampData +
                ", ImagesPath='" + ImagesPath + '\'' +
                '}';
    }
}
