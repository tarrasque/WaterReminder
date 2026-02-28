package com.tarrasqu3.waterreminder

import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val pickerStart = findViewById<NumberPicker>(R.id.pickerStart)
        val pickerEnd = findViewById<NumberPicker>(R.id.pickerEnd)
        val pickerInterval = findViewById<NumberPicker>(R.id.pickerInterval)
        val btnSave = findViewById<Button>(R.id.btnSave)

        pickerStart.minValue = 0; pickerStart.maxValue = 23; pickerStart.value = 8
        pickerEnd.minValue = 0; pickerEnd.maxValue = 23; pickerEnd.value = 22

        val intervals = arrayOf("20 min", "30 min", "45 min", "60 min", "90 min", "120 min")
        val intervalValues = intArrayOf(20, 30, 45, 60, 90, 120)
        pickerInterval.minValue = 0
        pickerInterval.maxValue = intervals.size - 1
        pickerInterval.displayedValues = intervals
        pickerInterval.value = 4

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        pickerStart.value = prefs.getInt("startHour", 8)
        pickerEnd.value = prefs.getInt("endHour", 22)

        // Recupera l'intervallo salvato e trova l'indice corretto
        val savedInterval = prefs.getInt("intervalMinutes", 60)
        val savedIndex = intervalValues.indexOfFirst { it == savedInterval }
        if (savedIndex >= 0) pickerInterval.value = savedIndex

        btnSave.setOnClickListener {
            prefs.edit()
                .putInt("startHour", pickerStart.value)
                .putInt("endHour", pickerEnd.value)
                .putInt("intervalMinutes", intervalValues[pickerInterval.value])
                .apply()

            WaterReminderReceiver.scheduleNextAlarm(this, intervalValues[pickerInterval.value])
            WaterReminderReceiver.sendTestNotification(this)
            Toast.makeText(this, "✅ Promemoria attivato!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}