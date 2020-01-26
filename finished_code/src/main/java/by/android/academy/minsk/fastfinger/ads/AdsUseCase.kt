package by.android.academy.minsk.fastfinger.ads

class AdsUseCase(private val api: AdsApi) {
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