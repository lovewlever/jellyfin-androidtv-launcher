package org.jellyfin.androidtv.ui.background

import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.auth.repository.ServerRepository
import org.jellyfin.androidtv.data.service.BackgroundService
import org.koin.compose.koinInject

@Composable
private fun AppThemeBackground(splashScreenBackground: ImageBitmap? = null) {
	val context = LocalContext.current
	var themeBackground by remember {
		mutableStateOf<ImageBitmap?>(null)
	}

	LaunchedEffect(context.theme, key2 = splashScreenBackground) {
		if (splashScreenBackground != null) {
			themeBackground = splashScreenBackground
			return@LaunchedEffect
		}
		withContext(Dispatchers.IO) {
			val attributes = context.theme.obtainStyledAttributes(intArrayOf(R.attr.defaultBackground))
			val drawable = attributes.getDrawable(0)
			attributes.recycle()
			if (drawable is ColorDrawable) {
				themeBackground = drawable.toBitmap(1, 1).asImageBitmap()
			} else {
				val assetManager = context.assets
				assetManager.open("default.webp").use { inputStream ->
					val options = BitmapFactory.Options().apply {
						inSampleSize = calculateInSampleSize(this, 1920, 1080)
					}
					themeBackground = BitmapFactory.decodeStream(inputStream, null, options)?.asImageBitmap()
				}
			}
		}
	}

	AnimatedContent(
		targetState = themeBackground,
		transitionSpec = {
			val duration = (BackgroundService.TRANSITION_DURATION.inWholeMilliseconds / 2).toInt()
			fadeIn(tween(durationMillis = duration)) togetherWith fadeOut(snap(delayMillis = duration))
		},) {
		if (it != null) {
			Image(
				bitmap = it,
				contentDescription = null,
				alignment = Alignment.Center,
				contentScale = ContentScale.Crop,
				modifier = Modifier.fillMaxSize().customBlur()
			)
		} else {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.background(Color.Black)
			)
		}
	}


}

@Composable
fun AppBackground(
	backgroundService: BackgroundService = koinInject<BackgroundService>(),
	serverAddress: String? = null
) {
	val currentBackground by backgroundService.currentBackground.collectAsState()
	val blurBackground by backgroundService.blurBackground.collectAsState()
	val enabled by backgroundService.enabled.collectAsState()

	if (enabled) {
		AnimatedContent(
			targetState = currentBackground,
			transitionSpec = {
				val duration = (BackgroundService.TRANSITION_DURATION.inWholeMilliseconds / 2).toInt()
				fadeIn(tween(durationMillis = duration)) togetherWith fadeOut(snap(delayMillis = duration))
			},
			label = "BackgroundTransition",
		) { background ->
			if (background != null) {
				Image(
					bitmap = background,
					contentDescription = null,
					alignment = Alignment.Center,
					contentScale = ContentScale.Crop,
					colorFilter = ColorFilter.tint(colorResource(R.color.background_filter), BlendMode.SrcAtop),
					modifier = Modifier
						.fillMaxSize()
						.then(if (blurBackground) Modifier.customBlur() else Modifier)
				)
			} else {
				val splashScreenBackground by backgroundService.splashScreenBackground.collectAsState()
				LaunchedEffect(serverAddress) {
					if (serverAddress != null) {
						backgroundService.setSplashScreenBitmap(serverAddress)
					}
				}
				AppThemeBackground(splashScreenBackground)
			}
		}
	}
}

@Composable
fun Modifier.customBlur(blur: Dp = 10.dp): Modifier {
	return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
		this.blur(blur)
	} else {
		this.hazeEffect(style = HazeStyle.Unspecified, block = {
			this.blurEnabled = true
			this.blurRadius = blur + 10.dp
			this.backgroundColor = Color.White
			this.noiseFactor = 0F
		})
	}
}

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
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
