package com.example.weatherapp

import android.graphics.Color as AndroidColor
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.weatherapp.data.FakeWeatherData
import com.example.weatherapp.ui.CityListScreen
import com.example.weatherapp.ui.CrashScreen
import com.example.weatherapp.ui.DetailScreen
import com.example.weatherapp.ui.Quality
import java.io.File
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
        )

        // --- Chế độ an toàn: nếu lần mở trước chưa chạy ổn định quá 2,5 giây thì tắt shader (vĩnh viễn).
        val prefs = getSharedPreferences("weather_prefs", MODE_PRIVATE)
        if (prefs.getBoolean("launching", false)) {
            prefs.edit().putBoolean("safe", true).commit()
        }
        val safe = prefs.getBoolean("safe", false)
        Quality.shaderOk = !safe
        prefs.edit().putBoolean("launching", true).commit()

        // --- Ghi lại lỗi Java để hiện lên màn hình ở lần mở sau.
        val crashFile = File(filesDir, "crash.txt")
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, error ->
            try {
                val header = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT}) " +
                    "${Build.MANUFACTURER} ${Build.MODEL}\n\n"
                crashFile.writeText((header + error.stackTraceToString()).take(7000))
            } catch (_: Throwable) {
            }
            previous?.uncaughtException(thread, error)
        }
        val crashText: String? = try {
            if (crashFile.exists()) crashFile.readText() else null
        } catch (_: Throwable) {
            null
        }

        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                var crash by remember { mutableStateOf(crashText) }
                LaunchedEffect(Unit) {
                    delay(2500)
                    prefs.edit().putBoolean("launching", false).apply()
                }
                val shown = crash
                if (shown != null) {
                    CrashScreen(trace = shown, safeMode = safe) {
                        crashFile.delete()
                        crash = null
                    }
                } else {
                    WeatherApp()
                }
            }
        }
    }
}

@Composable
fun WeatherApp() {
    val cities = remember { FakeWeatherData.cities }
    // null = đang ở danh sách thành phố; số = đang xem chi tiết thành phố đó
    var selected by rememberSaveable { mutableStateOf<Int?>(0) }
    val current = selected

    BackHandler(enabled = current != null) { selected = null }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        if (current != null) {
            DetailScreen(cities = cities, startIndex = current, onOpenList = { selected = null })
        } else {
            CityListScreen(cities = cities, onCityClick = { selected = it })
        }
    }
}
