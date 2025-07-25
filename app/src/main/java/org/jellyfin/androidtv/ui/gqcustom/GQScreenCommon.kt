package org.jellyfin.androidtv.ui.gqcustom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object GQScreenCommon {
	suspend fun loadImagesFromAssets(context: Context): List<String> {
		return withContext(Dispatchers.IO) {
			val assetManager = context.assets
			val imageFiles = try {
				assetManager.list("")?.filter {
					it.endsWith(".png", true) ||
						it.endsWith(".jpg", true) ||
						it.endsWith(".jpeg", true) ||
						it.endsWith(".webp", true)
				} ?: emptyList()
			} catch (e: IOException) {
				emptyList()
			}

			imageFiles.mapNotNull { fileName ->
				fileName
			}
		}
	}

}
