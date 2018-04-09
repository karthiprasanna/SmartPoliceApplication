package ga.gasoft.smartpolice.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

import ga.gasoft.smartpolice.MainActivity;
import ga.gasoft.smartpolice.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    NotificationCompat.Builder notificationBuilder;


    Bitmap image;
    private int uniqueId;


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
//        Log.e(TAG, "From: " + remoteMessage.getFrom());
////        Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//        Log.e(TAG, "FCM Data: " + remoteMessage.getData());
//
////        String tag = remoteMessage.getNotification();
////        String title = remoteMessage.getData().get("contentTitle");
//        String msg = remoteMessage.getData().get("message");
//        String img = remoteMessage.getData().get("image");

        Log.e(TAG, "From: " + remoteMessage.getFrom());
//        Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//        Log.e(TAG, "FCM Data: " + remoteMessage.getNotification().getBody());

        String tag = remoteMessage.getNotification().getTag();
        String msg = remoteMessage.getNotification().getBody().toString();


//        Log.e(TAG, "FCM Data: " + msg+"......"+remoteMessage.getNotification().getTitle());
//        String msg = remoteMessage.getData().get("message");
//        String img = remoteMessage.getData().get("image");


//        image = getBitmapFromURL(img);

//        sendNotification(tag, msg, image, remoteMessage);


//        image = getBitmapFromURL(img);
//
//        sendNotification(tag, msg, image);

        sendNotification(remoteMessage);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
   /* private void sendNotification(String tag, String messageBody, Bitmap img) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0  , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (tag != null && tag.equalsIgnoreCase("image")) {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Smart Police")
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(messageBody))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Smart Police")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(messageBody))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setContentText(messageBody);
        }

        uniqueId = (int) (System.currentTimeMillis() & 0xfffffff);;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setSound(defaultSoundUri);
        notificationManager.notify(0 , notificationBuilder.build());
    }

*/




   private void sendNotification(RemoteMessage messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Notification", 10);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
//       Settings.System.putInt(getContentResolver(),android.provider.Settings.System.NOTIFICATION_SOUND, 1);

       Log.e("onNotification", "called");
       if(messageBody.getNotification() != null && messageBody.getNotification().getTitle() != null && !messageBody.getNotification().getTitle().equalsIgnoreCase("Smart Police")) {
//           Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
           Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/"+R.raw.emergency_alert);

           Notification notificationBuilder = new NotificationCompat.Builder(this)
                   .setSmallIcon(R.drawable.smart_police_icon)
                   .setContentTitle(messageBody.getNotification().getTitle())
                   .setContentText(messageBody.getNotification().getBody().trim())
                   .setAutoCancel(true)
                   .setSound(defaultSoundUri)
                   .setColor(getResources().getColor(R.color.colorPrimaryDark))
                   .setContentIntent(pendingIntent).build();

           notificationBuilder.sound = defaultSoundUri;
           NotificationManager notificationManager =
                   (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//           MediaPlayer mp= MediaPlayer.create(getApplicationContext(), R.raw.emergency_alert);
//           mp.start();
           notificationManager.notify(0, notificationBuilder);
       }else{
           Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//           Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/"+R.raw.emergency_alert);
           Notification notificationBuilder = new NotificationCompat.Builder(this)
                   .setSmallIcon(R.drawable.smart_police_icon)
                   .setContentTitle(messageBody.getNotification().getTitle())
                   .setContentText(messageBody.getNotification().getBody().trim())
                   .setAutoCancel(true)
                   .setColor(getResources().getColor(R.color.colorPrimaryDark))
                   .setSound(defaultSoundUri)
                   .setContentIntent(pendingIntent).build();

           notificationBuilder.sound = defaultSoundUri;
           NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
           notificationManager.notify(0, notificationBuilder);
       }
    }
}
