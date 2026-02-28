package com.tarrasqu3.waterreminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val switchEnabled = findViewById<Switch>(R.id.switchEnabled)
        val pickerStart = findViewById<NumberPicker>(R.id.pickerStart)
        val pickerEnd = findViewById<NumberPicker>(R.id.pickerEnd)
        val pickerInterval = findViewById<NumberPicker>(R.id.pickerInterval)
        val btnSave = findViewById<Button>(R.id.btnSave)

        pickerStart.minValue = 0; pickerStart.maxValue = 23
        pickerEnd.minValue = 0; pickerEnd.maxValue = 23

        val intervals = arrayOf("20 min", "30 min", "45 min", "60 min", "90 min", "120 min")
        val intervalValues = intArrayOf(20, 30, 45, 60, 90, 120)
        pickerInterval.minValue = 0
        pickerInterval.maxValue = intervals.size - 1
        pickerInterval.displayedValues = intervals

        // Carica valori salvati
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        switchEnabled.isChecked = prefs.getBoolean("enabled", true)
        pickerStart.value = prefs.getInt("startHour", 8)
        pickerEnd.value = prefs.getInt("endHour", 22)
        val savedInterval = prefs.getInt("intervalMinutes", 60)
        val savedIndex = intervalValues.indexOfFirst { it == savedInterval }
        if (savedIndex >= 0) pickerInterval.value = savedIndex

        // Funzione per aggiornare lo stato visivo dei picker
        fun updatePickersState(enabled: Boolean) {
            pickerStart.isEnabled = enabled
            pickerEnd.isEnabled = enabled
            pickerInterval.isEnabled = enabled
            pickerStart.alpha = if (enabled) 1f else 0.4f
            pickerEnd.alpha = if (enabled) 1f else 0.4f
            pickerInterval.alpha = if (enabled) 1f else 0.4f
        }

        // Applica subito al caricamento
        updatePickersState(switchEnabled.isChecked)

        // Aggiorna quando l'utente tocca lo switch
        switchEnabled.setOnCheckedChangeListener { _, isChecked ->
            updatePickersState(isChecked)
        }

        btnSave.setOnClickListener {
            val isEnabled = switchEnabled.isChecked

            prefs.edit()
                .putBoolean("enabled", isEnabled)
                .putInt("startHour", pickerStart.value)
                .putInt("endHour", pickerEnd.value)
                .putInt("intervalMinutes", intervalValues[pickerInterval.value])
                .apply()

            if (isEnabled) {
                WaterReminderReceiver.scheduleNextAlarm(this, intervalValues[pickerInterval.value])
                WaterReminderReceiver.sendTestNotification(this)
                Toast.makeText(this, "✅ Promemoria attivato!", Toast.LENGTH_SHORT).show()
            } else {
                // Cancella l'allarme esistente
                val alarmManager = getSystemService(AlarmManager::class.java)
                val intent = Intent(this, WaterReminderReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(pendingIntent)
                Toast.makeText(this, "🔕 Promemoria disattivato!", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }
}