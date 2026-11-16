package com.example.weatherapp.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.weatherapp.model.SkyKind

fun skyColors(sky: SkyKind): List<Color> = when (sky) {
    SkyKind.CLEAR_DAY -> listOf(Color(0xFF4A93E8), Color(0xFF2F78D6), Color(0xFF1F5CB0))
    SkyKind.CLOUDY_DAY -> listOf(Color(0xFF7FA3D6), Color(0xFF4B78BE), Color(0xFF2E5AA3))
    SkyKind.RAIN_DAY -> listOf(Color(0xFF66727F), Color(0xFF4A5663), Color(0xFF343E49))
    SkyKind.CLEAR_NIGHT -> listOf(Color(0xFF0B1230), Color(0xFF1A2755), Color(0xFF2A3470))
    SkyKind.CLOUDY_NIGHT -> listOf(Color(0xFF151B33), Color(0xFF242C4C), Color(0xFF343C5E))
}

/** Nền bầu trời: gradient + (tuỳ chọn) quầng nắng, mây trôi, mưa rơi. */
@Composable
fun SkyBackground(sky: SkyKind, modifier: Modifier = Modifier, animated: Boolean = true) {
    Box(modifier.background(Brush.verticalGradient(skyColors(sky)))) {
        if (animated) {
            SunGlow(sky)
            CloudLayer(sky)
            if (sky == SkyKind.RAIN_DAY) RainLayer()
        }
    }
}

@Composable
private fun SunGlow(sky: SkyKind) {
    if (sky != SkyKind.CLEAR_DAY && sky != SkyKind.CLOUDY_DAY) return
    Canvas(Modifier.fillMaxSize()) {
        val a = if (sky == SkyKind.CLEAR_DAY) 0.55f else 0.4f
        drawRect(
            Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = a), Color.Transparent),
                center = Offset(size.width * 0.5f, -size.height * 0.02f),
                radius = size.width * 0.9f,
            ),
        )
    }
}

private class Blob(val y: Float, val r: Float, val k: Int, val phase: Float, val alpha: Float)

private val blobs = listOf(
    Blob(0.16f, 0.55f, 1, 0.10f, 0.30f),
    Blob(0.34f, 0.70f, 1, 0.55f, 0.22f),
    Blob(0.52f, 0.60f, 2, 0.30f, 0.18f),
    Blob(0.70f, 0.80f, 1, 0.80f, 0.20f),
    Blob(0.88f, 0.65f, 2, 0.00f, 0.16f),
)

@Composable
private fun CloudLayer(sky: SkyKind) {
    val transition = rememberInfiniteTransition(label = "clouds")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(120_000, easing = LinearEasing)),
        label = "cloudT",
    )
    val isNight = sky == SkyKind.CLEAR_NIGHT || sky == SkyKind.CLOUDY_NIGHT
    val tint = if (isNight) Color(0xFFAEB8E8) else Color.White
    Canvas(Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        for (b in blobs) {
            val r = w * b.r
            val x = ((b.phase + t * b.k) % 1f) * (w + 2f * r) - r
            val c = Offset(x, h * b.y)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(tint.copy(alpha = b.alpha), Color.Transparent),
                    center = c,
                    radius = r,
                ),
                radius = r,
                center = c,
            )
        }
    }
}

@Composable
private fun RainLayer() {
    val transition = rememberInfiniteTransition(label = "rain")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "rainT",
    )
    val streaks = remember {
        val rnd = java.util.Random(7L)
        List(70) { Triple(rnd.nextFloat(), rnd.nextFloat(), rnd.nextInt(2) + 1) }
    }
    Canvas(Modifier.fillMaxSize()) {
        val len = 22.dp.toPx()
        for ((fx, fy, k) in streaks) {
            val y = ((fy + t * k) % 1f) * (size.height + len) - len
            val x = fx * size.width
            drawLine(
                Color.White.copy(alpha = 0.28f),
                Offset(x, y),
                Offset(x - len * 0.18f, y + len),
                strokeWidth = 1.5.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}
