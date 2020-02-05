package by.android.academy.minsk.fastfinger.ads

import by.android.academy.minsk.fastfinger.REST_SERVER_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createAdsUseCase() = AdsUseCase(createAdsApi())

private fun createAdsApi(): AdsApi {
    val okHttpClient = OkHttpClient.Builder()
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(REST_SERVER_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(AdsApi::class.java)
}