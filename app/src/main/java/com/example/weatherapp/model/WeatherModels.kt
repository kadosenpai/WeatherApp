package com.example.weatherapp.model

/** Kiểu bầu trời -> quyết định gradient nền và hiệu ứng. */
enum class SkyKind { CLEAR_DAY, CLOUDY_DAY, RAIN_DAY, STORM, WINDY, DAWN, DUSK, CLEAR_NIGHT, CLOUDY_NIGHT }

/** Icon thời tiết nhỏ (dùng cho dự báo giờ / ngày). */
enum class IconKind { SUNNY, PARTLY_CLOUDY, CLOUDY, RAIN, STORM, WIND, NIGHT }

data class HourlyForecast(
    val label: String,
    val icon: IconKind,
    val temp: Int,
)

data class DailyForecast(
    val day: String,
    val icon: IconKind,
    val precipChance: Int?,
    val low: Int,
    val high: Int,
    val currentTemp: Int? = null,
)

/**
 * Model dữ liệu của riêng app. UI chỉ biết model này,
 * nên sau này đổi nguồn (Open-Meteo, WeatherKit...) chỉ cần viết Repository mới.
 */
data class CityWeather(
    val name: String,
    val subtitle: String,
    val isCurrentLocation: Boolean,
    val sky: SkyKind,
    val condition: String,
    val temp: Int,
    val high: Int,
    val low: Int,
    val summary: String,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>,
    val feelsLike: Int,
    val feelsNote: String,
    val avgDelta: Int,
    val avgNote: String,
    val windSpeed: Int,
    val windGust: Int,
    val windDirDeg: Int,
    val windDirName: String,
    val uvIndex: Int,
    val uvLabel: String,
    val uvNote: String,
    val sunrise: String,
    val sunset: String,
    val sunProgress: Float,
    val rainTodayMm: Int,
    val rainNote: String,
    val visibilityKm: Int,
    val visibilityNote: String,
    val moonPhaseName: String,
    val moonIllumination: Int,
    val moonrise: String,
    val daysToFullMoon: Int,
    val humidity: Int,
    val dewPoint: Int,
    val pressureHpa: Int,
)
