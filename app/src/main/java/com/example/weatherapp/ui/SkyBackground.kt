package com.example.weatherapp.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.example.weatherapp.model.SkyKind
import kotlin.math.abs
import kotlin.math.sin

fun skyColors(sky: SkyKind): List<Color> = when (sky) {
    SkyKind.CLEAR_DAY -> listOf(Color(0xFF3F8CEB), Color(0xFF2F78D6), Color(0xFF2266BE))
    SkyKind.CLOUDY_DAY -> listOf(Color(0xFF7FA3D6), Color(0xFF4B78BE), Color(0xFF2E5AA3))
    SkyKind.RAIN_DAY -> listOf(Color(0xFF66727F), Color(0xFF4A5663), Color(0xFF343E49))
    SkyKind.STORM -> listOf(Color(0xFF1B2028), Color(0xFF29313B), Color(0xFF38424E))
    SkyKind.WINDY -> listOf(Color(0xFF6FA4D8), Color(0xFF5A8FC6), Color(0xFF4879AD))
    SkyKind.DAWN -> listOf(Color(0xFF3E4C93), Color(0xFF9A78B4), Color(0xFFF0A08C), Color(0xFFFFCF8F))
    SkyKind.DUSK -> listOf(Color(0xFF26346E), Color(0xFF6B4A9C), Color(0xFFD9645A), Color(0xFFF2A65A))
    SkyKind.CLEAR_NIGHT -> listOf(Color(0xFF070C24), Color(0xFF131E4A), Color(0xFF243170))
    SkyKind.CLOUDY_NIGHT -> listOf(Color(0xFF121830), Color(0xFF212947), Color(0xFF323A5B))
}

/** Nền bầu trời: gradient + hiệu ứng riêng cho từng kiểu thời tiết (chỉ chạy khi animated = true). */
@Composable
fun SkyBackground(sky: SkyKind, modifier: Modifier = Modifier, animated: Boolean = true) {
    Box(modifier.background(Brush.verticalGradient(skyColors(sky)))) {
        if (animated) {
            when (sky) {
                SkyKind.CLEAR_DAY -> {
                    SunLayer()
                    CloudLayer(Color.White, 0.55f, 120_000)
                }
                SkyKind.CLOUDY_DAY -> {
                    SunGlow(0.4f)
                    CloudLayer(Color.White, 1f, 120_000)
                }
                SkyKind.RAIN_DAY -> {
                    CloudLayer(Color(0xFFD0D6DE), 1f, 100_000)
                    RainLayer(count = 70, durationMs = 1200, alpha = 0.28f, slant = 0.18f, lenDp = 22f, widthDp = 1.5f)
                }
                SkyKind.STORM -> {
                    CloudLayer(Color(0xFF9AA3B0), 1.3f, 60_000)
                    RainLayer(count = 190, durationMs = 650, alpha = 0.38f, slant = 0.3f, lenDp = 32f, widthDp = 1.6f)
                    LightningLayer()
                }
                SkyKind.WINDY -> {
                    SunGlow(0.3f)
                    CloudLayer(Color.White, 1.2f, 30_000)
                    WindLayer()
                }
                SkyKind.DAWN -> {
                    HorizonGlow(Color(0xFFFFB27A))
                    CloudLayer(Color(0xFFFFD0B8), 0.9f, 140_000)
                }
                SkyKind.DUSK -> {
                    HorizonGlow(Color(0xFFFF8A4C))
                    CloudLayer(Color(0xFFFFB48F), 0.9f, 140_000)
                }
                SkyKind.CLEAR_NIGHT -> {
                    StarLayer(90)
                    MoonLayer()
                    CloudLayer(Color(0xFFAEB8E8), 0.35f, 150_000)
                }
                SkyKind.CLOUDY_NIGHT -> {
                    StarLayer(30)
                    MoonLayer()
                    CloudLayer(Color(0xFF9AA4D0), 0.9f, 120_000)
                }
            }
        }
    }
}

// ------------------------------------------------------------------ nắng

@Composable
private fun SunGlow(strength: Float) {
    Canvas(Modifier.fillMaxSize()) {
        drawRect(
            Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = strength), Color.Transparent),
                center = Offset(size.width * 0.5f, -size.height * 0.02f),
                radius = size.width * 0.9f,
            ),
        )
    }
}

