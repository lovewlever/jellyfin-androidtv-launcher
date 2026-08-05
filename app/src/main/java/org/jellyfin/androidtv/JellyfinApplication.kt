package org.jellyfin.androidtv

import android.app.Application
import android.content.Context
import com.tencent.bugly.crashreport.CrashReport
import org.jellyfin.androidtv.telemetry.TelemetryService

class JellyfinApplication : Application() {
	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)
		CrashReport.initCrashReport(this, "2c6c1d711f", true)
		TelemetryService.init(this)
	}
}
