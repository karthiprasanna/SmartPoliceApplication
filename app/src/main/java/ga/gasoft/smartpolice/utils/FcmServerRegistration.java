package ga.gasoft.smartpolice.utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.UserNetworkManager;

/**
 * Created by prathibha on 12/14/2016.
 */

public class FcmServerRegistration {


    private static String systemOs = "Android";
    private Context myContext;

    public void sendRegisterFcmId(Context context){

        myContext = context;
        try {
        JSONObject jsonData = new JSONObject();
           jsonData.put("os",systemOs);
            jsonData.put("userid", App.getUserId());
            jsonData.put("fcmregnum", App.getFcmregistrationid());

        UserNetworkManager.postFcmRegistrationId(new CallBacks.StringCallBackListener() {
            @Override
            public void onSuccess(String successMessage) {
//                Toast.makeText(myContext, successMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
//                Toast.makeText(myContext, errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {

            }
        },jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
