package by.android.academy.minsk.fastfinger.ads

import retrofit2.http.GET

//!!!it's the FINISHED project, switch search to start module!!! TODO(11): Coroutines integration
interface AdsApi {
    @GET("advertisement")
    suspend fun getAdvertisement(): String
}

