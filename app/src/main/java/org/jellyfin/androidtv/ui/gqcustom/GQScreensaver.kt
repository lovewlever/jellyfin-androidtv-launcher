package org.jellyfin.androidtv.ui.gqcustom

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun GQScreensaver() {
	val context = LocalContext.current
	val coroutineScope = rememberCoroutineScope()

	val imageList = remember { mutableStateListOf<Bitmap>() }
	LaunchedEffect(true) {
		imageList.addAll(GQScreenCommon.loadImagesFromAssets(context))
	}

	var currentImageIndex by remember { mutableIntStateOf(0) }
	var currentImagePreIndex by remember { mutableIntStateOf(0) }
	val animRotation = remember { Animatable(0f) }

	LaunchedEffect(Unit) {
		while (true) {
			if (imageList.isNotEmpty()) {
				currentImageIndex = Random.nextInt(imageList.size)
				coroutineScope.launch {
					animRotation.animateTo(5F, animationSpec = tween(durationMillis = 1))
					animRotation.animateTo(
						0f,
						animationSpec = tween(durationMillis = 1500)
					)
				}
			}
			delay(6000)
			currentImagePreIndex = currentImageIndex
		}
	}


	if (imageList.isNotEmpty()) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier.background(color = Color.Black)
		) {
			val image = imageList[currentImageIndex].asImageBitmap()
			AnimatedContent(
				image,
				transitionSpec = {
					(fadeIn(animationSpec = tween(2200, delayMillis = 90)))
						.togetherWith(fadeOut(animationSpec = tween(2200)))
				}) {
				Image(
					bitmap = it,
					contentDescription = "",
					modifier = Modifier
						.fillMaxSize()
						.customBlurCompatible(20.dp),
					contentScale = ContentScale.Crop
				)
			}

			AnimatedContent(
				image,
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
				Image(
					bitmap = it,
					contentDescription = "",
					modifier = Modifier
						.fillMaxSize(),
					contentScale = ContentScale.Fit
				)
			}

		}

	}
}


