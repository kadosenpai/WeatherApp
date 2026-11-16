package com.example.weatherapp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.weatherapp.model.IconKind
import kotlin.math.cos
import kotlin.math.sin

// ---------------------------------------------------------------- helpers

private fun DrawScope.rays(color: Color, c: Offset, r0: Float, r1: Float, sw: Float) {
    for (i in 0 until 8) {
        val a = Math.toRadians(i * 45.0)
        drawLine(
            color,
            Offset(c.x + (r0 * cos(a)).toFloat(), c.y + (r0 * sin(a)).toFloat()),
            Offset(c.x + (r1 * cos(a)).toFloat(), c.y + (r1 * sin(a)).toFloat()),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
    }
}

private fun DrawScope.drawSun(c: Offset, r: Float) {
    val sun = Color(0xFFFFC83D)
    drawCircle(sun, r, c)
    rays(sun, c, r * 1.4f, r * 1.85f, r * 0.3f)
}

private fun DrawScope.drawCloud(left: Float, top: Float, w: Float, h: Float, color: Color) {
    drawRoundRect(
        color,
        topLeft = Offset(left, top + h * 0.4f),
        size = Size(w, h * 0.6f),
        cornerRadius = CornerRadius(h * 0.3f, h * 0.3f),
    )
    drawCircle(color, h * 0.32f, Offset(left + w * 0.32f, top + h * 0.5f))
    drawCircle(color, h * 0.42f, Offset(left + w * 0.62f, top + h * 0.42f))
}

// ---------------------------------------------------------------- weather icons (multi-colour)

@Composable
fun WeatherIcon(kind: IconKind, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val cloud = Color(0xFFF4F7FB)
        val cloudShade = Color(0xFFB9C4D6)
        when (kind) {
            IconKind.SUNNY -> drawSun(Offset(w / 2f, h / 2f), w * 0.21f)
            IconKind.PARTLY_CLOUDY -> {
                drawSun(Offset(w * 0.66f, h * 0.34f), w * 0.15f)
                drawCloud(w * 0.04f, h * 0.3f, w * 0.88f, h * 0.55f, cloud)
            }
            IconKind.CLOUDY -> {
                drawCloud(w * 0.22f, h * 0.12f, w * 0.72f, h * 0.5f, cloudShade)
                drawCloud(w * 0.02f, h * 0.3f, w * 0.88f, h * 0.58f, cloud)
            }
            IconKind.RAIN -> {
                drawCloud(w * 0.04f, h * 0.08f, w * 0.92f, h * 0.55f, cloudShade)
                for (i in 0..2) {
                    val x = w * (0.28f + i * 0.22f)
                    drawLine(
                        Color(0xFF7FD1FF),
                        Offset(x, h * 0.7f),
                        Offset(x - w * 0.06f, h * 0.92f),
                        strokeWidth = w * 0.07f,
                        cap = StrokeCap.Round,
                    )
                }
            }
            IconKind.NIGHT -> drawCircle(Color(0xFFF2EBC0), w * 0.3f, center)
        }
    }
}

// ---------------------------------------------------------------- small glyphs for card titles

enum class Glyph { SUN, DROP, EYE, GAUGE, WIND, SUNSET, MOON, THERMO, CALENDAR }

