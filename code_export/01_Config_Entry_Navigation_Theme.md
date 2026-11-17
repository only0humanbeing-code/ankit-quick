# Bundle 01: Config, Entry, Navigation & Theme

---

## File: `settings.gradle.kts`
```kotlin
pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "QuickKart"

include(":app")
```

---

## File: `build.gradle.kts`
```kotlin
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.google.devtools.ksp) apply false
  alias(libs.plugins.secrets) apply false
  alias(libs.plugins.google.services) apply false
}
```

---

## File: `app/build.gradle.kts`
```kotlin
import com.google.gms.googleservices.GoogleServicesPlugin.MissingGoogleServicesStrategy

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.services)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.aistudio.quickkart.indqkc"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD")
      keyAlias = "upload"
      keyPassword = System.getenv("KEY_PASSWORD")
    }
    create("debugConfig") {
      storeFile = file("${rootDir}/debug.keystore")
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
    debug {
      signingConfig = signingConfigs.getByName("debugConfig")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
}

secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
  ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
}

googleServices { missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN }

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.coil.compose)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.moshi.kotlin)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}
```

---

## File: `gradle/libs.versions.toml`
```toml
[versions]
agp = "9.1.1"
coreKtx = "1.18.0"
junit = "4.13.2"
junitVersion = "1.3.0"
espressoCore = "3.7.0"
lifecycleRuntimeKtx = "2.8.7"
lifecycleViewmodelCompose = "2.8.7"
lifecycleRuntimeCompose = "2.8.7"
activityCompose = "1.10.1"
kotlin = "2.2.10"
composeBom = "2024.09.00"
googleDevtoolsKsp = "2.3.5"
navigationCompose = "2.8.9"
roomRuntime = "2.7.0"
roomKtx = "2.7.0"
roomCompiler = "2.7.0"
kotlinxCoroutinesTest = "1.10.2"
core = "1.6.1"
runner = "1.6.2"
coilCompose = "2.7.0"
retrofit = "2.12.0"
converterMoshi = "2.12.0"
kotlinxCoroutinesAndroid = "1.10.2"
kotlinxCoroutinesCore = "1.10.2"
accompanistPermissions = "0.37.3"
playServicesLocation = "21.3.0"
cameraCamera2 = "1.5.0"
cameraLifecycle = "1.5.0"
cameraView = "1.5.0"
cameraCore = "1.5.0"
loggingInterceptor = "4.10.0"
okhttp = "4.10.0"
moshiKotlin = "1.15.2"
moshiKotlinCodegen = "1.15.2"
datastorePreferences = "1.1.7"
robolectric = "4.16.1"
firebaseBom = "34.17.0"
secretsGradlePlugin = "2.0.1"
googleServices = "4.5.0"
credentials = "1.5.0"
googleid = "1.1.1"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleViewmodelCompose" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeCompose" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "roomRuntime" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "roomKtx" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "roomCompiler" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "kotlinxCoroutinesTest" }
androidx-core = { group = "androidx.test", name = "core", version.ref = "core" }
androidx-runner = { group = "androidx.test", name = "runner", version.ref = "runner" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose-material-icons-core = { group = "androidx.compose.material", name = "material-icons-core" }
androidx-compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coilCompose" }
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
converter-moshi = { group = "com.squareup.retrofit2", name = "converter-moshi", version.ref = "converterMoshi" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "kotlinxCoroutinesAndroid" }
kotlinx-coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "kotlinxCoroutinesCore" }
accompanist-permissions = { group = "com.google.accompanist", name = "accompanist-permissions", version.ref = "accompanistPermissions" }
play-services-location = { group = "com.google.android.gms", name = "play-services-location", version.ref = "playServicesLocation" }
androidx-camera-camera2 = { group = "androidx.camera", name = "camera-camera2", version.ref = "cameraCamera2" }
androidx-camera-lifecycle = { group = "androidx.camera", name = "camera-lifecycle", version.ref = "cameraLifecycle" }
androidx-camera-view = { group = "androidx.camera", name = "camera-view", version.ref = "cameraView" }
androidx-camera-core = { group = "androidx.camera", name = "camera-core", version.ref = "cameraCore" }
logging-interceptor = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "loggingInterceptor" }
okhttp = { group = "com.squareup.okhttp3", name = "okhttp", version.ref = "okhttp" }
moshi-kotlin = { group = "com.squareup.moshi", name = "moshi-kotlin", version.ref = "moshiKotlin" }
moshi-kotlin-codegen = { group = "com.squareup.moshi", name = "moshi-kotlin-codegen", version.ref = "moshiKotlinCodegen" }
androidx-datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastorePreferences" }
robolectric = { group = "org.robolectric", name = "robolectric", version.ref = "robolectric" }
firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebaseBom" }
firebase-ai = { group = "com.google.firebase", name = "firebase-ai" }
firebase-appcheck-recaptcha = { group = "com.google.firebase", name = "firebase-appcheck-recaptcha" }
firebase-appcheck-debug = { group = "com.google.firebase", name = "firebase-appcheck-debug" }
firebase-firestore = { group = "com.google.firebase", name = "firebase-firestore" }
firebase-auth = { group = "com.google.firebase", name = "firebase-auth" }
androidx-credentials = { group = "androidx.credentials", name = "credentials", version.ref = "credentials" }
androidx-credentials-play-services = { group = "androidx.credentials", name = "credentials-play-services-auth", version.ref = "credentials" }
googleid = { group = "com.google.android.libraries.identity.googleid", name = "googleid", version.ref = "googleid" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
google-devtools-ksp = { id = "com.google.devtools.ksp", version.ref = "googleDevtoolsKsp" }
secrets = { id = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin", version.ref = "secretsGradlePlugin" }
google-services = { id = "com.google.gms.google-services", version.ref = "googleServices" }
```

