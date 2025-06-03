package org.jellyfin.androidtv.viewmodel.model

import android.graphics.drawable.Drawable
import coil3.Bitmap
import java.io.Serializable


class AppInfo(
	val appName: String,
	val pkgName: String,
	val icon: Drawable? = null
): Serializable
