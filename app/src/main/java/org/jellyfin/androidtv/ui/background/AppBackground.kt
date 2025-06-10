package org.jellyfin.androidtv.ui.background

import android.graphics.drawable.ColorDrawable
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.data.service.BackgroundService
import org.jellyfin.androidtv.ui.gqcustom.customBlur
import org.koin.compose.koinInject

@Composable
private fun AppThemeBackground() {
	val context = LocalContext.current
	var themeBackground by remember {
		mutableStateOf<ImageBitmap?>(null)
	}

	LaunchedEffect(context.theme) {
		withContext(Dispatchers.IO) {
			val attributes = context.theme.obtainStyledAttributes(intArrayOf(R.attr.defaultBackground))
			val drawable = attributes.getDrawable(0)
			attributes.recycle()
			if (drawable is ColorDrawable) {
				themeBackground = drawable.toBitmap(1, 1).asImageBitmap()
			} else {
				themeBackground = drawable?.toBitmap()?.asImageBitmap()
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
				AppThemeBackground()
			}
		}
	}
}
