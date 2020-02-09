package by.android.academy.minsk.fastfinger.ads

class AdsUseCase(private val api: AdsApi) {
    suspend fun showAds(): ShowAdsResult {
        //TODO(12): ads business logic. Return ShowAdsResult using api.
        TODO()
    }
}

sealed class ShowAdsResult {
    data class ShowLoadedAdvertisement(val text: String) : ShowAdsResult()
    object ShowAdsLoadingError : ShowAdsResult()
}