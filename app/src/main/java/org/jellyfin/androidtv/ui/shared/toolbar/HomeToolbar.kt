package org.jellyfin.androidtv.ui.shared.toolbar

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.ui.base.Icon
import org.jellyfin.androidtv.ui.base.JellyfinTheme
import org.jellyfin.androidtv.ui.base.button.IconButton
import org.jellyfin.androidtv.ui.base.button.IconButtonDefaults
import org.jellyfin.androidtv.ui.gqcustom.TopSlideAppList

@Composable
fun HomeToolbar(
	openSearch: () -> Unit,
	openSettings: () -> Unit,
	switchUsers: () -> Unit,
	userImage: String? = null,
) {
	var showTopSlideAppList = remember { mutableStateOf(false) }
	val context = LocalContext.current

	Toolbar {
		ToolbarButtons {

			IconButton(onClick = openSearch) {
				Icon(
					painter = painterResource(R.drawable.ic_search),
					contentDescription = stringResource(R.string.lbl_search),
				)
			}

			IconButton(onClick = { showTopSlideAppList.value = true }) {
				Icon(
					painter = painterResource(R.drawable.ic_apps),
					contentDescription = "Apps",
				)
			}

			IconButton(onClick = openSettings) {
				Icon(
					painter = painterResource(R.drawable.ic_settings),
					contentDescription = stringResource(R.string.lbl_settings),
				)
			}

			val userImagePainter = rememberAsyncImagePainter(userImage)
			val userImageState by userImagePainter.state.collectAsState()
			val userImageVisible = userImageState is AsyncImagePainter.State.Success
			IconButton(
				onClick = switchUsers,
				contentPadding = if (userImageVisible) PaddingValues(3.dp) else IconButtonDefaults.ContentPadding,
			) {
				if (userImageVisible) {
					Image(
						painter = userImagePainter,
						contentDescription = stringResource(R.string.lbl_switch_user),
						contentScale = ContentScale.Crop,
						modifier = Modifier
							.aspectRatio(1f)
							.clip(IconButtonDefaults.Shape)
					)
				} else {
					Icon(
						painter = painterResource(R.drawable.ic_switch_users),
						contentDescription = stringResource(R.string.lbl_switch_user),
					)
				}
			}


			IconButton(onClick = { context.startActivity(Intent(android.provider.Settings.ACTION_SETTINGS)) }) {
				Icon(
					painter = painterResource(R.drawable.ic_system_settings),
					contentDescription = "System settings",
				)
			}
		}
	}

	JellyfinTheme {
		TopSlideAppList(showTopSlideAppList)
	}
}
