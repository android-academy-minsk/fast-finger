package by.android.academy.minsk.fastfinger.ads

class AdsUseCase(private val api: AdsApi) {
    //!!!it's the FINISHED project, switch search to start module!!! TODO(12): ads business logic. Return ShowAdsResult using api.
    suspend fun showAds(): ShowAdsResult {
        return try {
            val response = api.getAdvertisement()
            ShowAdsResult.ShowLoadedAdvertisement(response)
        } catch (t: Throwable) {
            ShowAdsResult.ShowAdsLoadingError
        }
    }
}

sealed class ShowAdsResult {
    data class ShowLoadedAdvertisement(val text: String) : ShowAdsResult()
    object ShowAdsLoadingError : ShowAdsResult()
}