package ga.gasoft.smartpolice.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.R;
import ga.gasoft.smartpolice.fragments.MessageFragment;
import ga.gasoft.smartpolice.model.Notification;

public class CustomAdapter extends ArrayAdapter<Notification> {
    String [] result;
    Context context;
    int [] imageId;
    String[] msg;
    String[] time;
    private List<Notification> notificationList;
    private static LayoutInflater inflater=null;
    private Notification notification;
    private int layoutResourceId;
    private Holder holder;
    private int notificationCount;
    private MessageFragment messageFragment;
    private String[] dateTime;


    public CustomAdapter(Context mainActivity, int layoutResource, ArrayList<Notification> list, MessageFragment fragment) {
        super(mainActivity, layoutResource, list);
        // TODO Auto-generated constructor stub
//        result=prgmNameList;
        context=mainActivity;
        layoutResourceId = layoutResource;
        messageFragment = fragment;
        notificationList = list;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv,time,msg;
        ImageView img;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView = convertView;
        notification = getItem(position);

        if(rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rowView = inflater.inflate(layoutResourceId, parent, false);
            holder.time = (TextView) rowView.findViewById(R.id.time);
            holder.msg = (TextView) rowView.findViewById(R.id.message);

//
           rowView.setTag(holder);
        }else{
            holder = (Holder) rowView.getTag();
        }



            if (notification.DateTime != null) {
                dateTime = notification.DateTime.split("T");
            }
//        holder.tv.setText(result[position]);
            holder.msg.setText(notification.Message.trim());
            if (dateTime != null) {

                holder.time.setText(dateTime[0] + " " + dateTime[1]);
            }


//        holder.img.setImageResource(imageId[position]);
       /* rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });*/
        if(reachedEndOfList(position) &&  notificationList.size() == 10*App.getMessageCount()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    App.setMessageCount(App.getMessageCount()+1);
                    messageFragment.getNotificationDetails();
                }
            }, 500);

        }
        return rowView;
    }

    private boolean reachedEndOfList(int position) {
        // can check if close or exactly at the end
        if(notificationList != null && notificationList.size()>0)
        return position == notificationList.size() - 1;
        else
            return false;
    }

}