package com.tarrasqu3.waterreminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class WaterReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val startHour = prefs.getInt("startHour", 8)
        val endHour = prefs.getInt("endHour", 22)
        val intervalMinutes = prefs.getInt("intervalMinutes", 60)

        val now = java.util.Calendar.getInstance()
        val currentHour = now.get(java.util.Calendar.HOUR_OF_DAY)

        if (currentHour in startHour until endHour) {
            showNotification(context)
        }

        scheduleNextAlarm(context, intervalMinutes)
    }

    private fun showNotification(context: Context) {
        val channelId = "water_reminder"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Promemoria acqua",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("💧 Bevi acqua!")
            .setContentText("Ricordati di idratare il tuo corpo!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(1, notification)
    }

    companion object {
//        fun scheduleNextAlarm(context: Context, intervalMinutes: Int) {
//            val alarmManager = context.getSystemService(AlarmManager::class.java)
//            val intent = Intent(context, WaterReminderReceiver::class.java)
//            val pendingIntent = PendingIntent.getBroadcast(
//                context, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//            val triggerTime = System.currentTimeMillis() + intervalMinutes * 60 * 1000L
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                if (alarmManager.canScheduleExactAlarms()) {
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
//                } else {
//                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
//                }
//            } else {
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
//            }
//        }
        fun scheduleNextAlarm(context: Context, intervalMinutes: Int) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)
            val intent = Intent(context, WaterReminderReceiver::class.java)
            intent.action = "WATER_REMINDER"
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val triggerTime = System.currentTimeMillis() + intervalMinutes * 60 * 1000L

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        }
        fun sendTestNotification(context: Context) {
            val channelId = "water_reminder"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId, "Promemoria acqua",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                context.getSystemService(NotificationManager::class.java)
                    .createNotificationChannel(channel)
            }
//            val notification = NotificationCompat.Builder(context, channelId)
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
//                .setContentTitle("💧 Prova notifica!")
//                .setContentText("Se vedi questo, le notifiche funzionano!, bevi un bicchiere di acqua")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true)
//                .build()
//            context.getSystemService(NotificationManager::class.java).notify(2, notification)
        }
    }
}