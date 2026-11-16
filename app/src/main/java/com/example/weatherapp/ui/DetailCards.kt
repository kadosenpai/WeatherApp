package com.example.weatherapp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.model.CityWeather
import kotlin.math.cos
import kotlin.math.sin

// ------------------------------------------------------------------ Dự báo theo giờ

@Composable
fun HourlyCard(city: CityWeather) {
    WeatherCard(Modifier.fillMaxWidth(), padding = 0.dp) {
        WText(city.summary, 15.sp, Modifier.padding(14.dp))
        HairLine()
        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)) {
            items(city.hourly) { hour ->
                Column(Modifier.width(56.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    WText(hour.label, 13.sp, weight = FontWeight.SemiBold)
                    Spacer(Modifier.height(10.dp))
                    WeatherIcon(hour.icon, Modifier.size(30.dp))
                    Spacer(Modifier.height(10.dp))
                    WText("${hour.temp}°", 19.sp, weight = FontWeight.Medium)
                }
            }
        }
    }
}

// ------------------------------------------------------------------ Dự báo 10 ngày

private fun tempColor(t: Int): Color = when {
    t <= 20 -> Color(0xFF4FC3F7)
    t <= 25 -> lerp(Color(0xFF4FC3F7), Color(0xFF8BD66B), (t - 20) / 5f)
    t <= 29 -> lerp(Color(0xFF8BD66B), Color(0xFFFFD24D), (t - 25) / 4f)
    t <= 34 -> lerp(Color(0xFFFFD24D), Color(0xFFFF8A34), (t - 29) / 5f)
    else -> Color(0xFFFF6A2B)
}

@Composable
private fun TempBar(low: Int, high: Int, min: Int, max: Int, current: Int?, modifier: Modifier) {
    Canvas(modifier.height(5.dp)) {
        val range = (max - min).coerceAtLeast(1).toFloat()
        val corner = CornerRadius(size.height / 2f, size.height / 2f)
        drawRoundRect(Color.White.copy(alpha = 0.2f), cornerRadius = corner)
        val x0 = size.width * (low - min) / range
        val x1 = (size.width * (high - min) / range).coerceAtLeast(x0 + size.height)
        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = listOf(tempColor(low), tempColor(high)),
                startX = x0,
                endX = x1,
            ),
            topLeft = Offset(x0, 0f),
            size = Size(x1 - x0, size.height),
            cornerRadius = corner,
        )
        if (current != null) {
            val cx = (size.width * (current - min) / range).coerceIn(size.height / 2f, size.width - size.height / 2f)
            val c = Offset(cx, size.height / 2f)
            drawCircle(Color(0x99102040), size.height * 0.95f, c)
            drawCircle(Color.White, size.height * 0.6f, c)
        }
    }
}

@Composable
fun DailyCard(city: CityWeather) {
    val min = city.daily.minOf { it.low }
    val max = city.daily.maxOf { it.high }
    WeatherCard(Modifier.fillMaxWidth(), title = "DỰ BÁO 10 NGÀY", glyph = Glyph.CALENDAR) {
        city.daily.forEach { d ->
            HairLine()
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                WText(d.day, 19.sp, Modifier.width(78.dp), FontWeight.Medium)
                Column(
                    Modifier.width(38.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    WeatherIcon(d.icon, Modifier.size(26.dp))
                    if (d.precipChance != null) {
                        WText("${d.precipChance}%", 11.sp, weight = FontWeight.SemiBold, color = Color(0xFF7FD1FF))
                    }
                }
                WText("${d.low}°", 19.sp, Modifier.width(40.dp), color = SoftWhite, align = TextAlign.End)
                TempBar(d.low, d.high, min, max, d.currentTemp, Modifier.weight(1f).padding(horizontal = 8.dp))
                WText("${d.high}°", 19.sp, Modifier.width(36.dp), FontWeight.Medium)
            }
        }
    }
}

// ------------------------------------------------------------------ Trung bình / Cảm nhận

@Composable
fun AverageCard(city: CityWeather, modifier: Modifier) {
    val sign = if (city.avgDelta >= 0) "+" else "−"
    WeatherCard(modifier, title = "TRUNG BÌNH", glyph = Glyph.GAUGE) {
        WText("$sign${kotlin.math.abs(city.avgDelta)}°", 32.sp, weight = FontWeight.Medium)
        WText(city.avgNote, 15.sp, Modifier.padding(top = 4.dp))
    }
}

@Composable
fun FeelsCard(city: CityWeather, modifier: Modifier) {
    WeatherCard(modifier, title = "CẢM NHẬN", glyph = Glyph.THERMO) {
        WText("${city.feelsLike}°", 32.sp, weight = FontWeight.Medium)
        Spacer(Modifier.weight(1f))
        WText(city.feelsNote, 14.sp)
    }
}

