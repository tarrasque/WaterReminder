package com.tarrasqu3.waterreminder

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val start = prefs.getInt("startHour", -1)
        val end = prefs.getInt("endHour", -1)
        val interval = prefs.getInt("intervalMinutes", -1)

        val txt = findViewById<TextView>(R.id.txtCurrentSettings)
        if (start == -1) {
            txt.text = "Nessuna impostazione salvata ancora."
        } else {
            txt.text = "🕐 Inizio notifiche: ore $start:00\n🕙 Fine notifiche: ore $end:00\n⏱️ Ogni: $interval minuti"
        }
    }
}