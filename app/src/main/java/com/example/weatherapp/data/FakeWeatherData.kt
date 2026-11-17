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

    private val baseCities: List<CityWeather> = listOf(
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

    /** Các thành phố demo để xem từng kiểu nền: bình minh, hoàng hôn, đêm, giông bão, gió. */
    private val extraCities: List<CityWeather> = run {
        val b = baseCities[0]
        listOf(
            b.copy(
                name = "Hà Nội", subtitle = "05:52", isCurrentLocation = false,
                sky = SkyKind.DAWN, condition = "Bình minh", temp = 21, high = 27, low = 20,
                summary = "Trời quang dần khi mặt trời mọc. Gió nhẹ.",
                hourly = hourly(21, IconKind.PARTLY_CLOUDY),
                feelsLike = 21, feelsNote = "Cảm nhận giống nhiệt độ thực tế.",
                uvIndex = 0, uvLabel = "Thấp", uvNote = "Chưa cần chống nắng.",
                sunrise = "06:05", sunset = "17:12", sunProgress = 0f,
                windSpeed = 6, windGust = 10, windDirDeg = 45, windDirName = "ĐB",
                humidity = 84, dewPoint = 18,
            ),
            b.copy(
                name = "Huế", subtitle = "17:20", isCurrentLocation = false,
                sky = SkyKind.DUSK, condition = "Hoàng hôn", temp = 26, high = 29, low = 24,
                summary = "Trời quang, chuyển tối dần. Gió nhẹ.",
                hourly = hourly(26, IconKind.PARTLY_CLOUDY),
                feelsLike = 27, feelsNote = "Cảm nhận ấm hơn nhiệt độ thực tế.",
                uvIndex = 0, uvLabel = "Thấp", uvNote = "Chưa cần chống nắng.",
                sunrise = "05:58", sunset = "17:34", sunProgress = 0.97f,
                windSpeed = 8, windGust = 13, windDirDeg = 90, windDirName = "Đ",
                humidity = 78, dewPoint = 22,
            ),
            b.copy(
                name = "Cần Thơ", subtitle = "22:10", isCurrentLocation = false,
                sky = SkyKind.CLEAR_NIGHT, condition = "Trời quang", temp = 26, high = 31, low = 25,
                summary = "Trời quang suốt đêm. Gió nhẹ.",
                hourly = hourly(26, IconKind.NIGHT),
                feelsLike = 28, feelsNote = "Cảm nhận ấm hơn nhiệt độ thực tế.",
                uvIndex = 0, uvLabel = "Thấp", uvNote = "Không cần chống nắng.",
                sunrise = "05:50", sunset = "17:28", sunProgress = 1f,
                windSpeed = 7, windGust = 11, windDirDeg = 120, windDirName = "ĐNĐ",
                visibilityKm = 24, visibilityNote = "Tầm nhìn hoàn toàn rõ.",
                humidity = 80, dewPoint = 22,
            ),
            b.copy(
                name = "Hải Phòng", subtitle = "14:30", isCurrentLocation = false,
                sky = SkyKind.STORM, condition = "Mưa to, có sấm sét", temp = 24, high = 26, low = 22,
                summary = "Dông bão kèm sấm sét. Gió giật lên đến 55 km/h.",
                hourly = hourly(24, IconKind.STORM),
                feelsLike = 25, feelsNote = "Cảm nhận giống nhiệt độ thực tế.",
                windSpeed = 32, windGust = 55, windDirDeg = 200, windDirName = "NTN",
                uvIndex = 1, uvLabel = "Thấp", uvNote = "Không cần che chắn đặc biệt.",
                rainTodayMm = 48, rainNote = "Dự báo thêm 30 mm vào ngày mai.",
                visibilityKm = 3, visibilityNote = "Tầm nhìn rất kém do mưa lớn.",
                humidity = 96, dewPoint = 23, pressureHpa = 1004,
            ),
            b.copy(
                name = "Quy Nhơn", subtitle = "10:03", isCurrentLocation = false,
                sky = SkyKind.WINDY, condition = "Nhiều gió", temp = 28, high = 30, low = 25,
                summary = "Gió mạnh cả ngày. Gió giật lên đến 62 km/h.",
                hourly = hourly(28, IconKind.WIND),
                feelsLike = 27, feelsNote = "Cảm nhận mát hơn nhiệt độ thực tế do gió.",
                windSpeed = 38, windGust = 62, windDirDeg = 70, windDirName = "ĐĐB",
                humidity = 70, dewPoint = 22, pressureHpa = 1010,
            ),
        )
    }

    val cities: List<CityWeather> = baseCities + extraCities
}
