package com.example.weatherapp

import android.graphics.Color as AndroidColor
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.weatherapp.data.FakeWeatherData
import com.example.weatherapp.ui.CityListScreen
import com.example.weatherapp.ui.DetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
        )
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                WeatherApp()
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
