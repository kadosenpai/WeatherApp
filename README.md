# Ứng dụng Thời tiết (Android) – Giai đoạn 1

Giao diện theo phong cách app Thời tiết của iOS, dựng bằng Kotlin + Jetpack Compose.
Giai đoạn 1 dùng **dữ liệu giả** (xem `data/FakeWeatherData.kt`); chưa gắn Open-Meteo.

## Build bằng GitHub Actions
1. Tạo repo mới trên GitHub, đẩy toàn bộ thư mục này lên nhánh `main`.
2. Vào tab **Actions** -> workflow **Build APK** (tự chạy khi push, hoặc bấm *Run workflow*).
3. Khi xong, mở lần chạy đó -> mục **Artifacts** -> tải `WeatherApp-debug-apk` (file zip chứa `app-debug.apk`).
4. Giải nén, chép APK vào điện thoại Android và cài (cần bật "Cài ứng dụng từ nguồn không rõ").

## Cấu trúc
- `model/` – model dữ liệu của app (UI chỉ biết model này)
- `data/` – dữ liệu giả (sau này thay bằng Repository gọi Open-Meteo)
- `ui/SkyBackground.kt` – nền gradient, mây trôi, mưa rơi
- `ui/DetailScreen.kt` – màn chi tiết (pager nhiều thành phố, header co lại khi cuộn)
- `ui/DetailCards.kt` – các card: giờ, 10 ngày, gió, UV, mặt trời lặn, mưa, tầm nhìn, trăng, độ ẩm, áp suất
- `ui/CityListScreen.kt` – danh sách thành phố
