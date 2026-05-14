package org.jellyfin.androidtv.ui.gqcustom

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class GQWeatherData: Serializable {
	@Keep var city: String = ""
	@Keep var weather: String = ""
	@Keep var temperature: String = ""
	@Keep var temperatureFloat: String = ""
	@Keep var humidity: String = ""
	@Keep var humidityFloat: String = ""
	@Keep var winddirection: String = ""
	@Keep var windpower: String = ""
	@Keep var reporttime: String = ""

	@Keep
	override fun toString(): String {
		return "city: $city\n" +
				"weather: $weather\n" +
				"temperature: $temperature\n" +
				"temperatureFloat: $temperatureFloat\n" +
				"humidity: $humidity\n" +
				"humidityFloat: $humidityFloat\n" +
				"winddirection: $winddirection\n" +
				"windpower: $windpower\n" +
				"reporttime: $reporttime\n"
	}
}
