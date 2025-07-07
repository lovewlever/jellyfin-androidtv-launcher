package org.jellyfin.androidtv.ui.gqcustom

class GQWeatherData {
	var city: String = ""
	var weather: String = ""
	var temperature: String = ""
	var temperatureFloat: String = ""
	var humidity: String = ""
	var humidityFloat: String = ""
	var winddirection: String = ""
	var windpower: String = ""
	var reporttime: String = ""

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
