package com.example.weatherapp.data

import com.example.weatherapp.model.CityWeather
import com.example.weatherapp.model.DailyForecast
import com.example.weatherapp.model.HourlyForecast
import com.example.weatherapp.model.IconKind
import com.example.weatherapp.model.SkyKind

/** Dữ liệu giả (lấy theo video tham chiếu) để dựng UI trước khi gắn Open-Meteo. */
object FakeWeatherData {

    private val dayNames = listOf("Hôm nay", "Th 3", "Th 4", "Th 5", "Th 6", "Th 7", "CN", "Th 2", "Th 3", "Th 4")
    private val baseLow = listOf(26, 25, 24, 24, 25, 25, 25, 25, 24, 25)
    private val baseHigh = listOf(33, 33, 34, 34, 33, 32, 33, 34, 32, 33)
    private val hourLabels = listOf(
        "Bây giờ", "11 giờ", "12 giờ", "13 giờ", "14 giờ", "15 giờ",
        "16 giờ", "17 giờ", "18 giờ", "19 giờ", "20 giờ", "21 giờ",
    )
    private val hourDelta = listOf(0, 1, 2, 3, 3, 2, 1, 0, -2, -3, -3, -4)

    private fun hourly(temp: Int, icon: IconKind) =
        hourLabels.indices.map { HourlyForecast(hourLabels[it], icon, temp + hourDelta[it]) }

    private fun daily(
        dLow: Int,
        dHigh: Int,
        current: Int,
        icons: List<IconKind>,
        rain: List<Int?>,
    ) = dayNames.indices.map { i ->
        DailyForecast(
            day = dayNames[i],
            icon = icons[i],
            precipChance = rain[i],
            low = baseLow[i] + dLow,
            high = baseHigh[i] + dHigh,
            currentTemp = if (i == 0) current else null,
        )
    }

    private val hcmIcons = listOf(
        IconKind.CLOUDY, IconKind.RAIN, IconKind.PARTLY_CLOUDY, IconKind.PARTLY_CLOUDY, IconKind.CLOUDY,
        IconKind.RAIN, IconKind.CLOUDY, IconKind.PARTLY_CLOUDY, IconKind.RAIN, IconKind.CLOUDY,
    )
    private val hcmRain = listOf(null, 55, null, null, null, 40, null, null, 45, null)

    private val sunnyIcons = listOf(
        IconKind.SUNNY, IconKind.PARTLY_CLOUDY, IconKind.SUNNY, IconKind.SUNNY, IconKind.PARTLY_CLOUDY,
        IconKind.RAIN, IconKind.PARTLY_CLOUDY, IconKind.SUNNY, IconKind.PARTLY_CLOUDY, IconKind.CLOUDY,
    )
    private val sunnyRain = listOf(null, null, null, null, null, 35, null, null, null, null)

    private val rainyIcons = listOf(
        IconKind.RAIN, IconKind.RAIN, IconKind.CLOUDY, IconKind.RAIN, IconKind.RAIN,
        IconKind.CLOUDY, IconKind.RAIN, IconKind.RAIN, IconKind.CLOUDY, IconKind.RAIN,
    )
    private val rainyRain = listOf(70, 60, null, 55, 65, null, 50, 60, null, 55)

