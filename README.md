
# 📱 DemoMiniApp

Ứng dụng Android demo việc tích hợp Mini App (theo chuẩn Rakuten) hiển thị bằng WebView, cho phép giao tiếp hai chiều giữa Mini App (JS) và native app (Kotlin).

## 🏗️ Cấu trúc chính

```
.
├── MainActivity.kt               # Mở Mini App và hiển thị message từ Mini App
├── MiniAppWebViewActivity.kt     # Hiển thị Mini App qua WebView và xử lý alert, permission
├── sdk/
│   ├── MiniAppManager.kt         # Tạo và config WebView hiển thị Mini App
│   ├── CustomMiniAppMessageBridge.kt # Xử lý message JS -> Native và permission
│   └── MiniAppDevicePermissionType.kt # Enum permission (CAMERA, ...)
```

## 🚀 Tính năng

- ✅ Load Mini App từ server (`http://10.0.2.2:3000`)
- ✅ Giao tiếp JS ↔ Native qua `MiniAppBridge`
- ✅ Xin quyền CAMERA từ WebView (qua `requestDevicePermission`)
- ✅ Nhận message từ Mini App và hiển thị lên UI Android
- ✅ Xử lý `alert()` trong WebView bằng native `AlertDialog`

## 🔧 Cài đặt & chạy

### 1. Clone repo:
```bash
git clone https://github.com/your-username/DemoMiniApp.git
cd DemoMiniApp
```

### 2. Mở bằng Android Studio

### 3. Chạy miniapp-server (Node.js):
```bash
cd miniapp-server
npm install
node server.js
```

> Server chạy ở `http://10.0.2.2:3000` (với AVD Android Emulator)

## 📦 Giao tiếp JS <-> Native

### Từ JS gọi Native:
```js
MiniAppBridge.postMessage("Hello from MiniApp!");
MiniAppBridge.requestDevicePermission("CAMERA");
```

### Từ Native gửi kết quả về JS:
```kotlin
webView.evaluateJavascript("onCameraPermissionResult(true);", null)
```

## 🛡️ Quyền yêu cầu

- `CAMERA` (qua `requestPermissions` và xử lý trong `onRequestPermissionsResult`)

## 📸 Giao diện

| Màn hình chính | Mini App |
|----------------|----------|
| Nút "Mở Mini App" & TextView kết quả | WebView hiển thị Mini App từ server |

## 🧑‍💻 Đóng góp

Mọi đóng góp đều được chào đón!  
Fork repo, tạo nhánh mới, PR nhé. 🚀

## 📃 License

MIT License
