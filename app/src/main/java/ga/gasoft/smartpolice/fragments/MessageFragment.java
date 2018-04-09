package ga.gasoft.smartpolice.fragments;

/**
 * Created by devendiran on 8/18/2016.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.MainActivity;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;
import ga.gasoft.smartpolice.adapters.CustomAdapter;
import ga.gasoft.smartpolice.R;
import ga.gasoft.smartpolice.model.Notification;


public class MessageFragment extends Fragment {
    ListView lv;
    private ArrayList<Notification> notificationList;
    private ProgressDialog pDialog;
    private CustomAdapter notificationAdapter;
    private int notificationCount;
    private boolean isVisible;
    private boolean isStarted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.message, null);

        App.setMessageCount(1);
        notificationList = new ArrayList<Notification>();

        lv=(ListView) rootView.findViewById(R.id.listView);

        notificationAdapter = new CustomAdapter(getActivity(),R.layout.message_listitem, notificationList, MessageFragment.this);
        lv.setAdapter(notificationAdapter);

        if (isVisible && isStarted){
           getNotificationDetails();
        }

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart ","called");
        isStarted = true;
        if (isVisible && isStarted){
             getNotificationDetails();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
        Log.e("set User Visible ","called");
        isVisible = isVisibleToUser;
        if(isStarted)
        getNotificationDetails();
    }



    // Method for fetching Notification details
    public void getNotificationDetails() {
        if (NetUtils.isOnline(getContext())) {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getResources().getString(R.string.GetDetails));
            pDialog.setCancelable(false);
            pDialog.show();

            isStarted = false;
            isVisible = false;
            UserNetworkManager.getNotificationDetails(new CallBacks.ListCallBackListener<Notification>() {

                @Override
                public void onSuccess(List<Notification> responseList) {


                    if (responseList != null && responseList.size() > 0) {

                        for (int i = 0; i < responseList.size(); i++) {
                            if(i == responseList.size()-1) {
                                notificationCount = Integer.parseInt(responseList.get(i).Message);
                                App.setNotificationCount(notificationCount);
                                Log.e("notification details", responseList.get(i).toString()+"..."+ notificationCount);
                            }else if(!responseList.get(i).Type.equalsIgnoreCase("o")){
                                notificationList.add((responseList.get(i)));
                                Log.e("notification details", responseList.get(i).toString()+"..."+ notificationCount);
                            }

                        }
                        notificationAdapter.notifyDataSetChanged();

                        ((MainActivity)getActivity()).countTextView.setText("");
                        ((MainActivity)getActivity()).countTextView.setVisibility(View.INVISIBLE);
                        ((MainActivity)getActivity()).msgCount=0;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                    pDialog.dismiss();
                        }
                    }, 500);

                }

                @Override
                public void onError(String errorMessage) {
                    pDialog.dismiss();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStart() {

                }
            }, Notification.class);
        } else {
            Toast.makeText(getContext(),getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("onActivityCreated", "in fragment");

    }
}