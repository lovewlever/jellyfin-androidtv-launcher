package org.jellyfin.androidtv.ui.playback.overlay

import android.content.Context
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColor
import androidx.fragment.app.FragmentActivity
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jellyfin.androidtv.data.repository.ItemRepository
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.preference.constant.AppTheme
import org.jellyfin.androidtv.ui.base.JellyfinTheme
import org.jellyfin.androidtv.ui.base.Text
import org.jellyfin.androidtv.ui.preference.screen.UserPreferencesScreen
import org.jellyfin.androidtv.util.ThemeViewModel
import org.jellyfin.androidtv.util.apiclient.getUrl
import org.jellyfin.androidtv.util.apiclient.itemImages
import org.jellyfin.androidtv.util.applyTheme
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.itemsApi
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.request.GetItemsRequest
import org.koin.compose.koinInject
import java.util.concurrent.atomic.AtomicReference
import androidx.core.graphics.toColorInt
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


/**
 * Provides the function of selecting episodes in the playback page
 */
internal fun createPlayingSelectionsView(
	context: Context,
	currentItem: BaseItemDto,
	onItemClicked: (Int, List<BaseItemDto>) -> Unit?,
): ComposeView {
	var viewHasFocused by mutableStateOf(false)
	return ComposeView(context).apply {
		isFocusable = true
		setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
		this.onFocusChangeListener = OnFocusChangeListener { v, hasFocus -> viewHasFocused = hasFocus }

		setContent {
			JellyfinTheme {
				PlayingSelectionsView(
					modifier = Modifier.padding(top = 8.dp),
					viewHasFocused = viewHasFocused,
					currentItem = currentItem,
					onItemClicked = onItemClicked
				)
			}
		}
	}
}


@Composable
private fun PlayingSelectionsView(
	modifier: Modifier = Modifier,
	viewHasFocused: Boolean,
	currentItem: BaseItemDto,
	onItemClicked: (Int, List<BaseItemDto>) -> Unit?,
) {
	val itemsToPlayStateList = remember { mutableStateListOf<BaseItemDto>() }
	var first by remember { mutableStateOf(true) }
	val api = koinInject<ApiClient>()
	val lazyRowState = rememberLazyListState()
	val context = LocalContext.current

	val composeColor = remember {
		val attrs = context.obtainStyledAttributes(UserPreferences.appTheme.defaultValue.nameRes, intArrayOf(android.R.attr.colorAccent))
		val color = attrs.getColor(0, "#FFFFFF".toColorInt())
		val composeColor = Color(color)
		attrs.recycle()
		composeColor
	}

	LaunchedEffect(currentItem) {
		val byName = GetItemsRequest(
			fields = ItemRepository.itemFields,
			parentId = currentItem.seasonId,
		)
		val response = withContext(Dispatchers.IO) {
			api.itemsApi.getItems(byName).content
		}
		itemsToPlayStateList.clear()
		itemsToPlayStateList.addAll(response.items)

		itemsToPlayStateList.indexOfFirst { it.id == currentItem.id }.takeIf { it > 0 }?.let {
			lazyRowState.scrollToItem(it)
		}
	}

	LazyRow(
		modifier = modifier
			.focusGroup()
			.focusable(),
		state = lazyRowState
	) {
		itemsIndexed(itemsToPlayStateList, key = { index, item -> item.id }, contentType = { index, item -> item.id }) { index, item ->
			var focused by remember { mutableStateOf(false) }
			val focusRequester = remember { FocusRequester() }
			val animScale = animateFloatAsState(if (focused) 1.2f else 1f)

			Card(
				modifier = Modifier
					.graphicsLayer {
						scaleX = animScale.value
						scaleY = animScale.value
					}
					.padding(start = if (index == 0) 40.dp else 0.dp, end = if (index == itemsToPlayStateList.lastIndex) 40.dp else 0.dp)
					.padding(horizontal = 8.dp)
					.onFocusChanged {
						focused = it.isFocused
					}
					.focusRequester(focusRequester)
					.focusable()
					.onKeyEvent { keyEvent ->
						if (keyEvent.key == Key.Back || keyEvent.key == Key.Escape) {
							true
						}
						if ((keyEvent.key == Key.Enter || keyEvent.key == Key.DirectionCenter) && keyEvent.type == KeyEventType.KeyUp) {
							onItemClicked(index, itemsToPlayStateList.toList())
							true
						} else {
							false
						}
					},
				shape = RoundedCornerShape(8.dp),
				colors = CardDefaults.cardColors(containerColor = Color.Transparent)
			) {
				val spi by remember {
					mutableStateOf(
						item.itemImages.takeIf { it.isNotEmpty() }?.get(org.jellyfin.sdk.model.api.ImageType.PRIMARY)?.getUrl(api)
					)
				}
				AsyncImage(
					model = ImageRequest.Builder(context)
						.diskCachePolicy(CachePolicy.ENABLED)
						.data(spi)
						.build(),
					contentDescription = "",
					modifier = Modifier
						.size(140.dp, 80.dp)
						.clip(RoundedCornerShape(8.dp)),
					contentScale = ContentScale.Crop
				)
				Spacer(modifier = Modifier.height(2.dp))

				val itemName = item.name
				val s = item.parentIndexNumber?.toString()?.let { "S${it.padStart(2, '0')}" }
				val e = item.indexNumber?.toString()?.let { "E${it.padStart(2, '0')}" }
				val se = if (s != null && e != null) "$s$e: " else if (s != null) "$s: " else if (e != null) "$e: " else ""
				Text(
					text = "${se}${itemName}",
					color =  if (item.id == currentItem.id) composeColor else Color.White,
					modifier = Modifier
						.widthIn(max = 140.dp)
						.height(42.dp),
					overflow = TextOverflow.Ellipsis,
					maxLines = 2,
					fontSize = 14.sp
				)
			}

			LaunchedEffect(viewHasFocused) {
				if (viewHasFocused && index == lazyRowState.firstVisibleItemIndex && first) {
					focusRequester.requestFocus()
				}
			}
		}
	}
}
