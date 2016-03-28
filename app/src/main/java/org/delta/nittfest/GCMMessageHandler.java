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


public class GCMMessageHandler extends IntentService {

    String mes,title;

    public GCMMessageHandler() {
        super("GCMMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        title = extras.getString("title");
        mes = extras.getString("data");
        Log.d("gcm_status", mes);
        Log.d("gcm_status_title",title);
        if(mes!=null)
            gn(getApplicationContext(),mes,title);
    }

    private static void gn(Context context,String newMessage,String title)
    {

        if (newMessage != null) {
            NotificationCompat.Builder mBuilder =

                    new NotificationCompat.Builder(context).setSmallIcon(R.drawable.logo_nc)
                            .setContentTitle(title)
                            .setContentText(newMessage)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setLights(Color.GREEN, 500, 500);
            DBController controller = new DBController(context);
            HashMap<String, String> queryValues = new HashMap<String, String>();

            Calendar c = Calendar.getInstance();
            String currentDateandTime = new SimpleDateFormat("HH:mm").format(new Date());
            queryValues.put("notifText",newMessage);
            String t=currentDateandTime+" , Day "+String.valueOf((31-c.get(Calendar.DATE))%31);
            Log.e("time",t);
            queryValues.put("time",t);
            queryValues.put("title",title);
            controller.insertNotif(queryValues);
            NotificationCompat.InboxStyle inboxStyle;

            inboxStyle = new NotificationCompat.InboxStyle();
            String[] events;
            //TODO push notifications to be stacked

            events = newMessage.split("\n");
            inboxStyle.setBigContentTitle(title);

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

}
