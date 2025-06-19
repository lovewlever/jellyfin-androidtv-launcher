package org.jellyfin.androidtv.ui.gqcustom

import android.content.Context
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

	external fun queryScreensaverImageUrlList(hostName: String, hostPort: Int): String

	/////////////////////////////////////////////////////////////////////////////////////

	private const val UniqueWorkName = "ScreensaverImageUrlList"
	private var hostName: String = ""
	private var hostPort: Int = 0
	private val ScreensaverImageUrlListUUID by lazy { UUID.randomUUID() }

	fun startWorkManager(context: Context, hostName: String, hostPort: Int) {
		this.hostName = hostName
		this.hostPort = hostPort
		val workManager = WorkManager.getInstance(context)
		val workInfo = workManager.getWorkInfoById(ScreensaverImageUrlListUUID)
		if (workInfo.get() != null) {
			Timber.d("CustomWorker 已存在相同Worker 返回；")
			return
		}
		Timber.d("CustomWorker enqueueUniquePeriodicWork")
		val perReqWorkerReq = PeriodicWorkRequestBuilder<CustomWorker>(10, TimeUnit.MINUTES)
			.setId(ScreensaverImageUrlListUUID)
			.build()
		workManager.enqueueUniquePeriodicWork(UniqueWorkName, ExistingPeriodicWorkPolicy.KEEP ,perReqWorkerReq)
	}

	class CustomWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
		private val json = Json {
			ignoreUnknownKeys = true
			isLenient = true
		}
		override suspend fun doWork(): Result {
			Timber.d("CustomWorker startWork")
			val jsonStr = queryScreensaverImageUrlList(hostName, hostPort)

			Timber.d("CustomWorker jsonStr: $jsonStr")
			return Result.success()
		}

		/*override fun startWork(): ListenableFuture<Result?> {
			return ListenableFutureTask.create {

			}.apply { run() }
		}*/

	}

}
