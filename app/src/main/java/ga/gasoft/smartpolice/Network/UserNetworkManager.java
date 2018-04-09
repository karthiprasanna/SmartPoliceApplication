package ga.gasoft.smartpolice.Network;

import android.content.Context;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.R;
import ga.gasoft.smartpolice.model.Atm;
import ga.gasoft.smartpolice.model.Bank;
import ga.gasoft.smartpolice.model.Profile;

import static android.R.attr.type;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class UserNetworkManager {

    private static String TAG = "NetworkManager";

    private static String UPLOAD_URL;
    /**
     * API
     **/

//     development server ip
//    private static final String IP_ADDRESS = "http://10.20.30.136:3000";

    //     live server ip
    private static final String IP_ADDRESS = "http://www.smart.gasofttech.in";


    private static final String USER_LOGIN = IP_ADDRESS+"/api/HomeAPI/get_LoginDetails3";

    private static final String USER_PASSWORD_CONFIRMATION = IP_ADDRESS+"/api/HomeAPI/pwdConfirmation";

    private static final String USER_FCM_REGISTRATION = IP_ADDRESS+"/api/HomeAPI/insertFCMNumber?source=";

    private static final String GET_USER_DETAILS = IP_ADDRESS+"/api/UserManagementAPI/get_allUserDetailsbypsid?uid=";

    private static final String UPLOAD_PROFILE_IMAGE = IP_ADDRESS+"/api/detailsapi/saveProfileImageComingFromApp";

    private static final String GET_ATM_DETAILS = IP_ADDRESS+"/api/DetailsAPI/GetAtmbyUserid?uid=";

    private static final String GET_BANK_DETAILS = IP_ADDRESS+"/(S(4xirxgvsuy3amtcnnuhplvfb))/api/DetailsAPI/GetBankbyUserid?uid=";

    private static final String GET_PG_DETAILS = IP_ADDRESS+"/(S(4xirxgvsuy3amtcnnuhplvfb))/api/DetailsAPI/GetPGbyUserid?uid=";

    private static final String GET_PETROL_BUNK_DETAILS = IP_ADDRESS+"/(S(4xirxgvsuy3amtcnnuhplvfb))/api/DetailsAPI/GetPBbyUserid?uid=";

    private static final String GET_RELIGIOUS_DETAILS = IP_ADDRESS+"/api/DetailsAPI/GetreligiousbyUserid?uid=";

    private static final String GET_WINE_SHOP_DETAILS = IP_ADDRESS+"/(S(4xirxgvsuy3amtcnnuhplvfb))/api/DetailsAPI/GetwinebyUserid?uid=";

    private static final String GET_SLUM_DETAILS = IP_ADDRESS+"/api/DetailsAPI/get_AllSlumAreaDetailsbyUserID?userid=";

    private static final String GET_BOLO_DETAILS = IP_ADDRESS+"/api/DetailsAPI/get_AllboloDetailsbyUserID?userid=";

    private static final String GET_COMPLAINT_TYPE_DETAILS = IP_ADDRESS+"/api/Detailsapi/Sel_complaintType";

    private static final String GET_COMPLAINT_STATUS_DETAILS = IP_ADDRESS+"/api/Detailsapi/Sel_complaintstatus";

    private static final String GET_CHECK_POINT_DETAILS = IP_ADDRESS+"/api/DetailsAPI/get_AllCheckPointDetailsbyUserID?userid=";

    private static final String GET_MOB_TYPE_DETAILS = IP_ADDRESS+"/api/Detailsapi/Sel_Mobtype";

    private static final String GET_NOTIFICATION_DETAILS = IP_ADDRESS+"/api/DetailsAPI/findnotificationbyuid?uid=";

    private static final String GET_NOTIFICATION_COUNT = IP_ADDRESS+"/api/DetailsAPI/getunreadmessagebyuser?userid=";

    private static final String UPLOAD_ATM_IMAGES_URL = IP_ADDRESS+"/api/DetailsAPI/imageuploaddata";

    private static final String UPLOAD_BANK_IMAGES_URL = IP_ADDRESS+"/api/DetailsAPI/imageBankuploaddata";

    private static final String UPLOAD_RELIGIOUS_IMAGES_URL = IP_ADDRESS+"/(S(4xirxgvsuy3amtcnnuhplvfb))/api/DetailsAPI/imageuploaddataReligious";

    private static final String UPLOAD_PG_IMAGES_URL = IP_ADDRESS+"/(S(4xirxgvsuy3amtcnnuhplvfb))/api/DetailsAPI/imagePGupload";

    private static final String UPLOAD_PETROL_BUNK_IMAGES_URL = IP_ADDRESS+"/api/DetailsAPI/imageuploaddataPB";

    private static final String UPLOAD_SLUM_AREA_IMAGES_URL = IP_ADDRESS+"/api/Detailsapi/SlumAreaImageUpload";

    private static final String UPLOAD_BOLO_IMAGES_URL = IP_ADDRESS+"/api/Detailsapi/BoloImageUpload";

    private static final String UPLOAD_BAD_CHARACTER_IMAGES_URL = IP_ADDRESS+"/api/Detailsapi/BadCharacterImageUpload";

    private static final String UPLOAD_CHECK_POINT_IMAGES_URL = IP_ADDRESS+"/api/Detailsapi/CheckpointImageUpload";

    private static final String UPLOAD_MOB_IMAGES_URL = IP_ADDRESS+"/api/Detailsapi/MobImageUpload";

    private static final String UPLOAD_COMPLAINT_IMAGES_URL = IP_ADDRESS+"/api/Detailsapi/ComplaintImageUpload";

    private static final String UPLOAD_STOLEN_PROPERTY_IMAGES_URL = IP_ADDRESS+"/api/Detailsapi/stolenpropertyImageUpload";

    private static final String UPLOAD_WARRENT_NOTICE_IMAGES_URL = IP_ADDRESS+"/api/Detailsapi/WarrentandNoticeImageUpload";

    private static final String UPLOAD_WINE_SHOP_IMAGES_URL = IP_ADDRESS+"/api/DetailsAPI/imageuploaddatawine";

    private static final String UPLOAD_ATM_DETAILS_URL = IP_ADDRESS+"/api/DetailsAPI/saveATMdetails?source=";

    private static final String UPLOAD_BANK_DETAILS_URL = IP_ADDRESS+"/(S(4xirxgvsuy3amtcnnuhplvfb))/api/DetailsAPI/saveBankdetails?BankSource=";

    private static final String UPLOAD_PG_DETAILS_URL = IP_ADDRESS+"/(S(4xirxgvsuy3amtcnnuhplvfb))/api/DetailsAPI/savePGdetails?PGSource=";

    private static final String UPLOAD_PETROL_BUNK_DETAILS_URL = IP_ADDRESS+"/api/DetailsAPI/savePBdetails?PBSource=";

    private static final String UPLOAD_RELIGIOUS_DETAILS_URL =IP_ADDRESS+"/(S(4xirxgvsuy3amtcnnuhplvfb))/api/DetailsAPI/saveReligiousdetails?religioussource=";

    private static final String UPLOAD_SLUM_AREA_DETAILS_URL =IP_ADDRESS+"/api/DetailsAPI/SaveSlumAreaDetails?source=";

    private static final String UPLOAD_BOLO_DETAILS_URL =IP_ADDRESS+"/api/DetailsAPI/SaveBoloDetails?";

    private static final String UPLOAD_WINE_SHOP_DETAILS_URL = IP_ADDRESS+"/api/DetailsAPI/savewinedetails?winesource=";

    private static final String UPLOAD_CHECK_POINT_DETAILS_URL = IP_ADDRESS+"/api/DetailsAPI/SaveCheckpointDetails?";

    private static final String UPLOAD_BAD_CHARAC_DETAILS_URL = IP_ADDRESS+"/api/DetailsAPI/SaveBadCharacterDetails?";

    private static final String UPLOAD_STOLEN_PROPERTY_DETAILS_URL = IP_ADDRESS+"/api/DetailsAPI/SavestolenpropertyDetails?";

    private static final String UPLOAD_WARRANT_NOTICE_DETAILS_URL = IP_ADDRESS+"/api/DetailsAPI/SaveWarrentAndNoticeDetails?";

    private static final String UPLOAD_MOB_DETAILS_URL = IP_ADDRESS+"/api/Detailsapi/SaveMobDetails?";

    private static final String UPLOAD_COMPLAINT_DETAILS_URL = IP_ADDRESS+"/api/Detailsapi/SaveComplaintDetails?";

    private static final String UPLOAD_CURRENT_LOCATION_DETAILS_URL = IP_ADDRESS+"/api/DetailsAPI/gettrackerdata?uid=";

    private static final String SOS_TRIGGERING_URL = IP_ADDRESS+"/api/DetailsAPI/SOS?";


    //Method for getting user profile details to display in profile page
    public static <T> void getUserDetails(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType, JSONObject data) {

        Log.d("request data", data.toString());

//        String URL = GET_USER_DETAILS+App.getUserId();
        String URL = GET_USER_DETAILS+App.getUserId();

        Log.d("request url, data", URL+"...."+data.toString());

        StringRequest stringRequest = new StringRequest(POST,URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(" USER response success", response.toString());

                        try {
                            if (response != null) {

                                String data = response.toString();

                                Log.d("data", data);

                                JsonParser.getInstance().parseJsonArrayResponse(data, new JsonParser.ListDecodeCallback<T>() {
                                    @Override
                                    public void onSuccess(List<T> responseList) {
                                        responseCallBack.onSuccess(responseList);
                                    }

                                    @Override
                                    public void onError(Exception exception) {
                                        responseCallBack.onError(exception.getMessage());
                                    }
                                }, mType);

                            } else {

                                Log.e("Get user Details", "" + response);

                            }

                        } catch (Exception e) {


                            responseCallBack.onError("");

                            Log.e("Get user Details", e.getMessage());

                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                });


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        stringRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(stringRequest);

        responseCallBack.onStart();
    }


    // Method for getting notification count.
    public static <T> void getNotificationCount(final CallBacks.ObjectCallBackListener responseCallBack, Context mContext, JSONObject postData) {
//        Log.e("jsondata", postData.toString());

        String URL = GET_NOTIFICATION_COUNT+App.getUserId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                GET, URL, null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Notif count success", "" + response.toString());


                        if (response != null)

                        {

                            String data = response.toString();

                            Log.d("data", data);
                            responseCallBack.onSuccess(response);


                        } else {

                            String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                            try {

                                message = response.getJSONObject("status")

                                        .getString("message");

                            } catch (JSONException e) {

                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                                Log.e("NotificationCount", e.getMessage());

                                e.printStackTrace();

                            }

                            responseCallBack.onError("" + message);

                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonObjectRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonObjectRequest);

        responseCallBack.onStart();

    }

    // Method for logging in the user.
    public static <T> void userLogin(final CallBacks.ObjectCallBackListener responseCallBack, Context mContext, JSONObject postData) {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                POST, USER_LOGIN, postData,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("LOGIN response success", "" + response.toString());


                        if (response != null)

                        {

                            String data = response.toString();

                            Log.d("data", data);
                            responseCallBack.onSuccess(response);


                        } else {

                            String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                            try {

                                message = response.getJSONObject("status")

                                        .getString("message");

                            } catch (JSONException e) {

                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                                Log.e("SIGNIN", e.getMessage());

                                e.printStackTrace();

                            }

                            responseCallBack.onError("" + message);

                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonObjectRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonObjectRequest);

        responseCallBack.onStart();

    }


    // Method for checking password change for user.
    public static <T> void userPasswordCheck(final CallBacks.ObjectCallBackListener responseCallBack, JSONObject postData) {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                POST, USER_PASSWORD_CONFIRMATION, postData,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Password Check success", "" + response.toString());


                        if (response != null)

                        {

                            String data = response.toString();

                            Log.d("data", data);
                            responseCallBack.onSuccess(response);


                        } else {

                            String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                            try {

                                message = response.getJSONObject("status")

                                        .getString("message");

                            } catch (JSONException e) {

                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                                Log.e("SIGNIN", e.getMessage());

                                e.printStackTrace();

                            }

                            responseCallBack.onError("" + message);

                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonObjectRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonObjectRequest);

        responseCallBack.onStart();

    }


    // Posting User fcm registration Id To server
    public static void postFcmRegistrationId(final CallBacks.StringCallBackListener responseCallBack, final JSONObject data) {

        String URL = USER_FCM_REGISTRATION + data;

        Log.e("USER_FCM_REGISTRATION", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("success", response);
                        responseCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        stringRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(stringRequest);

        responseCallBack.onStart();


    }


    //    Method for getting Atm centers details .
    public static <T> void getAtmDetails(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {

        String URL = GET_ATM_DETAILS+App.getUserId();
        Log.d("request url", URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

   // Method for getting bank details.
    public static <T> void getBankDetails(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {

        String BANK_URL = GET_BANK_DETAILS+App.getUserId();
        Log.d("request url", BANK_URL);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BANK_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

    //Method for getting pg address details
    public static <T> void getPgDetails(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {

        String PG_URL = GET_PG_DETAILS+App.getUserId();
        Log.d("request url", PG_URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(PG_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

    // Method for getting petrol bunk details
    public static <T> void getPetrolBunkDetails(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {

        String PETROL_URL = GET_PETROL_BUNK_DETAILS+App.getUserId();
        Log.d("request url", PETROL_URL);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(PETROL_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

    // Method for getting religious places details
    public static <T> void getReligiousDetails(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {

//        Log.d("request data", data.toString());

        String RELIGIOUS_URL = GET_RELIGIOUS_DETAILS+App.getUserId();
        Log.d("request url", RELIGIOUS_URL);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(RELIGIOUS_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

    // Method for getting wine shops and bars details
    public static <T> void getWineShopDetails(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {

//        Log.d("request data", data.toString());

        String WINE_URL = GET_WINE_SHOP_DETAILS+App.getUserId();
        Log.d("request url", WINE_URL);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(WINE_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

// Method for getting slum area details
    public static <T> void getSlumAreaDetails(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {

        String SLUM_URL = GET_SLUM_DETAILS+App.getUserId();
        Log.d("request url", SLUM_URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(SLUM_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

    // Method for getting check point details
    public static <T> void getCheckPointDetails(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {

        String CHECK_URL = GET_CHECK_POINT_DETAILS+App.getUserId();
        Log.d("request url", CHECK_URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(CHECK_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

    // Method for getting bolo details
    public static <T> void getBoloDetails(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {

        String BOLO_URL = GET_BOLO_DETAILS+App.getUserId();
        Log.d("request url", BOLO_URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BOLO_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

    // Method for getting mob type details
    public static <T> void getMobTypeDetails(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(GET_MOB_TYPE_DETAILS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

    // Method for getting complaintType details
    public static <T> void getComplaintType(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {

        Log.d("request url", GET_COMPLAINT_TYPE_DETAILS);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(GET_COMPLAINT_TYPE_DETAILS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

    // Method for getting complaint status details
    public static <T> void getComplaintStatus(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {

        Log.d("request url", GET_COMPLAINT_STATUS_DETAILS);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(GET_COMPLAINT_STATUS_DETAILS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

    // Method for getting Notification details
    public static <T> void getNotificationDetails(final CallBacks.ListCallBackListener<T> responseCallBack, final Class<T> mType) {

        String NOTIFICATION_URL = GET_NOTIFICATION_DETAILS+App.getUserId()+"&number="+10*App.getMessageCount();
        Log.d("request url", NOTIFICATION_URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(NOTIFICATION_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());


                        // Parsing json array response
                        // loop through each json object
                        String jsonResponse = response.toString();
                        JsonParser.getInstance().parseJsonArrayResponse(jsonResponse, new JsonParser.ListDecodeCallback<T>() {
                            @Override
                            public void onSuccess(List<T> responseList) {
                                Log.e("response", responseList.toString());
                                responseCallBack.onSuccess(responseList);

                            }

                            @Override
                            public void onError(Exception exception) {
                                responseCallBack.onError(exception.getMessage());
                            }
                        }, mType);


                    }
                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        });


        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonArrayRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonArrayRequest);

        responseCallBack.onStart();
    }

    // Method for uploading images for all the categories
    public static <T> void uploadImages(final CallBacks.StringCallBackListener responseCallBack, List<File> files, String type) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();


        if(files != null) {

            try {
                for (int i = 0; i < files.size(); i++) {
                    params.put("image" + (i + 1), files.get(i));
                }

            } catch (FileNotFoundException e) {
                Log.e("FILENOT_FOUND", "YES");
            }
        }

        switch (type) {
            case "Atm":
                UPLOAD_URL = UPLOAD_ATM_IMAGES_URL;
                break;
            case "Bank":
                UPLOAD_URL = UPLOAD_BANK_IMAGES_URL;
                break;
            case "Religious":
                UPLOAD_URL = UPLOAD_RELIGIOUS_IMAGES_URL;
                break;
            case "Pg":
                UPLOAD_URL = UPLOAD_PG_IMAGES_URL;
                break;
            case "PetrolBunk":
                UPLOAD_URL = UPLOAD_PETROL_BUNK_IMAGES_URL;
                break;
            case "SlumArea":
                UPLOAD_URL = UPLOAD_SLUM_AREA_IMAGES_URL;
                break;
            case "Bolo":
                UPLOAD_URL = UPLOAD_BOLO_IMAGES_URL;
                break;
            case "WineShop":
                UPLOAD_URL = UPLOAD_WINE_SHOP_IMAGES_URL;
                break;
            case "CheckPoint":
                UPLOAD_URL = UPLOAD_CHECK_POINT_IMAGES_URL;
                break;
            case "BadCharacter":
                UPLOAD_URL = UPLOAD_BAD_CHARACTER_IMAGES_URL;
                break;
            case "StolenProperty":
                UPLOAD_URL = UPLOAD_STOLEN_PROPERTY_IMAGES_URL;
                break;
           case "WarrantAndNotice":
                UPLOAD_URL = UPLOAD_WARRENT_NOTICE_IMAGES_URL;
                break;
            case "Mob":
                UPLOAD_URL = UPLOAD_MOB_IMAGES_URL;
                break;
            case "Complaint":
                UPLOAD_URL = UPLOAD_COMPLAINT_IMAGES_URL;
                break;

        }

        client.post(UPLOAD_URL, params, new TextHttpResponseHandler() {


            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("fail ststuscode", statusCode + responseString);
                responseCallBack.onError("Image Upload Failed");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.e("ststuscode", statusCode + responseString);
                responseCallBack.onSuccess( ""+statusCode+responseString);
            }
        });


    }

    // Method for uploading profile image
    public static <T> void uploadProfileImages(final CallBacks.StringCallBackListener responseCallBack, File file) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        if(file != null) {

            try {
                params.put("profileImage", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }



        client.post(UPLOAD_PROFILE_IMAGE, params, new TextHttpResponseHandler() {


            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.e("fail ststuscode", statusCode + responseString);
                responseCallBack.onError("Image Upload Failed");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.e("ststuscode", statusCode + responseString);
                responseCallBack.onSuccess( ""+statusCode+responseString);
            }
        });


    }

    // Posting Atm checked details to server
    public static void postAtmDetails(final CallBacks.StringCallBackListener responseCallBack, final JSONObject data) {

        String URL = UPLOAD_ATM_DETAILS_URL + data;

        Log.e("UPLOAD_ATM_DETAILS_URL", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("success", response);
                        responseCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        stringRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(stringRequest);

        responseCallBack.onStart();


    }

    // Posting  Bank checked details to server
    public static void postBankDetails(final CallBacks.StringCallBackListener responseCallBack, final JSONObject data) {

        String URL = UPLOAD_BANK_DETAILS_URL + data;

        Log.e("UPLOAD_BANK_DETAILS_URL", UPLOAD_BANK_DETAILS_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("success", response);
                        responseCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        stringRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(stringRequest);

        responseCallBack.onStart();


    }


    // Posting  PG checked details to server
    public static void postPgDetails(final CallBacks.StringCallBackListener responseCallBack, final JSONObject data) {

        String URL = UPLOAD_PG_DETAILS_URL + data;

        Log.e("UPLOAD_PG_DETAILS_URL", UPLOAD_PG_DETAILS_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("success", response);
                        responseCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        stringRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(stringRequest);

        responseCallBack.onStart();


    }

    // Posting  petrol bunk checked details to server
    public static void postPetrolBunkDetails(final CallBacks.StringCallBackListener responseCallBack, final JSONObject data) {

        String URL = UPLOAD_PETROL_BUNK_DETAILS_URL + data;

        Log.e("UPLOAD_PETROL_BUNK_URL", UPLOAD_PETROL_BUNK_DETAILS_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("success", response);
                        responseCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        stringRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(stringRequest);

        responseCallBack.onStart();


    }

    // Posting  religious checked details to server
    public static void postReligiousDetails(final CallBacks.StringCallBackListener responseCallBack, final JSONObject data) {

        String URL = UPLOAD_RELIGIOUS_DETAILS_URL + data;

        Log.e("UPLOAD_RELIGIOUS_DET", UPLOAD_RELIGIOUS_DETAILS_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("success", response);
                        responseCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        stringRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(stringRequest);

        responseCallBack.onStart();


    }


    // Posting  wineshop and bar checked details to server
    public static void postWineShopDetails(final CallBacks.StringCallBackListener responseCallBack, final JSONObject data) {

        String URL = UPLOAD_WINE_SHOP_DETAILS_URL + data;

        Log.e("UPLOAD_WINE_SHOP_URL", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("success", response);
                        responseCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        stringRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(stringRequest);

        responseCallBack.onStart();


    }


    // Posting  slum area checked details to server
    public static void postSlumAreaDetails(final CallBacks.StringCallBackListener responseCallBack, final JSONObject data) {

        String URL = UPLOAD_SLUM_AREA_DETAILS_URL + data;

        Log.e("UPLOAD_SLUM_DETAILS_URL", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("success", response);
                        responseCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        stringRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(stringRequest);

        responseCallBack.onStart();


    }

    // Posting  bolo checked details to server
    public static <T> void postBoloDetails(final CallBacks.ObjectCallBackListener responseCallBack, Context mContext, JSONObject postData) {
        Log.e("jsondata", postData.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                POST, UPLOAD_BOLO_DETAILS_URL, postData,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Bolo success", "" + response.toString());


                        if (response != null)

                        {

                            Object data = null;
                            try {
                                data = response.get("output");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.d("data", ""+data);
                            responseCallBack.onSuccess(data);


                        } else {

                            String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                            try {

                                message = response.getJSONObject("status")

                                        .getString("message");

                            } catch (JSONException e) {

                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                                Log.e("SIGNIN", e.getMessage());

                                e.printStackTrace();

                            }

                            responseCallBack.onError("" + message);

                        }

                    }

                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonObjectRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonObjectRequest);

        responseCallBack.onStart();

    }
    // Posting  check point checked details to server
    public static <T> void postCheckPointDetails(final CallBacks.ObjectCallBackListener responseCallBack, Context mContext, JSONObject postData) {
        Log.e("jsondata", postData.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                POST, UPLOAD_CHECK_POINT_DETAILS_URL, postData,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Checkpoint success", "" + response.toString());


                        if (response != null)

                        {

                            Object data = null;
                            try {
                                data = response.get("output");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.d("data", ""+data);
                            responseCallBack.onSuccess(data);


                        } else {

                            String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                            try {

                                message = response.getJSONObject("status")

                                        .getString("message");

                            } catch (JSONException e) {

                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                                Log.e("SIGNIN", e.getMessage());

                                e.printStackTrace();

                            }

                            responseCallBack.onError("" + message);

                        }

                    }

                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonObjectRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonObjectRequest);

        responseCallBack.onStart();

    }

    // Posting  bad character checked details to server
    public static <T> void postBadCharacterDetails(final CallBacks.ObjectCallBackListener responseCallBack, Context mContext, JSONObject postData) {
        Log.e("jsondata", postData.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                POST, UPLOAD_BAD_CHARAC_DETAILS_URL, postData,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Bad character success", "" + response.toString());


                        if (response != null)

                        {

                            Object data = null;
                            try {
                                data = response.get("output");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.d("data", ""+data);
                            responseCallBack.onSuccess(data);


                        } else {

                            String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                            try {

                                message = response.getJSONObject("status")

                                        .getString("message");

                            } catch (JSONException e) {

                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                                Log.e("SIGNIN", e.getMessage());

                                e.printStackTrace();

                            }

                            responseCallBack.onError("" + message);

                        }

                    }

                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonObjectRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonObjectRequest);

        responseCallBack.onStart();

    }

    // Posting  stolen property checked details to server
    public static <T> void postStolenPropertyDetails(final CallBacks.ObjectCallBackListener responseCallBack, Context mContext, JSONObject postData) {
        Log.e("jsondata", postData.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                POST, UPLOAD_STOLEN_PROPERTY_DETAILS_URL, postData,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("stolen property success", "" + response.toString());


                        if (response != null)

                        {

                            Object data = null;
                            try {
                                data = response.get("output");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.d("data", ""+data);
                            responseCallBack.onSuccess(data);


                        } else {

                            String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                            try {

                                message = response.getJSONObject("status")

                                        .getString("message");

                            } catch (JSONException e) {

                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                                Log.e("SIGNIN", e.getMessage());

                                e.printStackTrace();

                            }

                            responseCallBack.onError("" + message);

                        }

                    }

                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonObjectRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonObjectRequest);

        responseCallBack.onStart();

    }

    // Posting  Warrant and Notice checked details to server
    public static <T> void postWarrantAndNoticeDetails(final CallBacks.ObjectCallBackListener responseCallBack, Context mContext, JSONObject postData) {
        Log.e("jsondata", postData.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                POST, UPLOAD_WARRANT_NOTICE_DETAILS_URL, postData,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("warrant success", "" + response.toString());


                        if (response != null)

                        {

                            Object data = null;
                            try {
                                data = response.get("output");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.d("data", ""+data);
                            responseCallBack.onSuccess(data);


                        } else {

                            String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                            try {

                                message = response.getJSONObject("status")

                                        .getString("message");

                            } catch (JSONException e) {

                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                                Log.e("SIGNIN", e.getMessage());

                                e.printStackTrace();

                            }

                            responseCallBack.onError("" + message);

                        }

                    }

                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonObjectRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonObjectRequest);

        responseCallBack.onStart();

    }


    // Posting  Mob checked details to server
    public static <T> void postMobDetails(final CallBacks.ObjectCallBackListener responseCallBack, Context mContext, JSONObject postData)
    {
        Log.e("jsondata", postData.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                POST, UPLOAD_MOB_DETAILS_URL, postData,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Mob success", "" + response.toString());


                        if (response != null)

                        {

                            Object data = null;
                            try {
                                data = response.get("output");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.d("data", ""+data);
                            responseCallBack.onSuccess(data);


                        } else {

                            String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                            try {

                                message = response.getJSONObject("status")

                                        .getString("message");

                            } catch (JSONException e) {

                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                                Log.e("SIGNIN", e.getMessage());

                                e.printStackTrace();

                            }

                            responseCallBack.onError("" + message);

                        }

                    }

                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonObjectRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonObjectRequest);

        responseCallBack.onStart();

    }

    // Posting  complaint checked details to server
    public static <T> void postComplaintDetails(final CallBacks.ObjectCallBackListener responseCallBack, Context mContext, JSONObject postData)
    {
        Log.e("jsondata", postData.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                POST, UPLOAD_COMPLAINT_DETAILS_URL, postData,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Complaint success", "" + response.toString());


                        if (response != null)

                        {

                            Object data = null;
                            try {
                                data = response.get("output");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.d("data", ""+data);
                            responseCallBack.onSuccess(data);


                        } else {

                            String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                            try {

                                message = response.getJSONObject("status")

                                        .getString("message");

                            } catch (JSONException e) {

                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);;

                                Log.e("SIGNIN", e.getMessage());

                                e.printStackTrace();

                            }

                            responseCallBack.onError("" + message);

                        }

                    }

                },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error.networkResponse.data
                Log.d(TAG, "volley Error String" + error);
                JSONObject errorResponseBody = null;

                String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                if (error.networkResponse != null) {

                    String jsonString = null;

                    try {

                        jsonString = new String(

                                error.networkResponse.data,

                                HttpHeaderParser

                                        .parseCharset(error.networkResponse.headers));

                        Log.d(TAG, "json Error String" + jsonString);

                        errorResponseBody = new JSONObject(jsonString);

                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();

                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                    if (errorResponseBody != null) {

                        try {

                            message = errorResponseBody.getJSONObject(

                                    "status").getString("message");

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }

                    }

                    responseCallBack.onError(message);

                } else {

                    if (error instanceof NoConnectionError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                    } else if (error instanceof ServerError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                    } else if (error instanceof TimeoutError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                    } else if (error instanceof NetworkError) {
                        message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                    }

                }

                responseCallBack.onError(message);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );

        // to cancel the request when cancelAllPendingTask method is called we

        // are adding this tag

        jsonObjectRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(jsonObjectRequest);

        responseCallBack.onStart();

    }


    // Triggering sos sending user id sos type id to server
    public static void postSOSDetails(final CallBacks.StringCallBackListener responseCallBack, final int data) {

        String URL = SOS_TRIGGERING_URL+"uid="+App.getUserId()+"&sosid="+data;

        Log.e("SOS_TRIGGERING_URL", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("success", response);
                        responseCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        stringRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(stringRequest);

        responseCallBack.onStart();


    }


    // Posting Atm checked details to server
    public static void postCurrentLatLong(final CallBacks.StringCallBackListener responseCallBack, double latitude, double longitude ) {

        String URL = UPLOAD_CURRENT_LOCATION_DETAILS_URL+App.getUserId()+"&lat="+latitude+"&lon="+longitude ;

        Log.e("UPLOAD_LOCATION_URL", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("success", response);
                        responseCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.networkResponse.data
                        Log.d(TAG, "volley Error String" + error);
                        JSONObject errorResponseBody = null;

                        String message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                        if (error.networkResponse != null) {

                            String jsonString = null;

                            try {

                                jsonString = new String(

                                        error.networkResponse.data,

                                        HttpHeaderParser

                                                .parseCharset(error.networkResponse.headers));

                                Log.d(TAG, "json Error String" + jsonString);

                                errorResponseBody = new JSONObject(jsonString);

                            } catch (UnsupportedEncodingException e) {

                                e.printStackTrace();

                            } catch (JSONException e) {

                                e.printStackTrace();

                            }

                            if (errorResponseBody != null) {

                                try {

                                    message = errorResponseBody.getJSONObject(

                                            "status").getString("message");

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                            }

                            responseCallBack.onError(message);

                        } else {

                            if (error instanceof NoConnectionError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.BAD_HAPPENED);

                            } else if (error instanceof ServerError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.SERVER_ERROR);

                            } else if (error instanceof TimeoutError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_TIMEOUT);

                            } else if (error instanceof NetworkError) {
                                message = App.getApplicationInstance().getResources().getString(R.string.NETWORK_ERROR);

                            }

                        }

                        responseCallBack.onError(message);
                    }
                }) {

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        stringRequest.setTag(MyVolley.TAG);

        MyVolley.addToRequestQueue(stringRequest);

        responseCallBack.onStart();


    }



}
