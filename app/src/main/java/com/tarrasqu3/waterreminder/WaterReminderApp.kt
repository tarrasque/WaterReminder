package com.tarrasqu3.waterreminder

import android.app.Application
import com.dynatrace.android.agent.Dynatrace
import com.dynatrace.android.agent.conf.DynatraceConfigurationBuilder
import com.tarrasqu3.waterreminder.BuildConfig

class WaterReminderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Dynatrace.startup(
            this,
            DynatraceConfigurationBuilder(
                BuildConfig.DYNATRACE_APP_ID,
                BuildConfig.DYNATRACE_BEACON_URL
            ).buildConfiguration()
        )
    }
}