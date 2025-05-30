package org.jellyfin.androidtv.ui.shared

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.integration.dream.model.DreamContent
import timber.log.Timber

@Composable
fun TopSlideAppList(
	show: Boolean,
	onDismissRequest: () -> Unit,
) {
	AnimatedVisibility(show) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.Black.copy(alpha = 0.6f))
				.pointerInput(true, block = { detectTapGestures() })
		) {

		}
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
	) {
		AnimatedVisibility(
			visible = show,
			enter = slideIn { IntOffset(0, -300) },
			exit = slideOutVertically(
				targetOffsetY = { -it },
				animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
			),
			modifier = Modifier.align(Alignment.TopCenter)
		) {
			val rowCount = 2
			val focusRequester = remember { FocusRequester() }
			var focusIndex by remember { mutableIntStateOf(0) }
			Surface(
				color = Color(0xFF171717),
				tonalElevation = 8.dp,
				shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
				modifier = Modifier
					.fillMaxWidth()
					.height(250.dp)
			) {
				LazyHorizontalGrid(
					rows = GridCells.Fixed(rowCount), modifier = Modifier
						.fillMaxSize()
						.padding(vertical = 8.dp)
						.focusable(true)
				) {
					items(20) { index ->
						Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp).let {
							if (index == 0 || index == 1) it.padding(start = 8.dp)
							else if (index == (20 - 1) || index == (20 - 2)) it.padding(end = 8.dp) else it
						}) {
							Card(
								modifier = Modifier
									.widthIn(min = 130.dp)
									.focusable(true)
									.clickable(true) {
										focusIndex = index
										focusRequester.requestFocus()
									}
									.let {
										if (focusIndex == index) it.focusRequester(focusRequester) else it
									}
									.onFocusChanged { focusState ->
										Timber.d("\"FOCUS: \": ${index}; ${focusState.isFocused}")
								},
								elevation = CardDefaults.cardElevation(
									defaultElevation = 12.dp,
									focusedElevation = 24.dp,
									pressedElevation = 24.dp
								),
								colors = CardDefaults.cardColors(containerColor = Color(0xFF424242))
							) {
								Spacer(modifier = Modifier.height(12.dp))
								Image(
									modifier = Modifier
										.align(Alignment.CenterHorizontally)
										.weight(1f),
									painter = painterResource(R.drawable.favorites),
									contentDescription = ""
								)
								Spacer(modifier = Modifier.height(4.dp))
								Text(
									text = "App",
									fontSize = 16.sp,
									modifier = Modifier.align(Alignment.CenterHorizontally),
									color = Color.White
								)
								Spacer(modifier = Modifier.height(12.dp))
							}
						}
					}
				}

			}
		}
	}

}
