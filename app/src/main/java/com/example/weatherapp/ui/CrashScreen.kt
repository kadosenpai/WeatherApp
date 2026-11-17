package com.example.weatherapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Màn hình hiện nội dung lỗi của lần chạy trước, để chụp màn hình hoặc chép gửi đi. */
@Composable
fun CrashScreen(trace: String, safeMode: Boolean, onContinue: () -> Unit) {
    val clipboard = LocalClipboardManager.current
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
    ) {
        WText("Lần chạy trước bị lỗi", 20.sp, weight = FontWeight.Bold)
        if (safeMode) {
            WText("Đã bật chế độ an toàn (tắt shader mây).", 13.sp, color = SoftWhite)
        }
        Spacer(Modifier.height(8.dp))
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFF1C1C1E))
                .verticalScroll(rememberScrollState())
                .padding(10.dp),
        ) {
            Text(
                text = trace,
                color = Color(0xFFFFB4AB),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(onClick = { clipboard.setText(AnnotatedString(trace)) }) { Text("Chép nội dung lỗi") }
            Button(onClick = onContinue) { Text("Tiếp tục") }
        }
    }
}
