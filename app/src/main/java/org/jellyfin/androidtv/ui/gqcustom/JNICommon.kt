package org.jellyfin.androidtv.ui.gqcustom

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.TimeUnit

object JNICommon {

	init {
		System.loadLibrary("JellyfinCPPTools")
	}

	external fun test()

	external fun init(cacheDirPath: String, debug: Boolean): Int

	external fun setCurrentServerUrl(serverUrl: String): Int

	external fun queryScreensaverImageUrlList(hostName: String, hostPort: Int): String

	external fun downloadScreensaverRemoteImageListToLocalPath(): Int

	external fun getScreensaverRemoteImageList(): List<String>

	external fun queryWeather(): GQWeatherData?

	/////////////////////////////////////////////////////////////////////////////////////

	private const val UniqueWorkName = "ScreensaverImageUrlList"
	private var hostName: String = ""
	private var hostPort: Int = 0
	private val ScreensaverImageUrlListUUID by lazy { UUID.fromString("7d7b6ec7-2616-44a1-9f7c-fa14e7323da2") }

	fun startWorkManager(context: Context, hostName: String, hostPort: Int) {
		this.hostName = hostName
		this.hostPort = hostPort
		val workManager = WorkManager.getInstance(context)
		val workInfo = workManager.getWorkInfoById(ScreensaverImageUrlListUUID)
		if (workInfo.get() != null) {
			Timber.d("CustomWorker 已存在相同Worker 将更新")
		}
		Timber.d("CustomWorker enqueueUniquePeriodicWork")
		val perReqWorkerReq = PeriodicWorkRequestBuilder<CustomWorker>(15, TimeUnit.MINUTES)
			.setId(ScreensaverImageUrlListUUID)
			.setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
			.build()
		workManager.enqueueUniquePeriodicWork(UniqueWorkName, ExistingPeriodicWorkPolicy.UPDATE ,perReqWorkerReq)
	}

	class CustomWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
		override suspend fun doWork(): Result {
			Timber.d("CustomWorker startWork")
			val jsonStr = queryScreensaverImageUrlList(hostName, hostPort)
			// val download = downloadScreensaverRemoteImageListToLocalPath()
			Timber.d("CustomWorker jsonStr: $jsonStr; DownloadToLocal: nullptr")
			return if(jsonStr.isNotEmpty()) {
				Result.success()
			} else {
				Result.retry()
			}
		}
	}
}
