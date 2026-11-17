package com.example.weatherapp.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.model.CityWeather
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailScreen(cities: List<CityWeather>, startIndex: Int, onOpenList: () -> Unit) {
    val pagerState = rememberPagerState(initialPage = startIndex) { cities.size }
    Box(Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            CityPage(cities[page])
        }
        BottomBar(
            cities = cities,
            current = pagerState.currentPage,
            onOpenList = onOpenList,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

/** Theo dõi tổng số pixel đã cuộn (để làm parallax). */
private class ScrollTracker : NestedScrollConnection {
    var y by mutableFloatStateOf(0f)

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        y = (y - consumed.y).coerceAtLeast(0f)
        return Offset.Zero
    }
}

/** Mỗi khối hiện ra bằng hiệu ứng mờ dần + trượt lên (lần lượt, chỉ chạy một lần cho mỗi khối). */
@Composable
private fun AnimItem(index: Int, seen: MutableSet<Int>, content: @Composable () -> Unit) {
    val already = remember { index in seen }
    val p = remember { Animatable(if (already) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (!already) {
            delay((index * 70L).coerceAtMost(350L))
            p.animateTo(1f, tween(500, easing = FastOutSlowInEasing))
        }
        seen.add(index)
    }
    Box(
        Modifier.graphicsLayer {
            alpha = p.value
            translationY = (1f - p.value) * 36.dp.toPx()
        },
    ) {
        content()
    }
}

@Composable
private fun CityPage(city: CityWeather) {
    val listState = rememberLazyListState()
    val tracker = remember { ScrollTracker() }
    val seen = remember { mutableSetOf<Int>() }
    val collapsePx = with(LocalDensity.current) { 170.dp.toPx() }
    val progress = remember(collapsePx) {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) {
                1f
            } else {
                (listState.firstVisibleItemScrollOffset / collapsePx).coerceIn(0f, 1f)
            }
        }
    }
    val topColor = skyColors(city.sky).first()

    Box(Modifier.fillMaxSize().nestedScroll(tracker)) {
        SkyBackground(city.sky, Modifier.fillMaxSize(), scroll = { tracker.y })

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { Header(city, { progress.value }, { tracker.y }) }
            item { AnimItem(1, seen) { HourlyCard(city) } }
            item { AnimItem(2, seen) { DailyCard(city) } }
            item {
                AnimItem(3, seen) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AverageCard(city, Modifier.weight(1f).aspectRatio(1f))
                        FeelsCard(city, Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
            item { AnimItem(4, seen) { WindCard(city) } }
            item {
                AnimItem(5, seen) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        UvCard(city, Modifier.weight(1f).aspectRatio(1f))
                        SunsetCard(city, Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
            item {
                AnimItem(6, seen) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        RainCard(city, Modifier.weight(1f).aspectRatio(1f))
                        VisibilityCard(city, Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
            item { AnimItem(7, seen) { MoonCard(city) } }
            item {
                AnimItem(8, seen) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        HumidityCard(city, Modifier.weight(1f).aspectRatio(1f))
                        PressureCard(city, Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
            item {
                WText(
                    "Dữ liệu minh họa (giai đoạn 1)",
                    12.sp,
                    Modifier.fillMaxWidth().padding(top = 8.dp),
                    color = SoftWhite,
                    align = TextAlign.Center,
                )
            }
        }

        CollapsedBar(city, topColor) { progress.value }
    }
}

/** Phần đầu: tên, nhiệt độ lớn, mô tả. Mờ dần và nhỏ lại khi cuộn. */
@Composable
private fun Header(city: CityWeather, progress: () -> Float, scroll: () -> Float) {
    Column(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 28.dp, bottom = 32.dp)
            .graphicsLayer {
                val p = progress()
                alpha = (1f - p * 1.3f).coerceIn(0f, 1f)
                scaleX = 1f - p * 0.08f
                scaleY = 1f - p * 0.08f
                translationY = scroll().coerceIn(0f, 700f) * 0.4f
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (city.isCurrentLocation) {
            WText("VỊ TRÍ CỦA TÔI", 12.sp, weight = FontWeight.Medium, color = SoftWhite)
        }
        WText(city.name, 32.sp, align = TextAlign.Center)
        WText("${city.temp}°", 96.sp, weight = FontWeight.ExtraLight, lineHeight = 104.sp)
        WText(city.condition, 20.sp, weight = FontWeight.Medium, color = Color.White.copy(alpha = 0.9f))
        WText("C:${city.high}°  T:${city.low}°", 20.sp, weight = FontWeight.Medium)
    }
}

/** Thanh ghim phía trên, hiện ra khi header đã cuộn đi. */
@Composable
private fun CollapsedBar(city: CityWeather, topColor: Color, progress: () -> Float) {
    Column(
        Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRect(
                    Brush.verticalGradient(
                        listOf(topColor.copy(alpha = 0.92f * progress()), Color.Transparent),
                    ),
                )
            }
            .statusBarsPadding()
            .padding(top = 6.dp, bottom = 22.dp)
            .graphicsLayer { alpha = ((progress() - 0.6f) / 0.4f).coerceIn(0f, 1f) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WText(city.name, 20.sp, weight = FontWeight.Medium)
        WText("${city.temp}° | ${city.condition}", 15.sp, color = Color.White.copy(alpha = 0.85f))
    }
}

@Composable
private fun BottomBar(
    cities: List<CityWeather>,
    current: Int,
    onOpenList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0x73000000))))
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
            NavIcon(NavKind.MAP, Modifier.size(24.dp))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            cities.forEachIndexed { i, c ->
                val a = if (i == current) 1f else 0.4f
                if (c.isCurrentLocation) {
                    NavIcon(NavKind.LOCATION, Modifier.size(13.dp), Color.White.copy(alpha = a))
                } else {
                    Box(
                        Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = a)),
                    )
                }
            }
        }
        Box(
            Modifier
                .size(44.dp)
                .clickable { onOpenList() },
            contentAlignment = Alignment.Center,
        ) {
            NavIcon(NavKind.LIST, Modifier.size(24.dp))
        }
    }
}
