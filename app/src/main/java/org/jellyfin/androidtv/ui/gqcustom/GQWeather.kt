package org.jellyfin.androidtv.ui.gqcustom

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.ui.base.Text

@Composable
fun GQWeather(modifier: Modifier = Modifier) {
	var weather by remember { mutableStateOf<GQWeatherData?>(null) }
	LaunchedEffect(true) {
		launch(Dispatchers.IO) {
			weather = JNICommon.getCacheWeather()
		}
	}
	Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
		weather?.let {
			// Image(painter = painterResource(R.drawable.ic_apps), contentDescription = "", modifier = Modifier.size(40.dp))
			// Spacer(modifier = Modifier.width(4.dp))
			Text(text = "${it.weather}\n${it.temperature}℃", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
		}
	}
}
