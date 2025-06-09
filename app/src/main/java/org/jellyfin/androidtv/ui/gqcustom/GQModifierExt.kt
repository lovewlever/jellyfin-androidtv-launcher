package org.jellyfin.androidtv.ui.gqcustom

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect

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