/** Mặt trời có quầng sáng nhấp nháy nhẹ và các tia sáng xoay chậm. */
@Composable
private fun SunLayer() {
    val transition = rememberInfiniteTransition(label = "sun")
    val rot by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(180_000, easing = LinearEasing)),
        label = "sunRot",
    )
    val pulse by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "sunPulse",
    )
    Canvas(Modifier.fillMaxSize()) {
        val c = Offset(size.width * 0.78f, size.height * 0.10f)
        val r = size.width * 0.75f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFF3C4).copy(alpha = 0.9f * pulse),
                    Color(0xFFFFE08A).copy(alpha = 0.35f * pulse),
                    Color.Transparent,
                ),
                center = c,
                radius = r,
            ),
            radius = r,
            center = c,
        )
        rotate(rot, c) {
            for (i in 0 until 10) {
                rotate(i * 36f, c) {
                    drawLine(
                        Color.White.copy(alpha = 0.09f),
                        c,
                        Offset(c.x, c.y + size.height * 1.3f),
                        strokeWidth = 26.dp.toPx(),
                        cap = StrokeCap.Round,
                    )
                }
            }
        }
        drawCircle(Color(0xFFFFFBE6), size.width * 0.07f, c)
    }
}

// ------------------------------------------------------------------ mây

private class Blob(val y: Float, val r: Float, val k: Int, val phase: Float, val alpha: Float)

private val blobs = listOf(
    Blob(0.16f, 0.55f, 1, 0.10f, 0.30f),
    Blob(0.34f, 0.70f, 1, 0.55f, 0.22f),
    Blob(0.52f, 0.60f, 2, 0.30f, 0.18f),
    Blob(0.70f, 0.80f, 1, 0.80f, 0.20f),
    Blob(0.88f, 0.65f, 2, 0.00f, 0.16f),
)

@Composable
private fun CloudLayer(tint: Color, alphaScale: Float, periodMs: Int) {
    val transition = rememberInfiniteTransition(label = "clouds")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(periodMs, easing = LinearEasing)),
        label = "cloudT",
    )
    Canvas(Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        for (b in blobs) {
            val r = w * b.r
            val x = ((b.phase + t * b.k) % 1f) * (w + 2f * r) - r
            val c = Offset(x, h * b.y)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(tint.copy(alpha = (b.alpha * alphaScale).coerceIn(0f, 1f)), Color.Transparent),
                    center = c,
                    radius = r,
                ),
                radius = r,
                center = c,
            )
        }
    }
}

// ------------------------------------------------------------------ bình minh / hoàng hôn

@Composable
private fun HorizonGlow(color: Color) {
    val transition = rememberInfiniteTransition(label = "glow")
    val pulse by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(5000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glowP",
    )
    Canvas(Modifier.fillMaxSize()) {
        val c = Offset(size.width * 0.5f, size.height * 0.36f)
        val r = size.width * 1.0f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    color.copy(alpha = 0.75f * pulse),
                    color.copy(alpha = 0.25f * pulse),
                    Color.Transparent,
                ),
                center = c,
                radius = r,
            ),
            radius = r,
            center = c,
        )
    }
}

// ------------------------------------------------------------------ ban đêm

private class Star(val x: Float, val y: Float, val r: Float, val phase: Float)

@Composable
private fun StarLayer(count: Int) {
    val transition = rememberInfiniteTransition(label = "stars")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing)),
        label = "starT",
    )
    val stars = remember(count) {
        val rnd = java.util.Random(11L)
        List(count) {
            Star(rnd.nextFloat(), rnd.nextFloat() * 0.75f, 0.6f + rnd.nextFloat(), rnd.nextFloat())
        }
    }
    Canvas(Modifier.fillMaxSize()) {
        for (s in stars) {
            val tw = 0.5f + 0.5f * sin(2f * Math.PI.toFloat() * (t + s.phase))
            drawCircle(
                Color.White.copy(alpha = 0.35f + 0.65f * tw),
                s.r.dp.toPx(),
                Offset(s.x * size.width, s.y * size.height),
            )
        }
    }
}

@Composable
private fun MoonLayer() {
    Canvas(Modifier.fillMaxSize()) {
        val c = Offset(size.width * 0.82f, size.height * 0.11f)
        val r = size.width * 0.055f
        val halo = size.width * 0.4f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFDDE4FF).copy(alpha = 0.35f), Color.Transparent),
                center = c,
                radius = halo,
            ),
            radius = halo,
            center = c,
        )
        drawCircle(Color(0xFFF4F1DE), r, c)
        drawCircle(Color(0x1F8A8F9E), r * 0.28f, Offset(c.x - r * 0.3f, c.y - r * 0.2f))
        drawCircle(Color(0x1F8A8F9E), r * 0.2f, Offset(c.x + r * 0.35f, c.y + r * 0.3f))
    }
}

