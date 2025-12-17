
# Travelupa - Aplikasi Rekomendasi Tempat Wisata

**Nama = Risqi Achmad Fahreal**\
**NIM = 235150201111048**

Aplikasi Android berbasis Jetpack Compose untuk menyimpan dan mengelola rekomendasi tempat wisata dengan integrasi Firebase dan Room Database.

## Deskripsi

Travelupa adalah aplikasi yang membantu pengguna untuk menyimpan dan mengelola daftar tempat wisata yang ingin dikunjungi. Aplikasi ini dilengkapi dengan fitur upload gambar, penyimpanan lokal dengan Room Database, dan sinkronisasi dengan Firebase Cloud.

## Fitur Utama

### 1. Autentikasi
- Login dengan Firebase Authentication
- Registrasi akun baru
- Validasi email dan password
- Logout dengan konfirmasi

### 2. Manajemen Tempat Wisata
- Tambah tempat wisata baru
- Upload gambar dari galeri
- Deskripsi tempat wisata
- Hapus tempat wisata dengan dropdown menu
- List view dengan LazyColumn

### 3. Penyimpanan Data
- **Firebase Firestore**: Penyimpanan data tempat wisata di cloud
- **Firebase Storage**: Upload dan penyimpanan gambar
- **Room Database**: Cache lokal untuk akses offline
- Sinkronisasi otomatis antara lokal dan cloud

### 4. Galeri Lokal
- Tampilkan semua gambar yang tersimpan di Room Database
- Grid layout untuk preview gambar
- Informasi detail gambar (ID, path, timestamp)
- Hapus gambar dari database lokal

### 5. Navigation
- Greeting Screen sebagai halaman awal
- Login Screen untuk autentikasi
- Register Screen untuk pendaftaran
- Rekomendasi Tempat Screen sebagai halaman utama
- Gallery Screen untuk manajemen gambar lokal

## Teknologi yang Digunakan

### Android Framework
- **Jetpack Compose**: UI modern dan declarative
- **Material Design 3**: Komponen UI yang konsisten
- **Navigation Compose**: Navigasi antar screen
- **Lifecycle & ViewModel**: State management
- **Coroutines**: Asynchronous programming

### Database & Storage
- **Room Database**: Local database dengan SQLite
- **Firebase Firestore**: Cloud NoSQL database
- **Firebase Storage**: Cloud storage untuk media files
- **Firebase Authentication**: User authentication service

### Image Processing
- **Coil**: Image loading library untuk Compose
- **ActivityResult API**: Image picker dari galeri
- **ContentResolver**: Akses file system Android

### Build Tools
- **Gradle**: Build automation
- **Kotlin**: Programming language
- **KAPT**: Annotation processing untuk Room

## Struktur Proyek

```
app/
├── src/main/
│   ├── java/com/example/travelupa/
│   │   ├── MainActivity.kt
│   │   ├── data/
│   │   │   ├── dao/
│   │   │   │   └── ImageDao.kt
│   │   │   ├── database/
│   │   │   │   └── AppDatabase.kt
│   │   │   └── entity/
│   │   │       └── ImageEntity.kt
│   │   ├── navigation/
│   │   │   └── Screen.kt
│   │   ├── ui/
│   │   │   ├── screen/
│   │   │   │   ├── GreetingScreen.kt
│   │   │   │   ├── LoginScreen.kt
│   │   │   │   ├── RegisterScreen.kt
│   │   │   │   ├── RekomendasiTempatScreen.kt
│   │   │   │   └── GalleryScreen.kt
│   │   │   └── theme/
│   │   │       └── TravelupaTheme.kt
│   │   └── utils/
│   │       └── ImageUploadHelper.kt
│   └── res/
│       ├── drawable/
│       │   ├── tumpak_sewu.png
│       │   ├── gunung_bromo.png
│       │   └── default_image.png
│       └── values/
│           ├── strings.xml
│           └── colors.xml
└── build.gradle.kts
```

## Instalasi dan Konfigurasi

### Prasyarat
- Android Studio Hedgehog atau lebih baru
- JDK 11 atau lebih tinggi
- Android SDK API Level 24 (Android 7.0) minimum
- Akun Firebase (gratis)

### Langkah Instalasi

1. **Clone Repository**
   ```bash
   git clone https://github.com/Xfhreall/travelupa-papb
   cd travelupa
   ```

2. **Konfigurasi Firebase**
   - Buat project baru di [Firebase Console](https://console.firebase.google.com)
   - Tambahkan aplikasi Android dengan package name: `com.example.travelupa`
   - Download file `google-services.json`
   - Letakkan file tersebut di folder `app/`

3. **Enable Firebase Services**
   - Firebase Authentication: Enable Email/Password provider
   - Firebase Firestore: Buat database dengan mode test
   - Firebase Storage: Enable dengan security rules berikut:
   
   ```
   rules_version = '2';
   service firebase.storage {
     match /b/{bucket}/o {
       match /{allPaths=**} {
         allow read, write: if request.auth != null;
       }
     }
   }
   ```

4. **Firestore Security Rules**
   ```
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /tempat_wisata/{document=**} {
         allow read, write: if request.auth != null;
       }
     }
   }
   ```

5. **Sync dan Build Project**
   - Buka project di Android Studio
   - Sync project dengan Gradle
   - Build > Make Project
   - Run di emulator atau device fisik