// ------------------------------------------------------------------ Gió

@Composable
private fun Compass(speed: Int, deg: Int, modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val r = size.minDimension / 2f
            val c = center
            for (i in 0 until 72) {
                val a = Math.toRadians(i * 5.0)
                val long = i % 6 == 0
                val inner = if (long) r * 0.86f else r * 0.92f
                drawLine(
                    Color.White.copy(alpha = if (long) 0.8f else 0.4f),
                    Offset(c.x + (inner * sin(a)).toFloat(), c.y - (inner * cos(a)).toFloat()),
                    Offset(c.x + (r * sin(a)).toFloat(), c.y - (r * cos(a)).toFloat()),
                    strokeWidth = 1.5.dp.toPx(),
                )
            }
            val t = Math.toRadians(deg.toDouble())
            val dx = sin(t).toFloat()
            val dy = -cos(t).toFloat()
            fun p(k: Float) = Offset(c.x + dx * r * k, c.y + dy * r * k)
            drawLine(Color.White, p(0.38f), p(0.62f), 2.dp.toPx(), StrokeCap.Round)
            drawLine(Color.White.copy(alpha = 0.5f), p(-0.38f), p(-0.62f), 2.dp.toPx(), StrokeCap.Round)
            drawCircle(Color.White, 3.5.dp.toPx(), p(0.62f))
        }
        WText("B", 11.sp, Modifier.align(Alignment.TopCenter).padding(top = 14.dp), FontWeight.SemiBold, SoftWhite)
        WText("N", 11.sp, Modifier.align(Alignment.BottomCenter).padding(bottom = 14.dp), FontWeight.SemiBold, SoftWhite)
        WText("Đ", 11.sp, Modifier.align(Alignment.CenterEnd).padding(end = 14.dp), FontWeight.SemiBold, SoftWhite)
        WText("T", 11.sp, Modifier.align(Alignment.CenterStart).padding(start = 14.dp), FontWeight.SemiBold, SoftWhite)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WText("$speed", 26.sp, weight = FontWeight.Medium)
            WText("km/h", 11.sp, color = SoftWhite)
        }
    }
}

@Composable
fun WindCard(city: CityWeather) {
    WeatherCard(Modifier.fillMaxWidth(), title = "GIÓ", glyph = Glyph.WIND) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                KeyValueRow("Gió", "${city.windSpeed} km/h")
                HairLine()
                KeyValueRow("Gió giật", "${city.windGust} km/h")
                HairLine()
                KeyValueRow("Hướng", "${city.windDirDeg}° ${city.windDirName}")
            }
            Spacer(Modifier.width(16.dp))
            Compass(city.windSpeed, city.windDirDeg, Modifier.size(128.dp))
        }
    }
}

// ------------------------------------------------------------------ UV / Mặt trời lặn

@Composable
fun UvCard(city: CityWeather, modifier: Modifier) {
    WeatherCard(modifier, title = "CHỈ SỐ UV", glyph = Glyph.SUN) {
        WText("${city.uvIndex}", 30.sp, weight = FontWeight.Medium)
        WText(city.uvLabel, 18.sp, weight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))
        Canvas(Modifier.fillMaxWidth().height(8.dp)) {
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF3DDC84), Color(0xFFFFEB3B), Color(0xFFFF9800),
                        Color(0xFFF44336), Color(0xFF9C27B0),
                    ),
                ),
                cornerRadius = CornerRadius(size.height / 2f, size.height / 2f),
            )
            val x = (size.width * (city.uvIndex / 11f)).coerceIn(size.height, size.width - size.height)
            val c = Offset(x, size.height / 2f)
            drawCircle(Color(0x99000000), size.height * 0.8f, c)
            drawCircle(Color.White, size.height * 0.6f, c)
        }
        Spacer(Modifier.weight(1f))
        WText(city.uvNote, 12.sp)
    }
}

@Composable
fun SunsetCard(city: CityWeather, modifier: Modifier) {
    WeatherCard(modifier, title = "MẶT TRỜI LẶN", glyph = Glyph.SUNSET) {
        WText(city.sunset, 30.sp, weight = FontWeight.Medium)
        Canvas(Modifier.fillMaxWidth().weight(1f)) {
            val w = size.width
            val h = size.height
            val y0 = h * 0.8f
            val y1 = -h * 0.5f
            val path = Path().apply {
                moveTo(0f, y0)
                quadraticBezierTo(w / 2f, y1, w, y0)
            }
            drawLine(Color.White.copy(alpha = 0.5f), Offset(0f, y0), Offset(w, y0), 1.dp.toPx())
            drawPath(path, Color.White.copy(alpha = 0.6f), style = Stroke(2.dp.toPx()))
            val t = city.sunProgress
            val u = 1f - t
            val px = 2f * u * t * (w / 2f) + t * t * w
            val py = u * u * y0 + 2f * u * t * y1 + t * t * y0
            drawCircle(Color.White.copy(alpha = 0.3f), 8.dp.toPx(), Offset(px, py))
            drawCircle(Color.White, 4.dp.toPx(), Offset(px, py))
        }
        WText("Mặt trời mọc: ${city.sunrise}", 12.sp)
    }
}

