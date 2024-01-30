object Dependencies {
    // Kotlin
    val kotlin by lazy { "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}" }
    val kotlinxSerialization by lazy { "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerialization}" }

    val retrofit by lazy { "com.squareup.retrofit2:retrofit:${Versions.retrofit}" }
    val okhttp3 by lazy { "com.squareup.okhttp3:okhttp:${Versions.okhttp3}" }
    val kotlinxSerializationRetrofit by lazy { "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.kotlinxSerializationRetrofit}" }

    // Android
    val coreKtx by lazy { "androidx.core:core-ktx:${Versions.coreKtx}" }
    val lifecycleRuntimeKtx by lazy { "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}" }
    val hilt by lazy { "com.google.dagger:hilt-android:${Versions.hilt}" }
    val hiltCompiler by lazy { "com.google.dagger:hilt-android-compiler:${Versions.hilt}" }
    val timber by lazy { "com.jakewharton.timber:timber:${Versions.timber}" }
    val datastore by lazy { "androidx.datastore:datastore-preferences:${Versions.datastore}" }

    // Compose
    val composeActivity by lazy { "androidx.activity:activity-compose:${Versions.composeActivity}" }
    val composeViewModel by lazy { "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.composeViewModel}" }
    val material by lazy { "androidx.compose.material3:material3:${Versions.material3}" }
    val composeUi by lazy { "androidx.compose.ui:ui:${Versions.compose}" }
    val composeNavigation by lazy { "androidx.navigation:navigation-compose:${Versions.composeNavigation}" }
    val composeNavigationHilt by lazy { "androidx.hilt:hilt-navigation-compose:${Versions.composeNavigationHilt}" }
    val composeUiToolingPreview by lazy { "androidx.compose.ui:ui-tooling-preview:${Versions.compose}" }
    val composeUiTooling by lazy { "androidx.compose.ui:ui-tooling:${Versions.compose}" }
    val composeUiTestManifest by lazy { "androidx.compose.ui:ui-test-manifest:${Versions.compose}" }

    // Tools
    val coil by lazy { "io.coil-kt:coil-compose:${Versions.coil}" }

    // Testing
    val junit by lazy { "junit:junit:${Versions.junit}" }
    val androidxJunit by lazy { "androidx.test.ext:junit:${Versions.androidxJunit}" }
    val espressoCore by lazy { "androidx.test.espresso:espresso-core:${Versions.espressoCore}" }
    val composeUiTest by lazy { "androidx.compose.ui:ui-test-junit4:${Versions.compose}" }
}