package org.jellyfin.androidtv.ui.gqcustom

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jellyfin.androidtv.viewmodel.model.AppInfo
import timber.log.Timber

class AppListViewModel(application: Application) : AndroidViewModel(application) {

	val appList = mutableStateListOf<AppInfo>()

	fun queryAllApps() {
		viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val context = getApplication<Application>()
                // 查询所有带图标的app
                val apps = getLaunchableApps()
                Timber.Forest.d("queryAllApps: ${apps.size}; ${appList.size + 1}")
                if (apps.size != appList.size + 1) {
                    appList.clear()
                    for (app in apps) {
                        val packageName = app.activityInfo.packageName
                        if (packageName == context.packageName) continue
                        val appName = app.loadLabel(context.packageManager).toString()
                        val icon = app.loadIcon(context.packageManager)
                        appList.add(AppInfo(appName, packageName, icon))
                        Timber.Forest.d("AppInfo - Name: $appName, Package: $packageName")
                    }
                }
            }
		}
	}

	private fun getLaunchableApps(): List<ResolveInfo> {
		val packageManager = getApplication<Application>().packageManager

		val intent = Intent(Intent.ACTION_MAIN).apply {
			addCategory(Intent.CATEGORY_LAUNCHER)
		}
		return packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
	}
}