---

## File: `app/src/main/AndroidManifest.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MyApplication">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.MyApplication">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

---

## File: `app/src/main/res/values/strings.xml`
```xml
<resources>
    <string name="app_name">QuickKart</string>
</resources>
```

---

## File: `app/src/main/java/com/example/MainActivity.kt`
```kotlin
package com.example

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.QuickKartDatabase
import com.example.data.repository.RoomQuickCommerceRepository
import com.example.ui.QuickKartApp
import com.example.ui.theme.QuickKartTheme
import com.example.viewmodel.QuickKartViewModel
import com.example.viewmodel.QuickKartViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        )

        val db = QuickKartDatabase.getInstance(applicationContext)
        val repository = RoomQuickCommerceRepository(db)
        val factory = QuickKartViewModelFactory(repository)

        setContent {
            QuickKartTheme {
                val vm: QuickKartViewModel = viewModel(factory = factory)
                QuickKartApp(viewModel = vm)
            }
        }
    }
}
```

---

## File: `app/src/main/java/com/example/ui/theme/Color.kt`
```kotlin
package com.example.ui.theme

import androidx.compose.ui.graphics.Color

val QuickGreen = Color(0xFF5D9923)
val QuickGreenDark = Color(0xFF437416)
val QuickGreenLight = Color(0xFF72B035)
val QuickGreenContainer = Color(0xFFEEF7E5)
val QuickGreenOnContainer = Color(0xFF284D09)

val QuickAmber = Color(0xFFFACC15)
val QuickGold = Color(0xFFD97706)
val QuickAmberContainer = Color(0xFFFEF3C7)

val QuickBlue = Color(0xFF2563EB)
val QuickBlueContainer = Color(0xFFDBEAFE)

val QuickRed = Color(0xFFDC2626)
val QuickRedContainer = Color(0xFFFEE2E2)

val QuickBackground = Color(0xFFFFFFFF)
val QuickSurface = Color(0xFFFFFFFF)
val QuickSurfaceVariant = Color(0xFFF4F6F8)
val QuickSlate = Color(0xFF1F2937)
val QuickSlateSecondary = Color(0xFF4B5563)
val QuickMuted = Color(0xFF6B7280)
val QuickOutline = Color(0xFFE5E7EB)

// Dark Theme tokens
val QuickDarkBackground = Color(0xFF0E1411)
val QuickDarkSurface = Color(0xFF161F1A)
val QuickDarkSurfaceVariant = Color(0xFF202C25)
val QuickDarkText = Color(0xFFF3F4F6)
val QuickDarkMuted = Color(0xFF9CA3AF)
val QuickDarkGreen = Color(0xFF34D399)
val QuickDarkGreenContainer = Color(0xFF064E3B)
```

---

## File: `app/src/main/java/com/example/ui/theme/Theme.kt`
```kotlin
package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = QuickGreen,
    onPrimary = Color.White,
    primaryContainer = QuickGreenContainer,
    onPrimaryContainer = QuickGreenOnContainer,
    secondary = QuickGold,
    onSecondary = Color.White,
    secondaryContainer = QuickAmberContainer,
    onSecondaryContainer = QuickSlate,
    tertiary = QuickBlue,
    onTertiary = Color.White,
    tertiaryContainer = QuickBlueContainer,
    onTertiaryContainer = QuickSlate,
    background = QuickBackground,
    onBackground = QuickSlate,
    surface = QuickSurface,
    onSurface = QuickSlate,
    surfaceVariant = QuickSurfaceVariant,
    onSurfaceVariant = QuickMuted,
    outline = QuickOutline,
    error = QuickRed,
    onError = Color.White,
    errorContainer = QuickRedContainer,
    onErrorContainer = QuickRed
)

private val DarkColorScheme = darkColorScheme(
    primary = QuickDarkGreen,
    onPrimary = QuickDarkBackground,
    primaryContainer = QuickDarkGreenContainer,
    onPrimaryContainer = Color(0xFFA7F3D0),
    secondary = QuickAmber,
    onSecondary = QuickSlate,
    secondaryContainer = Color(0xFF451A03),
    onSecondaryContainer = Color(0xFFFDE68A),
    tertiary = Color(0xFF60A5FA),
    onTertiary = QuickSlate,
    background = QuickDarkBackground,
    onBackground = QuickDarkText,
    surface = QuickDarkSurface,
    onSurface = QuickDarkText,
    surfaceVariant = QuickDarkSurfaceVariant,
    onSurfaceVariant = QuickDarkMuted,
    outline = Color(0xFF2E3C34),
    error = Color(0xFFF87171),
    onError = QuickSlate
)

val QuickKartShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun QuickKartTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = QuickKartTypography,
        shapes = QuickKartShapes,
        content = content
    )
}
```

