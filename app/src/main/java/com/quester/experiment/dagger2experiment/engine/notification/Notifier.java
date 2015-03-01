package com.quester.experiment.dagger2experiment.engine.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.quester.experiment.dagger2experiment.R;
import com.quester.experiment.dagger2experiment.data.checkpoint.Checkpoint;
import com.quester.experiment.dagger2experiment.ui.CheckpointReachedActivity;

import static android.content.Context.*;

public class Notifier {

    private NotificationManager notificationManager;
    private Context context;

    public Notifier(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    public void notifyCheckpointReached(Checkpoint checkpoint) {

        notificationManager.notify((int) checkpoint.getId(), buildNotification(checkpoint));
    }

    private Notification buildNotification(Checkpoint checkpoint) {

        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("New Checkpoint reached")
                .setContentIntent(createPendingIntent())
                .setAutoCancel(true)
                .setContentText("Checkpoint id=" + checkpoint.getId())
                .build();
    }

    private PendingIntent createPendingIntent() {
        return PendingIntent.getActivity(
                context,
                0,
                new Intent(context, CheckpointReachedActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }
}
