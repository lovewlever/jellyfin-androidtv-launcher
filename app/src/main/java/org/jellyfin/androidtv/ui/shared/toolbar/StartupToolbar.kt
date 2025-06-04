package org.jellyfin.androidtv.ui.shared.toolbar

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.ui.base.Icon
import org.jellyfin.androidtv.ui.base.JellyfinTheme
import org.jellyfin.androidtv.ui.base.button.IconButton
import org.jellyfin.androidtv.ui.shared.TopSlideAppList

@Composable
fun StartupToolbar(
	openHelp: () -> Unit,
	openSettings: () -> Unit,
) {

	var showTopSlideAppList = remember { mutableStateOf(false) }
	val context = LocalContext.current

	Toolbar {
		ToolbarButtons {
			IconButton(onClick = openHelp) {
				Icon(
					painter = painterResource(R.drawable.ic_help),
					contentDescription = stringResource(R.string.help),
				)
			}

			IconButton(onClick = openSettings) {
				Icon(
					painter = painterResource(R.drawable.ic_settings),
					contentDescription = stringResource(R.string.lbl_settings),
				)
			}

			IconButton(onClick = { showTopSlideAppList.value = true }) {
				Icon(
					painter = painterResource(R.drawable.ic_apps),
					contentDescription = "Apps",
				)
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