// ------------------------------------------------------------------ mưa / sấm chớp

@Composable
private fun RainLayer(count: Int, durationMs: Int, alpha: Float, slant: Float, lenDp: Float, widthDp: Float) {
    val transition = rememberInfiniteTransition(label = "rain")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMs, easing = LinearEasing)),
        label = "rainT",
    )
    val streaks = remember(count) {
        val rnd = java.util.Random(7L)
        List(count) { Triple(rnd.nextFloat(), rnd.nextFloat(), rnd.nextInt(2) + 1) }
    }
    Canvas(Modifier.fillMaxSize()) {
        val len = lenDp.dp.toPx()
        for ((fx, fy, k) in streaks) {
            val y = ((fy + t * k) % 1f) * (size.height + len) - len
            val x = fx * size.width
            drawLine(
                Color.White.copy(alpha = alpha),
                Offset(x, y),
                Offset(x - len * slant, y + len),
                strokeWidth = widthDp.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}

private val flashTimes = floatArrayOf(0.10f, 0.125f, 0.55f, 0.58f, 0.84f)

private fun boltPoints(seed: Long): List<Offset> {
    val rnd = java.util.Random(seed)
    val pts = mutableListOf<Offset>()
    var x = 0.2f + rnd.nextFloat() * 0.6f
    var y = 0f
    pts.add(Offset(x, y))
    while (y < 0.5f) {
        y += 0.05f + rnd.nextFloat() * 0.05f
        x += (rnd.nextFloat() - 0.5f) * 0.12f
        pts.add(Offset(x, y))
    }
    return pts
}

private val bolts: List<List<Offset>> = flashTimes.indices.map { boltPoints(100L + it) }

/** Chớp: toàn màn hình loé trắng + tia sét ngoằn ngoèo, lặp lại theo chu kỳ 9 giây. */
@Composable
private fun LightningLayer() {
    val transition = rememberInfiniteTransition(label = "lightning")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing)),
        label = "lightningT",
    )
    Canvas(Modifier.fillMaxSize()) {
        var best = 0f
        var idx = -1
        for (i in flashTimes.indices) {
            val a = 1f - abs(t - flashTimes[i]) / 0.02f
            if (a > best) {
                best = a
                idx = i
            }
        }
        if (best > 0f && idx >= 0) {
            drawRect(Color.White.copy(alpha = best * 0.5f))
            val path = Path()
            bolts[idx].forEachIndexed { j, p ->
                val o = Offset(p.x * size.width, p.y * size.height)
                if (j == 0) path.moveTo(o.x, o.y) else path.lineTo(o.x, o.y)
            }
            drawPath(
                path,
                Color(0xFFBFD4FF).copy(alpha = best * 0.5f),
                style = Stroke(10.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
            drawPath(
                path,
                Color.White.copy(alpha = best),
                style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
        }
    }
}

// ------------------------------------------------------------------ gió

private class Streak(val y: Float, val len: Float, val k: Int, val phase: Float, val alpha: Float, val amp: Float)

private val windStreaks: List<Streak> = run {
    val rnd = java.util.Random(3L)
    List(16) {
        Streak(
            y = 0.08f + rnd.nextFloat() * 0.8f,
            len = 0.25f + rnd.nextFloat() * 0.35f,
            k = rnd.nextInt(3) + 1,
            phase = rnd.nextFloat(),
            alpha = 0.18f + rnd.nextFloat() * 0.22f,
            amp = 6f + rnd.nextFloat() * 12f,
        )
    }
}

@Composable
private fun WindLayer() {
    val transition = rememberInfiniteTransition(label = "wind")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "windT",
    )
    Canvas(Modifier.fillMaxSize()) {
        for (s in windStreaks) {
            val len = size.width * s.len
            val amp = s.amp.dp.toPx()
            val x = ((s.phase + t * s.k) % 1f) * (size.width + len) - len
            val y = size.height * s.y
            val path = Path().apply {
                moveTo(x, y)
                cubicTo(x + len * 0.33f, y - amp, x + len * 0.66f, y + amp, x + len, y)
            }
            drawPath(
                path,
                Color.White.copy(alpha = s.alpha),
                style = Stroke(1.6.dp.toPx(), cap = StrokeCap.Round),
            )
        }
    }
}
