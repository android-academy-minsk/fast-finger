package by.android.academy.minsk.fastfinger.ads

import retrofit2.http.GET

//TODO(11): Coroutines integration
interface AdsApi {
    @GET("advertisement")
    suspend fun getAdvertisement(): String
}

