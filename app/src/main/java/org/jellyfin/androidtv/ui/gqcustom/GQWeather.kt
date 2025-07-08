package org.jellyfin.androidtv.ui.gqcustom

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jellyfin.androidtv.ui.base.Text

@Composable
fun GQWeather(modifier: Modifier = Modifier) {

	val weather by JNICommon.weatherFlow.collectAsState()

	weather?.let {
		Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
			Spacer(Modifier.width(24.dp))

			// Image(painter = painterResource(R.drawable.ic_apps), contentDescription = "", modifier = Modifier.size(40.dp))
			// Spacer(modifier = Modifier.width(4.dp))
			Text(text = "${it.weather}\n${it.temperature}℃", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
		}
	}
}
