package com.example.weatherapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** Cờ chất lượng đồ họa. Tự tắt shader nếu máy yếu/giả lập vẽ quá chậm hoặc lần chạy trước bị lỗi. */
object Quality {
    var shaderOk by mutableStateOf(true)
}
