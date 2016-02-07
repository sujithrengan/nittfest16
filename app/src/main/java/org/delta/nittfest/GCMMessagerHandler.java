package org.delta.nittfest;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class GCMMessagerHandler extends IntentService {

    String mes;

    public GCMMessagerHandler() {
        super("GCMMessagerHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        mes = extras.getString("data");
        //clear(mes);//adding to db
        //Log.d("gcm_status", mes);
        //generateNotification(getApplicationContext(), mes);
        if(mes!=null)
        gn(getApplicationContext(),mes);
    }

    private static void gn(Context context,String newMessage)
    {

        if (newMessage != null) {
            NotificationCompat.Builder mBuilder =

                    new NotificationCompat.Builder(context).setSmallIcon(R.drawable.logo_nc)
                            .setContentTitle("Nittfest 2016")
                            .setContentText(newMessage)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setLights(Color.GREEN, 500, 500);

            //mBuilder.setLights(Color.BLUE, 500, 500);
            //long[] pattern = {500,500,500,500,500,500,500,500,500};
            //mBuilder.setVibrate(pattern);
            DBController controller = new DBController(context);
            HashMap<String, String> queryValues = new HashMap<String, String>();

            // Add departmentName extracted from Object
            Calendar c = Calendar.getInstance();
            String currentDateandTime = new SimpleDateFormat("HH:mm").format(new Date());
           // date = c.get(Calendar.DATE);
            queryValues.put("notifText",newMessage);
            //String t=String.valueOf(c.get(Calendar.HOUR))+":"+String.valueOf(c.get(Calendar.MINUTE))+" , Day "+String.valueOf(c.get(Calendar.DATE)-25);
            String t=currentDateandTime+" , Day "+String.valueOf(c.get(Calendar.DATE)-25);
            Log.e("time",t);
            queryValues.put("time",t);

            // Add userID extracted from Object

            // Insert User into SQLite DB

            controller.insertNotif(queryValues);
            NotificationCompat.InboxStyle inboxStyle;

            inboxStyle = new NotificationCompat.InboxStyle();
            String[] events = new String[6];

            events = newMessage.split("\n");
            inboxStyle.setBigContentTitle("Event details:");

            for (int i = 0; i < events.length; i++) {

                inboxStyle.addLine(events[i]);
            }
            mBuilder.setStyle(inboxStyle);

            Intent resultIntent = new Intent(context, Notify.class);

            Bundle b = new Bundle();
            b.putString("key", newMessage);
            resultIntent.putExtras(b);

            // The stack builder object will contain an artificial back stack
            // for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out
            // of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(Notify.class);
            // Adds the Intent that starts the Activity to the top of the stack

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            // Start of a loop that processes data and then notifies the user

            //mBuilder.setContentText(newMessage).setNumber(numMessages);

            // Sets an ID for the notification, so it can be updated

            // mId allows you to update the notification later on.
            mBuilder.setAutoCancel(true);
            mNotificationManager.notify(100, mBuilder.build());

        }



    }

    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.logo_nc;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        notification = new Notification(icon, "Festember", when);
        String title = message;

        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        //notification.setLatestEventInfo(context,"CampusComm", message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(666, notification);
    }

    public void clear(String not) {
        /*MyDBHandler dbHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
        dbHandler.addName(not, "posts");*/
    }
}
