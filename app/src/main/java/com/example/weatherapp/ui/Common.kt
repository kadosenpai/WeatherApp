package com.example.weatherapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val CardColor = Color(0x33123A7A)
val CardShape = RoundedCornerShape(20.dp)
val SoftWhite = Color.White.copy(alpha = 0.6f)

/** Text ngắn gọn: (nội dung, cỡ chữ, ...). */
@Composable
fun WText(
    text: String,
    size: TextUnit,
    modifier: Modifier = Modifier,
    weight: FontWeight = FontWeight.Normal,
    color: Color = Color.White,
    align: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = size,
        fontWeight = weight,
        textAlign = align,
        lineHeight = lineHeight,
    )
}

@Composable
fun CardTitle(title: String, glyph: Glyph?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (glyph != null) {
            GlyphIcon(glyph, Modifier.size(13.dp))
            Spacer(Modifier.width(5.dp))
        }
        WText(title, 12.sp, weight = FontWeight.SemiBold, color = SoftWhite)
    }
}

/** Khung card mờ kiểu Apple Weather (giai đoạn 1: nền bán trong suốt, chưa blur). */
@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    glyph: Glyph? = null,
    padding: Dp = 14.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier
            .clip(CardShape)
            .background(CardColor)
            .border(0.5.dp, Color.White.copy(alpha = 0.12f), CardShape)
            .padding(padding),
    ) {
        if (title != null) {
            CardTitle(title, glyph)
            Spacer(Modifier.height(8.dp))
        }
        content()
    }
}

@Composable
fun HairLine() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Color.White.copy(alpha = 0.2f)),
    )
}

@Composable
fun KeyValueRow(key: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WText(key, 16.sp, Modifier.weight(1f))
        WText(value, 16.sp, color = SoftWhite)
    }
}

fun formatPressure(hpa: Int): String {
    val s = hpa.toString()
    return if (s.length > 3) s.dropLast(3) + "." + s.takeLast(3) else s
}
