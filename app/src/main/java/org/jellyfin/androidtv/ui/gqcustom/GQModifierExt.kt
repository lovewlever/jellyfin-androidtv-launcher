package org.jellyfin.androidtv.ui.gqcustom

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@Composable
fun Modifier.customBlur(blur: Dp = 10.dp): Modifier {
	return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
		this.blur(blur)
	} else {
		this
	}
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun Modifier.customBlurCompatible(hazeState: HazeState? = null, blur: Dp = 10.dp): Modifier {
	return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
		this.blur(blur)
	} else {
		this.hazeEffect(
			state = hazeState,
			style = HazeMaterials.ultraThin(containerColor = Color.DarkGray), block = {
			this.blurEnabled = true
			this.blurRadius = blur
			// this.backgroundColor = Color.White
			this.noiseFactor = 0F
		})
	}
}