// ------------------------------------------------------------------ Lượng mưa / Tầm nhìn

@Composable
fun RainCard(city: CityWeather, modifier: Modifier) {
    WeatherCard(modifier, title = "LƯỢNG MƯA", glyph = Glyph.DROP) {
        WText("${city.rainTodayMm} mm", 30.sp, weight = FontWeight.Medium)
        WText("Hôm nay", 18.sp, weight = FontWeight.Medium)
        Spacer(Modifier.weight(1f))
        WText(city.rainNote, 13.sp)
    }
}

@Composable
fun VisibilityCard(city: CityWeather, modifier: Modifier) {
    WeatherCard(modifier, title = "TẦM NHÌN", glyph = Glyph.EYE) {
        WText("${city.visibilityKm} km", 30.sp, weight = FontWeight.Medium)
        Spacer(Modifier.weight(1f))
        WText(city.visibilityNote, 13.sp)
    }
}

// ------------------------------------------------------------------ Mặt trăng

@Composable
private fun MoonDisc(illumination: Int, modifier: Modifier) {
    Canvas(modifier) {
        val r = size.minDimension / 2f
        val c = center
        val disc = Path().apply { addOval(Rect(c, r)) }
        clipPath(disc) {
            drawCircle(Color(0xFFD5DBEA), r, c)
            drawCircle(Color(0xFF2A3F6E), r, Offset(c.x - 2f * r * illumination / 100f, c.y))
        }
    }
}

@Composable
fun MoonCard(city: CityWeather) {
    WeatherCard(Modifier.fillMaxWidth(), title = city.moonPhaseName, glyph = Glyph.MOON) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                KeyValueRow("Chiếu sáng", "${city.moonIllumination}%")
                HairLine()
                KeyValueRow("Trăng mọc", city.moonrise)
                HairLine()
                KeyValueRow("Trăng tròn tiếp theo", "${city.daysToFullMoon} ngày")
            }
            Spacer(Modifier.width(16.dp))
            MoonDisc(city.moonIllumination, Modifier.size(96.dp))
        }
    }
}

// ------------------------------------------------------------------ Độ ẩm / Áp suất

@Composable
fun HumidityCard(city: CityWeather, modifier: Modifier) {
    WeatherCard(modifier, title = "ĐỘ ẨM", glyph = Glyph.DROP) {
        WText("${city.humidity}%", 30.sp, weight = FontWeight.Medium)
        Spacer(Modifier.weight(1f))
        WText("Điểm sương là ${city.dewPoint}° ngay lúc này.", 13.sp)
    }
}

@Composable
fun PressureCard(city: CityWeather, modifier: Modifier) {
    WeatherCard(modifier, title = "ÁP SUẤT", glyph = Glyph.GAUGE) {
        Box(Modifier.fillMaxWidth().weight(1f)) {
            Canvas(Modifier.fillMaxSize()) {
                val d = minOf(size.width, size.height)
                val stroke = 8.dp.toPx()
                val tl = Offset((size.width - d) / 2f + stroke, (size.height - d) / 2f + stroke)
                val sz = Size(d - 2f * stroke, d - 2f * stroke)
                drawArc(
                    Color.White.copy(alpha = 0.55f), 135f, 270f, false,
                    topLeft = tl,
                    size = sz,
                    style = Stroke(
                        width = stroke,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.dp.toPx(), 4.dp.toPx())),
                    ),
                )
                val frac = ((city.pressureHpa - 950) / 100f).coerceIn(0f, 1f)
                val a = Math.toRadians((135f + 270f * frac).toDouble())
                val r = sz.width / 2f
                val c = Offset(tl.x + r, tl.y + r)
                drawCircle(
                    Color.White,
                    4.5.dp.toPx(),
                    Offset(c.x + (r * cos(a)).toFloat(), c.y + (r * sin(a)).toFloat()),
                )
            }
            Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                WText("=", 16.sp, weight = FontWeight.Bold)
                WText(formatPressure(city.pressureHpa), 20.sp, weight = FontWeight.SemiBold)
                WText("hPa", 12.sp, color = SoftWhite)
            }
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                WText("Thấp", 12.sp, weight = FontWeight.SemiBold, color = SoftWhite)
                WText("Cao", 12.sp, weight = FontWeight.SemiBold, color = SoftWhite)
            }
        }
    }
}