@Composable
fun GlyphIcon(glyph: Glyph, modifier: Modifier = Modifier, color: Color = SoftWhite) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val sw = w * 0.12f
        when (glyph) {
            Glyph.SUN -> {
                drawCircle(color, w * 0.2f, center)
                rays(color, center, w * 0.32f, w * 0.48f, sw)
            }
            Glyph.DROP -> {
                val p = Path().apply {
                    moveTo(w * 0.5f, h * 0.08f)
                    cubicTo(w * 0.95f, h * 0.5f, w * 0.85f, h * 0.92f, w * 0.5f, h * 0.92f)
                    cubicTo(w * 0.15f, h * 0.92f, w * 0.05f, h * 0.5f, w * 0.5f, h * 0.08f)
                    close()
                }
                drawPath(p, color)
            }
            Glyph.EYE -> {
                drawOval(color, topLeft = Offset(w * 0.04f, h * 0.25f), size = Size(w * 0.92f, h * 0.5f), style = Stroke(sw))
                drawCircle(color, w * 0.14f, center)
            }
            Glyph.GAUGE -> {
                drawArc(
                    color, 135f, 270f, false,
                    topLeft = Offset(sw, sw),
                    size = Size(w - 2 * sw, h - 2 * sw),
                    style = Stroke(sw, cap = StrokeCap.Round),
                )
            }
            Glyph.WIND -> {
                drawLine(color, Offset(w * 0.05f, h * 0.3f), Offset(w * 0.75f, h * 0.3f), sw, StrokeCap.Round)
                drawLine(color, Offset(w * 0.05f, h * 0.5f), Offset(w * 0.95f, h * 0.5f), sw, StrokeCap.Round)
                drawLine(color, Offset(w * 0.05f, h * 0.7f), Offset(w * 0.6f, h * 0.7f), sw, StrokeCap.Round)
            }
            Glyph.SUNSET -> {
                val baseY = h * 0.25f + w * 0.35f
                drawArc(
                    color, 180f, 180f, false,
                    topLeft = Offset(w * 0.15f, h * 0.25f),
                    size = Size(w * 0.7f, w * 0.7f),
                    style = Stroke(sw),
                )
                drawLine(color, Offset(0f, baseY), Offset(w, baseY), sw, StrokeCap.Round)
            }
            Glyph.MOON -> drawCircle(color, w * 0.4f, center)
            Glyph.THERMO -> {
                drawRoundRect(color, Offset(w * 0.4f, h * 0.05f), Size(w * 0.2f, h * 0.6f), CornerRadius(w * 0.1f, w * 0.1f))
                drawCircle(color, w * 0.2f, Offset(w * 0.5f, h * 0.78f))
            }
            Glyph.CALENDAR -> {
                drawRoundRect(
                    color,
                    topLeft = Offset(w * 0.08f, h * 0.15f),
                    size = Size(w * 0.84f, h * 0.75f),
                    cornerRadius = CornerRadius(w * 0.12f, w * 0.12f),
                    style = Stroke(sw),
                )
                drawLine(color, Offset(w * 0.08f, h * 0.38f), Offset(w * 0.92f, h * 0.38f), sw)
            }
        }
    }
}

// ---------------------------------------------------------------- navigation icons

enum class NavKind { MAP, LIST, LOCATION, MENU, SEARCH }

@Composable
fun NavIcon(kind: NavKind, modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val sw = w * 0.08f
        when (kind) {
            NavKind.MAP -> {
                val p = Path().apply {
                    moveTo(w * 0.1f, h * 0.2f)
                    lineTo(w * 0.37f, h * 0.1f)
                    lineTo(w * 0.63f, h * 0.2f)
                    lineTo(w * 0.9f, h * 0.1f)
                    lineTo(w * 0.9f, h * 0.8f)
                    lineTo(w * 0.63f, h * 0.9f)
                    lineTo(w * 0.37f, h * 0.8f)
                    lineTo(w * 0.1f, h * 0.9f)
                    close()
                }
                drawPath(p, color, style = Stroke(sw, join = StrokeJoin.Round))
                drawLine(color, Offset(w * 0.37f, h * 0.1f), Offset(w * 0.37f, h * 0.8f), sw)
                drawLine(color, Offset(w * 0.63f, h * 0.2f), Offset(w * 0.63f, h * 0.9f), sw)
            }
            NavKind.LIST -> {
                for (i in 0..2) {
                    val y = h * (0.25f + i * 0.25f)
                    drawCircle(color, sw * 0.9f, Offset(w * 0.12f, y))
                    drawLine(color, Offset(w * 0.28f, y), Offset(w * 0.92f, y), sw * 1.2f, StrokeCap.Round)
                }
            }
            NavKind.LOCATION -> {
                val p = Path().apply {
                    moveTo(w * 0.88f, h * 0.12f)
                    lineTo(w * 0.12f, h * 0.46f)
                    lineTo(w * 0.54f, h * 0.5f)
                    lineTo(w * 0.58f, h * 0.88f)
                    close()
                }
                drawPath(p, color)
            }
            NavKind.MENU -> {
                drawCircle(color, w * 0.5f - sw, center, style = Stroke(sw))
                drawCircle(color, w * 0.05f, Offset(w * 0.3f, h * 0.5f))
                drawCircle(color, w * 0.05f, Offset(w * 0.5f, h * 0.5f))
                drawCircle(color, w * 0.05f, Offset(w * 0.7f, h * 0.5f))
            }
            NavKind.SEARCH -> {
                drawCircle(color, w * 0.26f, Offset(w * 0.42f, h * 0.42f), style = Stroke(sw * 1.3f))
                drawLine(color, Offset(w * 0.62f, h * 0.62f), Offset(w * 0.9f, h * 0.9f), sw * 1.3f, StrokeCap.Round)
            }
        }
    }
}
