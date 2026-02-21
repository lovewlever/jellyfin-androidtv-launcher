package org.jellyfin.androidtv.ui.gqcustom

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.customBlur(blur: Dp = 6.dp): Modifier {
	return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
		this.blur(blur)
	} else {
		this
	}
}
