package com.example.weatherapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.model.CityWeather

@Composable
fun CityListScreen(
    cities: List<CityWeather>,
    onCityClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuOpen by remember { mutableStateOf(false) }

    Box(modifier.fillMaxSize().background(Color.Black)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.End) {
                    Box {
                        Box(
                            Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2C2C2E))
                                .clickable { menuOpen = true },
                            contentAlignment = Alignment.Center,
                        ) {
                            NavIcon(NavKind.MENU, Modifier.size(22.dp))
                        }
                        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                            listOf("Sửa danh sách", "Thông báo", "Độ C", "Độ F", "Đơn vị", "Báo cáo sự cố").forEach { label ->
                                DropdownMenuItem(text = { Text(label) }, onClick = { menuOpen = false })
                            }
                        }
                    }
                }
            }
            item {
                WText("Thời tiết", 34.sp, Modifier.padding(top = 4.dp, bottom = 6.dp), FontWeight.Bold)
            }
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1C1C1E))
                        .padding(horizontal = 10.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    NavIcon(NavKind.SEARCH, Modifier.size(18.dp), Color(0xFF8E8E93))
                    Spacer(Modifier.width(8.dp))
                    WText("Tìm tên thành phố/sân bay", 17.sp, color = Color(0xFF8E8E93))
                }
            }
            itemsIndexed(cities) { index, city ->
                CityCard(city) { onCityClick(index) }
            }
            item {
                WText(
                    "Dữ liệu minh họa (giai đoạn 1)",
                    12.sp,
                    Modifier.fillMaxWidth().padding(top = 12.dp),
                    color = Color(0xFF8E8E93),
                    align = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun CityCard(city: CityWeather, onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(108.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClick() },
    ) {
        SkyBackground(city.sky, Modifier.matchParentSize(), animated = false)
        Row(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)) {
            Column(Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                Column {
                    WText(city.name, 20.sp, weight = FontWeight.SemiBold)
                    WText(city.subtitle, 13.sp, color = Color.White.copy(alpha = 0.85f))
                }
                WText(city.condition, 14.sp)
            }
            Column(
                Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                WText("${city.temp}°", 46.sp, weight = FontWeight.ExtraLight, lineHeight = 50.sp)
                WText("C:${city.high}° T:${city.low}°", 14.sp)
            }
        }
    }
}
