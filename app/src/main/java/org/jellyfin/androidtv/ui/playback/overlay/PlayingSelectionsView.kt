package org.jellyfin.androidtv.ui.playback.overlay

import android.content.Context
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jellyfin.androidtv.constant.ImageType
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.util.apiclient.getUrl
import org.jellyfin.androidtv.util.apiclient.images
import org.jellyfin.androidtv.util.apiclient.itemBackdropImages
import org.jellyfin.androidtv.util.apiclient.itemImages
import org.jellyfin.androidtv.util.apiclient.parentBackdropImages
import org.jellyfin.androidtv.util.apiclient.seriesPrimaryImage
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.model.api.BaseItemDto
import org.koin.compose.koinInject
import timber.log.Timber


fun createPlayingSelectionsView(context: Context, itemsToPlay: List<BaseItemDto>): ComposeView {
	var viewHasFocused by mutableStateOf(false)
	return ComposeView(context).apply {
		id = View.generateViewId()
		isFocusable = true
		// isFocusableInTouchMode = true
		setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
		this.onFocusChangeListener = object : OnFocusChangeListener {
			override fun onFocusChange(v: View?, hasFocus: Boolean) {
				Timber.d("PlayingSelectionsView-HasFocus: $hasFocus")
				viewHasFocused = hasFocus
			}
		}

		setContent {
			val itemsToPlayStateList = remember { mutableStateListOf<BaseItemDto>() }
			LaunchedEffect(true) {
				itemsToPlayStateList.clear()
				itemsToPlayStateList.addAll(itemsToPlay)
			}
			PlayingSelectionsView(
				modifier = Modifier.padding(top = 8.dp),
				viewHasFocused = viewHasFocused,
				itemsToPlay = itemsToPlayStateList
			)
		}
	}
}


@Composable
fun PlayingSelectionsView(modifier: Modifier = Modifier, viewHasFocused: Boolean, itemsToPlay: SnapshotStateList<BaseItemDto>) {
	var first by remember { mutableStateOf(true) }
	val context = LocalContext.current
	val api = koinInject<ApiClient>()
	LazyRow(
		modifier = modifier
            .focusGroup()
            .focusable()
	) {
		itemsIndexed(itemsToPlay) { index, item ->

			var focused by remember { mutableStateOf(false) }
			val focusRequester = remember { FocusRequester() }
			val animScale = animateFloatAsState(if (focused) 1.2f else 1f)

			Card(
				modifier = Modifier
                    .graphicsLayer {
                        scaleX = animScale.value
                        scaleY = animScale.value
                    }
                    .padding(horizontal = 8.dp)
                    .onFocusChanged {
                        Timber.d("Card-HasFocus: ${it.isFocused}")
                        focused = it.isFocused
                    }
                    .focusRequester(focusRequester)
                    .focusable(),
				shape = RoundedCornerShape(8.dp),
				colors = CardDefaults.cardColors(containerColor = Color.Transparent)
			) {

				val spi = item.itemImages.takeIf { it.isNotEmpty() }?.get(org.jellyfin.sdk.model.api.ImageType.PRIMARY)?.getUrl(api)
				//val imageS = (item.itemBackdropImages + item.parentBackdropImages).map { it.getUrl(api) }.toSet().toList()
				//val url = imageS.takeIf { it.isNotEmpty() }?.get(0)
				AsyncImage(
					model = spi,
					contentDescription = "",
					modifier = Modifier
                        .size(140.dp, 80.dp)
                        .clip(RoundedCornerShape(8.dp)),
					contentScale = ContentScale.Crop
				)
				// Spacer(modifier = Modifier.height(4.dp))
				val name = item.mediaSources?.takeIf { it.isNotEmpty() }?.get(0)?.name ?: item.name
				val seriesName = item.seriesName ?: "" //
				val seasonName = item.seasonName ?: "" //
				Text(text = "${name}", color = Color.White, modifier = Modifier.widthIn(max = 140.dp), overflow = TextOverflow.Ellipsis)
			}

			LaunchedEffect(viewHasFocused) {
				if (viewHasFocused && index == 0 && first) {
					val bool = focusRequester.requestFocus()
					Timber.d("Card-HasFocus-Request: $bool")
				}
			}
		}
	}
}