    val cities: List<CityWeather> = listOf(
        CityWeather(
            name = "Thành Phố Hồ Chí Minh",
            subtitle = "Vị trí của tôi",
            isCurrentLocation = true,
            sky = SkyKind.CLOUDY_DAY,
            condition = "Trời nhiều mây",
            temp = 30, high = 33, low = 26,
            summary = "Có mây sẽ tiếp tục cả ngày. Gió giật lên đến 17 km/h.",
            hourly = hourly(30, IconKind.CLOUDY),
            daily = daily(0, 0, 30, hcmIcons, hcmRain),
            feelsLike = 32, feelsNote = "Cảm nhận ấm hơn nhiệt độ thực tế.",
            avgDelta = 3, avgNote = "trên cao nhất hàng ngày trung bình",
            windSpeed = 9, windGust = 17, windDirDeg = 26, windDirName = "BĐB",
            uvIndex = 5, uvLabel = "Trung bình", uvNote = "Tránh nắng đến 15:00.",
            sunrise = "05:49", sunset = "17:26", sunProgress = 0.36f,
            rainTodayMm = 1, rainNote = "Dự báo là 2 mm vào ngày mai.",
            visibilityKm = 21, visibilityNote = "Tầm nhìn hoàn toàn rõ.",
            moonPhaseName = "LƯỠI LIỀM ĐẦU THÁNG", moonIllumination = 37, moonrise = "11:15", daysToFullMoon = 8,
            humidity = 75, dewPoint = 25,
            pressureHpa = 1013,
        ),
        CityWeather(
            name = "Q. Tân Bình",
            subtitle = "10:03",
            isCurrentLocation = false,
            sky = SkyKind.CLOUDY_DAY,
            condition = "Trời nhiều mây",
            temp = 30, high = 33, low = 26,
            summary = "Có mây sẽ tiếp tục cả ngày. Gió giật lên đến 18 km/h.",
            hourly = hourly(30, IconKind.CLOUDY),
            daily = daily(0, 0, 30, hcmIcons, hcmRain),
            feelsLike = 33, feelsNote = "Cảm nhận ấm hơn nhiệt độ thực tế.",
            avgDelta = 3, avgNote = "trên cao nhất hàng ngày trung bình",
            windSpeed = 10, windGust = 18, windDirDeg = 40, windDirName = "ĐBĐ",
            uvIndex = 5, uvLabel = "Trung bình", uvNote = "Tránh nắng đến 15:00.",
            sunrise = "05:49", sunset = "17:26", sunProgress = 0.36f,
            rainTodayMm = 1, rainNote = "Dự báo là 2 mm vào ngày mai.",
            visibilityKm = 20, visibilityNote = "Tầm nhìn hoàn toàn rõ.",
            moonPhaseName = "LƯỠI LIỀM ĐẦU THÁNG", moonIllumination = 37, moonrise = "11:15", daysToFullMoon = 8,
            humidity = 75, dewPoint = 25,
            pressureHpa = 1013,
        ),
        CityWeather(
            name = "TX. Gò Công",
            subtitle = "10:03",
            isCurrentLocation = false,
            sky = SkyKind.CLEAR_DAY,
            condition = "Nhiều nắng",
            temp = 31, high = 32, low = 25,
            summary = "Trời nắng cả ngày. Gió giật lên đến 20 km/h.",
            hourly = hourly(31, IconKind.SUNNY),
            daily = daily(-1, -1, 31, sunnyIcons, sunnyRain),
            feelsLike = 34, feelsNote = "Cảm nhận ấm hơn nhiệt độ thực tế.",
            avgDelta = 2, avgNote = "trên cao nhất hàng ngày trung bình",
            windSpeed = 12, windGust = 20, windDirDeg = 60, windDirName = "ĐĐB",
            uvIndex = 8, uvLabel = "Rất cao", uvNote = "Tránh nắng đến 16:00.",
            sunrise = "05:50", sunset = "17:27", sunProgress = 0.36f,
            rainTodayMm = 0, rainNote = "Không có mưa trong 10 ngày tới.",
            visibilityKm = 24, visibilityNote = "Tầm nhìn hoàn toàn rõ.",
            moonPhaseName = "LƯỠI LIỀM ĐẦU THÁNG", moonIllumination = 37, moonrise = "11:15", daysToFullMoon = 8,
            humidity = 68, dewPoint = 24,
            pressureHpa = 1012,
        ),
        CityWeather(
            name = "Đà Lạt",
            subtitle = "10:03",
            isCurrentLocation = false,
            sky = SkyKind.RAIN_DAY,
            condition = "Mưa",
            temp = 19, high = 21, low = 15,
            summary = "Mưa sẽ tiếp tục đến chiều. Gió giật lên đến 22 km/h.",
            hourly = hourly(19, IconKind.RAIN),
            daily = daily(-10, -12, 19, rainyIcons, rainyRain),
            feelsLike = 18, feelsNote = "Cảm nhận lạnh hơn nhiệt độ thực tế.",
            avgDelta = -1, avgNote = "dưới cao nhất hàng ngày trung bình",
            windSpeed = 14, windGust = 22, windDirDeg = 200, windDirName = "NTN",
            uvIndex = 2, uvLabel = "Thấp", uvNote = "Không cần che chắn đặc biệt.",
            sunrise = "05:35", sunset = "17:10", sunProgress = 0.36f,
            rainTodayMm = 12, rainNote = "Dự báo là 8 mm vào ngày mai.",
            visibilityKm = 6, visibilityNote = "Tầm nhìn bị hạn chế do mưa.",
            moonPhaseName = "LƯỠI LIỀM ĐẦU THÁNG", moonIllumination = 37, moonrise = "11:15", daysToFullMoon = 8,
            humidity = 92, dewPoint = 18,
            pressureHpa = 1015,
        ),
    )
}
