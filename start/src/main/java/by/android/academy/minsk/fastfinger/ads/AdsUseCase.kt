package by.android.academy.minsk.fastfinger.ads

class AdsUseCase(private val api: AdsApi) {
    //TODO(12): ads business logic. Return ShowAdsResult using api.
    suspend fun showAds(): ShowAdsResult {
        TODO()
    }
}

sealed class ShowAdsResult {
    data class ShowLoadedAdvertisement(val text: String) : ShowAdsResult()
    object ShowAdsLoadingError : ShowAdsResult()
}