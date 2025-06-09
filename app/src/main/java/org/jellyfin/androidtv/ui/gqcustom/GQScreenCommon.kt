package org.jellyfin.androidtv.ui.gqcustom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object GQScreenCommon {
	// 从Assets加载图片，优化为低分辨率以适配低端设备
	suspend fun loadImagesFromAssets(context: Context): List<Bitmap> {
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
				try {
					assetManager.open("$fileName").use { inputStream ->
						// 优化大图加载：降低采样率
						val options = BitmapFactory.Options().apply {
							inSampleSize = calculateInSampleSize(this, 1920, 1080) // 适配电视分辨率
						}
						BitmapFactory.decodeStream(inputStream, null, options)
					}
				} catch (e: IOException) {
					null
				}
			}
		}
	}

	// 计算Bitmap采样率以优化内存和加载速度
	fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
		val (height: Int, width: Int) = options.run { outHeight to outWidth }
		var inSampleSize = 1
		if (height > reqHeight || width > reqWidth) {
			val halfHeight: Int = height / 2
			val halfWidth: Int = width / 2
			while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
				inSampleSize *= 2
			}
		}
		return inSampleSize
	}
}
