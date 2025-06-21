package org.jellyfin.androidtv.ui.gqcustom

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.transformations
import com.commit451.coiltransformations.BlurTransformation
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.integration.dream.composable.DreamHost
import timber.log.Timber
import java.util.ArrayList
import kotlin.random.Random

@Composable
fun GQScreensaver(serverScreensaverHostUrlPrefix: String) {
	val context = LocalContext.current
	val coroutineScope = rememberCoroutineScope()

	val hazeState = rememberHazeState()
	var currentImageIndex by remember { mutableIntStateOf(0) }
	val animRotation = remember { Animatable(0f) }
	var imageSource by remember { mutableStateOf("") }

	val funcJNIGetScreensaverRemoteImageList = remember {
		{
			JNICommon.getScreensaverRemoteImageList()
		}
	}

	val funcAnimRotationStart = remember {
		{
			coroutineScope.launch {
				animRotation.animateTo(5F, animationSpec = tween(durationMillis = 1))
				animRotation.animateTo(
					0f,
					animationSpec = tween(durationMillis = 1500)
				)
			}
		}
	}

	DisposableEffect(true) {
		coroutineScope.launch {
			try {
				while (true) {
					Timber.d("currentImageIndex: $currentImageIndex")
					val remoteImageUrls = funcJNIGetScreensaverRemoteImageList()
					if (remoteImageUrls.isNotEmpty()) {
						currentImageIndex = Random.nextInt(remoteImageUrls.size)
						imageSource = "${serverScreensaverHostUrlPrefix}/${remoteImageUrls[currentImageIndex]}"
						funcAnimRotationStart()
					} else {
						imageSource = ""
					}
					delay(6000)
				}
			} catch (e: Exception) {
				Timber.e(e)
			}
		}

		onDispose {
			coroutineScope.cancel()
		}
	}


	if (imageSource.isNotEmpty()) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier.background(color = Color.Black)
		) {
			AnimatedContent(
				imageSource,
				transitionSpec = {
					(fadeIn(animationSpec = tween(2200, delayMillis = 90)))
						.togetherWith(fadeOut(animationSpec = tween(2200)))
				}) {
				key(it) {
					SubcomposeAsyncImage(
						model = ImageRequest.Builder(context)
							.data(it)
							.transformations(BlurTransformation(context, 10f, 2f))
							.diskCachePolicy(CachePolicy.ENABLED)
							.diskCacheKey(it)
							.size(1280, 720)
							.build(),
						contentDescription = "",
						modifier = Modifier
							.fillMaxSize()
							.hazeSource(state = hazeState),
						contentScale = ContentScale.Crop
					)
				}
			}

//			key("GQScreensaverBlur") {
//				Box(modifier = Modifier
//					.fillMaxSize()
//					.background(color = Color.Transparent)
//					.customBlurCompatible(hazeState = hazeState, blur = 20.dp))
//			}

			AnimatedContent(
				imageSource,
				transitionSpec = {
					(fadeIn(animationSpec = tween(2000, delayMillis = 90)) +
						scaleIn(
							initialScale = 1.2f,
							animationSpec = tween(2000, delayMillis = 90)
						))
						.togetherWith(
							fadeOut(animationSpec = tween(1500)) +
								scaleOut(
									targetScale = 0.9f,
									animationSpec = tween(1500)
								)
						)
				}) {
				key(it) {
					SubcomposeAsyncImage(
						model = ImageRequest.Builder(context)
							.data(it)
							.diskCachePolicy(CachePolicy.ENABLED)
							.diskCacheKey(it)
							.size(1280, 720)
							.build(),
						contentDescription = "",
						modifier = Modifier
							.fillMaxSize(),
						contentScale = ContentScale.Fit
					)
				}
			}
		}
	} else {
		DreamHost()
	}
}