---

## File: `app/src/main/java/com/example/ui/theme/Type.kt`
```kotlin
package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val OutfitFontFamily = FontFamily(
    Font(R.font.outfit, FontWeight.Normal),
    Font(R.font.outfit, FontWeight.Medium),
    Font(R.font.outfit, FontWeight.SemiBold),
    Font(R.font.outfit, FontWeight.Bold),
    Font(R.font.outfit, FontWeight.ExtraBold)
)

val PlusJakartaFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans, FontWeight.Bold)
)

val QuickKartTypography = Typography(
    displaySmall = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp
    ),
    bodySmall = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 18.sp
    ),
    labelMedium = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 12.sp
    )
)
```

---

## File: `app/src/main/java/com/example/ui/QuickKartApp.kt`
```kotlin
package com.example.ui

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.local.ShopCategoryTaxonomy
import com.example.model.SavedAddress
import com.example.ui.components.FilterSortBottomSheet
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.AddEditAddressDialog
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.BrandProductsScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.ManufacturerBrandsScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProductListScreen
import com.example.ui.screens.ShopByCategoryScreen
import com.example.ui.theme.QuickGreen
import com.example.ui.theme.QuickGreenContainer
import com.example.ui.theme.QuickMuted
import com.example.util.FormatUtils
import com.example.viewmodel.QuickKartViewModel
import kotlinx.coroutines.flow.collectLatest

object QuickKartRoutes {
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val CATALOG = "catalog"
    const val ORDERS = "orders"
    const val CART = "cart"
    const val ACCOUNT = "account"
    const val PRODUCT_DETAIL = "product/{productId}"
    const val MANUFACTURER_BRANDS = "manufacturer/{brandId}"
    const val BRAND_PRODUCTS = "brand_products/{brandId}/{subBrand}"
    const val SUBCATEGORY_PRODUCTS = "subcategory_products/{shopCategory}/{subCategory}"
    const val CHECKOUT = "checkout"
    const val ADMIN = "admin"

    fun productDetail(productId: String) = "product/$productId"
    fun manufacturerBrands(brandId: String) = "manufacturer/$brandId"
    fun brandProducts(brandId: String, subBrand: String?): String {
        val encoded = if (subBrand.isNullOrBlank()) "__ALL__" else Uri.encode(subBrand)
        return "brand_products/$brandId/$encoded"
    }
    fun subCategoryProducts(shopCategory: String, subCategory: String?): String {
        val encodedCat = Uri.encode(shopCategory)
        val encodedSub = if (subCategory.isNullOrBlank()) "__ALL__" else Uri.encode(subCategory)
        return "subcategory_products/$encodedCat/$encodedSub"
    }
}

private data class BottomNavTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickKartApp(viewModel: QuickKartViewModel) {
    val isReady by viewModel.isReady.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val brands by viewModel.brands.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val filteredProducts by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val availablePackSizes by viewModel.availablePackSizes.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val cartSummary by viewModel.cartSummary.collectAsStateWithLifecycle()
    val addresses by viewModel.addresses.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val deliverySlots by viewModel.deliverySlots.collectAsStateWithLifecycle()
    val storeConfig by viewModel.storeConfig.collectAsStateWithLifecycle()
    val notifyProductIds by viewModel.notifyProductIds.collectAsStateWithLifecycle()
    val savedProductIds by viewModel.savedProductIds.collectAsStateWithLifecycle()
    val isProcessingOrder by viewModel.isProcessingOrder.collectAsStateWithLifecycle()
    val lastPlacedOrder by viewModel.lastPlacedOrder.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var showFilterSheet by remember { mutableStateOf(false) }
    var showAddressPickerSheet by remember { mutableStateOf(false) }
    var showNewAddressDialogFromPicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    if (!isReady) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = QuickGreen)
        }
        return
    }

    if (!userProfile.isLoggedIn) {
        LoginScreen(
            onVerifyOtp = { phone, otp, onError ->
                viewModel.verifyOtp(
                    phone = phone,
                    otp = otp,
                    onSuccess = { },
                    onError = onError
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(QuickGreen)
                .statusBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        )
        return
    }

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomTabs = listOf(
        BottomNavTab(QuickKartRoutes.HOME, "Home", Icons.Rounded.Home, Icons.Outlined.Home),
        BottomNavTab(QuickKartRoutes.CATEGORIES, "Categories", Icons.Rounded.Category, Icons.Outlined.Category),
        BottomNavTab(QuickKartRoutes.ORDERS, "Orders", Icons.Rounded.ReceiptLong, Icons.Outlined.ReceiptLong),
        BottomNavTab(QuickKartRoutes.CART, "Cart", Icons.Rounded.ShoppingCart, Icons.Outlined.ShoppingCart),
        BottomNavTab(QuickKartRoutes.ACCOUNT, "Account", Icons.Rounded.Person, Icons.Outlined.Person)
    )

    val showBottomBar = currentRoute in setOf(
        QuickKartRoutes.HOME,
        QuickKartRoutes.CATEGORIES,
        QuickKartRoutes.CATALOG,
        QuickKartRoutes.MANUFACTURER_BRANDS,
        QuickKartRoutes.BRAND_PRODUCTS,
        QuickKartRoutes.SUBCATEGORY_PRODUCTS,
        QuickKartRoutes.ORDERS,
        QuickKartRoutes.CART,
        QuickKartRoutes.ACCOUNT
    )

    val showFloatingCartBanner = currentRoute in setOf(
        QuickKartRoutes.HOME,
        QuickKartRoutes.CATEGORIES,
        QuickKartRoutes.CATALOG,
        QuickKartRoutes.MANUFACTURER_BRANDS,
        QuickKartRoutes.BRAND_PRODUCTS,
        QuickKartRoutes.SUBCATEGORY_PRODUCTS
    ) && cartSummary.totalCount > 0

    val activeAddress: SavedAddress? = addresses.firstOrNull { it.isDefault } ?: addresses.firstOrNull()

    fun navigateTopTab(route: String) {
        navController.navigate(route) {
            popUpTo(QuickKartRoutes.HOME) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                Column(modifier = Modifier.background(Color.White)) {
                    AnimatedVisibility(visible = showFloatingCartBanner) {
                        Surface(
                            color = QuickGreen,
                            shape = RoundedCornerShape(14.dp),
                            shadowElevation = 6.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .clickable { navigateTopTab(QuickKartRoutes.CART) }
                                .testTag("floating_view_cart_bar")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Rounded.ShoppingBag,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Column {
                                        AnimatedContent(
                                            targetState = "${cartSummary.totalCount} Item(s) • ${FormatUtils.inr(cartSummary.itemTotal)}",
                                            transitionSpec = {
                                                fadeIn(tween(150)).togetherWith(fadeOut(tween(120)))
                                            },
                                            label = "cart_bar_total"
                                        ) { summaryText ->
                                            Text(
                                                text = summaryText,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        if (cartSummary.itemSavings > 0) {
                                            Text(
                                                text = "Saving ${FormatUtils.inr(cartSummary.itemSavings)} on MRP",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White.copy(alpha = 0.88f)
                                            )
                                        }
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "View Cart",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 0.8.dp)
                    NavigationBar(
                        containerColor = Color.White,
                        tonalElevation = 0.dp
                    ) {
                        bottomTabs.forEach { tab ->
                            val selected = currentRoute == tab.route ||
                                (tab.route == QuickKartRoutes.CATEGORIES && (currentRoute == QuickKartRoutes.CATALOG || currentRoute == QuickKartRoutes.SUBCATEGORY_PRODUCTS))
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (tab.route == QuickKartRoutes.CATEGORIES) {
                                        viewModel.resetFiltersCompletely()
                                    }
                                    navigateTopTab(tab.route)
                                },
                                icon = {
                                    if (tab.route == QuickKartRoutes.CART && cartSummary.totalCount > 0) {
                                        BadgedBox(
                                            badge = {
                                                Badge(containerColor = QuickGreen, contentColor = Color.White) {
                                                    Text("${cartSummary.totalCount}")
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                                contentDescription = tab.label
                                            )
                                        }
                                    } else {
                                        Icon(
                                            imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.label
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        text = tab.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                        maxLines = 1
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = QuickGreen,
                                    selectedTextColor = QuickGreen,
                                    unselectedIconColor = Color(0xFF4B5563),
                                    unselectedTextColor = Color(0xFF4B5563),
                                    indicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.testTag("bottom_nav_${tab.route}")
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        val standardScreenModifier = Modifier
            .fillMaxSize()
            .background(QuickGreen)
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)

        NavHost(
            navController = navController,
            startDestination = QuickKartRoutes.HOME,
            enterTransition = {
                fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                    slideInHorizontally(initialOffsetX = { it / 16 }, animationSpec = tween(220, easing = FastOutSlowInEasing))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(180, easing = FastOutSlowInEasing))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                    slideInHorizontally(initialOffsetX = { -it / 16 }, animationSpec = tween(220, easing = FastOutSlowInEasing))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(180, easing = FastOutSlowInEasing)) +
                    slideOutHorizontally(targetOffsetX = { it / 14 }, animationSpec = tween(180, easing = FastOutSlowInEasing))
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(QuickKartRoutes.HOME) {
                HomeScreen(
                    categories = categories,
                    brands = brands,
                    activeAddress = activeAddress,
                    storeConfig = storeConfig,
                    onAddressClick = { showAddressPickerSheet = true },
                    onSearchBarClick = {
                        viewModel.resetFiltersCompletely()
                        navController.navigate(QuickKartRoutes.CATALOG)
                    },
                    onFilterClick = {
                        navController.navigate(QuickKartRoutes.CATALOG)
                        showFilterSheet = true
                    },
                    onAdminClick = { navController.navigate(QuickKartRoutes.ADMIN) },
                    onAccountClick = { navigateTopTab(QuickKartRoutes.ACCOUNT) },
                    onOrdersClick = { navigateTopTab(QuickKartRoutes.ORDERS) },
                    onCategoryClick = { cat ->
                        viewModel.selectCategoryFilter(cat.id)
                        navController.navigate(QuickKartRoutes.CATALOG)
                    },
                    onShopCategoryClick = { shopCategory, subCategory ->
                        viewModel.selectShopCategoryFilter(shopCategory, subCategory)
                        navController.navigate(QuickKartRoutes.subCategoryProducts(shopCategory, subCategory))
                    },
                    onBrandClick = { brand ->
                        navController.navigate(QuickKartRoutes.manufacturerBrands(brand.id))
                    },
                    onSeeAllCategoriesClick = { navigateTopTab(QuickKartRoutes.CATEGORIES) },
                    onViewAllProducts = {
                        viewModel.resetFiltersCompletely()
                        navController.navigate(QuickKartRoutes.CATALOG)
                    }
                )
            }

            composable(QuickKartRoutes.CATEGORIES) {
                ShopByCategoryScreen(
                    totalProductCount = products.size,
                    onBackClick = { navigateTopTab(QuickKartRoutes.HOME) },
                    onSearchSubmit = { query ->
                        viewModel.resetFiltersCompletely()
                        viewModel.updateSearchQuery(query)
                        navController.navigate(QuickKartRoutes.CATALOG)
                    },
                    onSelectCategoryAll = { group ->
                        viewModel.selectShopCategoryFilter(group.title, null)
                        navController.navigate(QuickKartRoutes.subCategoryProducts(group.title, null))
                    },
                    onSelectSubCategory = { group, subName ->
                        viewModel.selectShopCategoryFilter(group.title, subName)
                        navController.navigate(QuickKartRoutes.subCategoryProducts(group.title, subName))
                    }
                )
            }

            composable(QuickKartRoutes.CATALOG) {
                val selectedCat = categories.firstOrNull { it.id == filterState.categoryId }
                val selectedBrand = brands.firstOrNull { it.id == filterState.brandId }
                val dynamicTitle = selectedCat?.name ?: selectedBrand?.name ?: "Explore Products"
                ProductListScreen(
                    title = dynamicTitle,
                    showBackButton = true,
                    categories = categories,
                    brands = brands,
                    filteredProducts = filteredProducts,
                    filterState = filterState,
                    cartItems = cartItems,
                    notifyProductIds = notifyProductIds,
                    onBackClick = { navController.popBackStack() },
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onCategorySelect = viewModel::selectCategoryFilter,
                    onBrandSelect = viewModel::selectBrandFilter,
                    onOpenFilterSheet = { showFilterSheet = true },
                    onResetFilters = viewModel::resetFiltersCompletely,
                    onProductClick = { prod -> navController.navigate(QuickKartRoutes.productDetail(prod.id)) },
                    onAddClick = { prod -> viewModel.addOrIncrementCart(prod) },
                    onIncrementClick = { prod -> viewModel.addOrIncrementCart(prod) },
                    onDecrementClick = { prod -> viewModel.decrementCart(prod.id, prod.defaultVariant.id) },
                    onNotifyClick = { prod -> viewModel.toggleNotifyMe(prod.id, prod.name) },
                    modifier = standardScreenModifier
                )
            }

            composable(
                route = QuickKartRoutes.MANUFACTURER_BRANDS,
                arguments = listOf(navArgument("brandId") { type = NavType.StringType })
            ) { entry ->
                val brandId = entry.arguments?.getString("brandId").orEmpty()
                val manufacturer = brands.firstOrNull { it.id == brandId }
                if (manufacturer == null) {
                    Box(standardScreenModifier, contentAlignment = Alignment.Center) {
                        Text("Manufacturer not found")
                    }
                } else {
                    val mfgProducts = products.filter { it.brandId == brandId }
                    ManufacturerBrandsScreen(
                        manufacturer = manufacturer,
                        productsForManufacturer = mfgProducts,
                        onBackClick = { navController.popBackStack() },
                        onSearchClick = {
                            viewModel.selectBrandFilter(brandId)
                            navController.navigate(QuickKartRoutes.CATALOG)
                        },
                        onSubBrandClick = { subEntry ->
                            viewModel.selectSubBrandFilter(brandId, subEntry.name)
                            navController.navigate(QuickKartRoutes.brandProducts(brandId, subEntry.name))
                        },
                        onViewAllManufacturerProducts = {
                            viewModel.selectSubBrandFilter(brandId, null)
                            navController.navigate(QuickKartRoutes.brandProducts(brandId, null))
                        },
                        modifier = standardScreenModifier
                    )
                }
            }

            composable(
                route = QuickKartRoutes.BRAND_PRODUCTS,
                arguments = listOf(
                    navArgument("brandId") { type = NavType.StringType },
                    navArgument("subBrand") { type = NavType.StringType }
                )
            ) { entry ->
                val brandId = entry.arguments?.getString("brandId").orEmpty()
                val rawSubBrand = entry.arguments?.getString("subBrand").orEmpty()
                val subBrand = if (rawSubBrand == "__ALL__") null else Uri.decode(rawSubBrand)
                val manufacturer = brands.firstOrNull { it.id == brandId }
                val title = subBrand ?: manufacturer?.name ?: "Products"

                val allSubBrandProducts = products.filter { prod ->
                    prod.brandId == brandId && (subBrand == null || prod.subBrand == subBrand)
                }
                val displayedProducts = if (
                    filterState.brandId == brandId && filterState.subBrand == subBrand
                ) {
                    filteredProducts
                } else {
                    allSubBrandProducts
                }

                BrandProductsScreen(
                    title = title,
                    categories = categories,
                    allSubBrandProducts = allSubBrandProducts,
                    products = displayedProducts,
                    filterState = filterState,
                    cartItems = cartItems,
                    notifyProductIds = notifyProductIds,
                    etaMinutes = storeConfig.etaMinutes,
                    onBackClick = { navController.popBackStack() },
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onCategoryChipClick = viewModel::setCategoryWithinCurrentFilter,
                    onOpenFilterSheet = { showFilterSheet = true },
                    onProductClick = { prod ->
                        navController.navigate(QuickKartRoutes.productDetail(prod.id))
                    },
                    onAddClick = { prod, vid -> viewModel.addOrIncrementCart(prod, vid) },
                    onIncrementClick = { prod, vid -> viewModel.addOrIncrementCart(prod, vid) },
                    onDecrementClick = { prod, vid -> viewModel.decrementCart(prod.id, vid) },
                    onNotifyClick = { prod -> viewModel.toggleNotifyMe(prod.id, prod.name) },
                    brands = brands,
                    modifier = standardScreenModifier
                )
            }

            composable(
                route = QuickKartRoutes.SUBCATEGORY_PRODUCTS,
                arguments = listOf(
                    navArgument("shopCategory") { type = NavType.StringType },
                    navArgument("subCategory") { type = NavType.StringType }
                )
            ) { entry ->
                val shopCategory = Uri.decode(entry.arguments?.getString("shopCategory").orEmpty())
                val rawSubCategory = entry.arguments?.getString("subCategory").orEmpty()
                val initialSub = if (rawSubCategory == "__ALL__") null else Uri.decode(rawSubCategory)
                val leafChips = remember(initialSub) { ShopCategoryTaxonomy.leafFiltersFor(initialSub) }
                val useLeafStrip = initialSub != null && leafChips.isNotEmpty()
                var selectedLeaf by rememberSaveable(shopCategory, initialSub) { mutableStateOf<String?>(null) }

                val activeSub = if (useLeafStrip) {
                    initialSub
                } else if (filterState.shopCategory == shopCategory) {
                    filterState.subCategory
                } else {
                    initialSub
                }
                val title = if (useLeafStrip) {
                    selectedLeaf ?: initialSub!!
                } else {
                    activeSub ?: shopCategory
                }
                val group = ShopCategoryTaxonomy.findByTitle(shopCategory)

                val allGroupProducts = products.filter { it.shopCategory == shopCategory }
                val baseProducts = if (filterState.shopCategory == shopCategory) {
                    if (useLeafStrip && filterState.subCategory != initialSub) {
                        filteredProducts.filter { it.subCategory == initialSub }
                    } else {
                        filteredProducts
                    }
                } else {
                    allGroupProducts.filter { activeSub == null || it.subCategory == activeSub }
                }
                val displayedProducts = remember(baseProducts, useLeafStrip, selectedLeaf) {
                    if (!useLeafStrip || selectedLeaf == null) {
                        baseProducts
                    } else {
                        baseProducts.filter { ShopCategoryTaxonomy.matchesLeafFilter(it, selectedLeaf!!) }
                    }
                }

                BrandProductsScreen(
                    title = title,
                    categories = categories,
                    allSubBrandProducts = allGroupProducts,
                    products = displayedProducts,
                    filterState = filterState,
                    cartItems = cartItems,
                    notifyProductIds = notifyProductIds,
                    etaMinutes = storeConfig.etaMinutes,
                    onBackClick = { navController.popBackStack() },
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onCategoryChipClick = {},
                    onOpenFilterSheet = { showFilterSheet = true },
                    onProductClick = { prod ->
                        navController.navigate(QuickKartRoutes.productDetail(prod.id))
                    },
                    onAddClick = { prod, vid -> viewModel.addOrIncrementCart(prod, vid) },
                    onIncrementClick = { prod, vid -> viewModel.addOrIncrementCart(prod, vid) },
                    onDecrementClick = { prod, vid -> viewModel.decrementCart(prod.id, vid) },
                    onNotifyClick = { prod -> viewModel.toggleNotifyMe(prod.id, prod.name) },
                    brands = brands,
                    customChipLabels = if (useLeafStrip) null else (group?.subcategories ?: emptyList()),
                    selectedCustomChip = if (useLeafStrip) null else activeSub,
                    onCustomChipClick = if (useLeafStrip) {
                        null
                    } else {
                        { clickedSub ->
                            if (clickedSub != null && ShopCategoryTaxonomy.leafFiltersFor(clickedSub).isNotEmpty()) {
                                viewModel.selectShopCategoryFilter(shopCategory, clickedSub)
                                navController.navigate(QuickKartRoutes.subCategoryProducts(shopCategory, clickedSub))
                            } else {
                                viewModel.setSubCategoryWithinCurrentFilter(clickedSub)
                            }
                        }
                    },
                    leafChipLabels = if (useLeafStrip) leafChips else null,
                    selectedLeafChip = selectedLeaf,
                    onLeafChipClick = if (useLeafStrip) {
                        { clicked -> selectedLeaf = if (selectedLeaf == clicked) null else clicked }
                    } else null,
                    modifier = standardScreenModifier
                )
            }

            composable(
                route = QuickKartRoutes.PRODUCT_DETAIL,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { entry ->
                val productId = entry.arguments?.getString("productId")
                val product = products.firstOrNull { it.id == productId }
                if (product == null) {
                    Box(standardScreenModifier, contentAlignment = Alignment.Center) {
                        Text("Product not found")
                    }
                } else {
                    val productBrand = brands.firstOrNull { it.id == product.brandId }
                    val sameSubBrandCount = remember(products, product.brandId, product.subBrand) {
                        if (product.subBrand.isBlank()) 0
                        else products.count { it.brandId == product.brandId && it.subBrand == product.subBrand }
                    }
                    val targetSubBrand = if (sameSubBrandCount > 1) product.subBrand else null
                    val seeMoreBrandLabel = targetSubBrand
                        ?: productBrand?.name
                        ?: product.subBrand.ifBlank { "Brand" }

                    ProductDetailScreen(
                        product = product,
                        brand = productBrand,
                        category = categories.firstOrNull { it.id == product.categoryId },
                        cartItems = cartItems,
                        totalCartCount = cartSummary.totalCount,
                        isNotified = product.id in notifyProductIds,
                        isSaved = product.id in savedProductIds,
                        etaMinutes = storeConfig.etaMinutes,
                        seeMoreBrandLabel = seeMoreBrandLabel,
                        onBackClick = { navController.popBackStack() },
                        onSearchClick = {
                            viewModel.resetFiltersCompletely()
                            navController.navigate(QuickKartRoutes.CATALOG)
                        },
                        onCartClick = { navigateTopTab(QuickKartRoutes.CART) },
                        onSeeMoreBrandClick = {
                            viewModel.selectSubBrandFilter(product.brandId, targetSubBrand)
                            navController.navigate(QuickKartRoutes.brandProducts(product.brandId, targetSubBrand))
                        },
                        onSaveForLaterClick = { viewModel.toggleSaveForLater(product.id, product.name) },
                        onAddClick = { vid -> viewModel.addOrIncrementCart(product, vid) },
                        onIncrementClick = { vid -> viewModel.addOrIncrementCart(product, vid) },
                        onDecrementClick = { vid -> viewModel.decrementCart(product.id, vid) },
                        onNotifyClick = { viewModel.toggleNotifyMe(product.id, product.name) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            composable(QuickKartRoutes.ORDERS) {
                OrdersScreen(
                    orders = orders,
                    onOrderAgain = { order ->
                        viewModel.orderAgain(order) {
                            navigateTopTab(QuickKartRoutes.CART)
                        }
                    },
                    onBrowseGroceries = { navigateTopTab(QuickKartRoutes.HOME) },
                    modifier = standardScreenModifier
                )
            }

            composable(QuickKartRoutes.CART) {
                CartScreen(
                    cartSummary = cartSummary,
                    storeConfig = storeConfig,
                    onIncrement = { pid, vid ->
                        products.firstOrNull { it.id == pid }?.let { prod ->
                            viewModel.addOrIncrementCart(prod, vid)
                        }
                    },
                    onDecrement = { pid, vid -> viewModel.decrementCart(pid, vid) },
                    onClearCart = viewModel::clearCart,
                    onBrowseProducts = { navigateTopTab(QuickKartRoutes.CATEGORIES) },
                    onCheckoutClick = {
                        viewModel.clearLastPlacedOrder()
                        navController.navigate(QuickKartRoutes.CHECKOUT)
                    },
                    modifier = standardScreenModifier
                )
            }

            composable(QuickKartRoutes.CHECKOUT) {
                CheckoutScreen(
                    cartSummary = cartSummary,
                    addresses = addresses,
                    deliverySlots = deliverySlots,
                    userProfile = userProfile,
                    isProcessing = isProcessingOrder,
                    placedOrder = lastPlacedOrder,
                    onBackClick = {
                        viewModel.clearLastPlacedOrder()
                        navController.popBackStack()
                    },
                    onAddAddress = viewModel::saveAddress,
                    onPlaceOrder = { addr, slot, method ->
                        viewModel.placeOrder(addr, slot, method) {}
                    },
                    onViewOrdersAfterSuccess = {
                        viewModel.clearLastPlacedOrder()
                        navController.navigate(QuickKartRoutes.ORDERS) {
                            popUpTo(QuickKartRoutes.HOME) { saveState = true }
                            launchSingleTop = true
                        }
                    },
                    onContinueShoppingAfterSuccess = {
                        viewModel.clearLastPlacedOrder()
                        navigateTopTab(QuickKartRoutes.HOME)
                    },
                    modifier = standardScreenModifier
                )
            }

            composable(QuickKartRoutes.ACCOUNT) {
                val savedProducts = remember(products, savedProductIds) {
                    products.filter { it.id in savedProductIds }
                }
                AccountScreen(
                    userProfile = userProfile,
                    addresses = addresses,
                    onUpdateProfile = viewModel::updateProfile,
                    onTopUpWallet = viewModel::topUpWallet,
                    onSaveAddress = viewModel::saveAddress,
                    onSetDefaultAddress = viewModel::setDefaultAddress,
                    onDeleteAddress = viewModel::deleteAddress,
                    onOpenAdminPanel = { navController.navigate(QuickKartRoutes.ADMIN) },
                    onLogout = viewModel::logout,
                    savedProducts = savedProducts,
                    onOpenSavedProduct = { prod ->
                        navController.navigate(QuickKartRoutes.productDetail(prod.id))
                    },
                    onRemoveSavedProduct = { prod ->
                        viewModel.toggleSaveForLater(prod.id, prod.name)
                    },
                    modifier = standardScreenModifier
                )
            }

            composable(QuickKartRoutes.ADMIN) {
                AdminScreen(
                    products = products,
                    categories = categories,
                    brands = brands,
                    orders = orders,
                    deliverySlots = deliverySlots,
                    storeConfig = storeConfig,
                    onExitAdmin = { navController.popBackStack() },
                    onSaveProduct = viewModel::adminSaveProduct,
                    onDeleteProduct = viewModel::adminDeleteProduct,
                    onSaveCategory = viewModel::adminSaveCategory,
                    onDeleteCategory = viewModel::adminDeleteCategory,
                    onSaveBrand = viewModel::adminSaveBrand,
                    onDeleteBrand = viewModel::adminDeleteBrand,
                    onUpdateOrderStatus = viewModel::adminUpdateOrderStatus,
                    onSaveStoreConfig = viewModel::adminSaveStoreConfig,
                    onSaveDeliverySlot = viewModel::adminSaveDeliverySlot,
                    onDeleteDeliverySlot = viewModel::adminDeleteDeliverySlot,
                    modifier = standardScreenModifier
                )
            }
        }
    }

    if (showFilterSheet) {
        FilterSortBottomSheet(
            currentFilter = filterState,
            brands = brands,
            packSizes = availablePackSizes,
            onApply = { updated ->
                viewModel.applyFilterState(updated)
                showFilterSheet = false
            },
            onDismiss = { showFilterSheet = false }
        )
    }

    if (showAddressPickerSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddressPickerSheet = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Choose Delivery Location", style = MaterialTheme.typography.titleLarge)
                    TextButton(onClick = {
                        showAddressPickerSheet = false
                        showNewAddressDialogFromPicker = true
                    }) {
                        Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Add New", color = QuickGreen)
                    }
                }
                addresses.forEach { addr ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (addr.isDefault) QuickGreenContainer else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (addr.isDefault) QuickGreen else MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.setDefaultAddress(addr.id)
                                showAddressPickerSheet = false
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = QuickGreen)
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(addr.label, style = MaterialTheme.typography.titleSmall)
                                Text(addr.shortSummary, style = MaterialTheme.typography.bodySmall, color = QuickMuted)
                            }
                            if (addr.isDefault) {
                                Icon(Icons.Rounded.CheckCircle, contentDescription = "Active", tint = QuickGreen)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
            }
        }
    }

    if (showNewAddressDialogFromPicker) {
        AddEditAddressDialog(
            initial = null,
            defaultPhone = userProfile.phone.ifEmpty { "9876543210" },
            defaultName = userProfile.name,
            onDismiss = { showNewAddressDialogFromPicker = false },
            onSave = { newAddr ->
                viewModel.saveAddress(newAddr)
                showNewAddressDialogFromPicker = false
            }
        )
    }
}
```
