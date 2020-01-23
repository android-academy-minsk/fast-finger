package by.android.academy.minsk.fastfinger.ads

import retrofit2.http.GET

interface AdsApi {
    @GET("advertisement")
    suspend fun getAdvertisement(): String
}

