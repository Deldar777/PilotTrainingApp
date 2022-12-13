package nl.shekho.videoplayer.api

import nl.shekho.videoplayer.api.entities.*
import nl.shekho.videoplayer.models.Asset
import retrofit2.Response
import retrofit2.http.*


interface ApiMediaService {

    // Asset ive streaming endpoints


//    @Headers(
//        "Accept-Encoding: gzip",
//        "Accept: application/json",
//        "Content-Type: text/plain; charset=utf-8",
//        "Transfer-Encoding: chunked"
//    )

    // 1-Create empty asset
    @POST("CreateEmptyAsset")
    suspend fun createEmptyAsset(
        @Header("Authorization") token: String?,
        @Body body: AssetRequestBodyEntity,
    ):Response<Asset>

    // 2-Publish asset
    @POST("PublishAsset")
    suspend fun publishAsset(
        @Header("Authorization") token: String?,
        @Body body: AssetRequestPublishedEntity,
    ):Response<nl.shekho.videoplayer.models.Streaming>

    // 3-Create live event
    @POST("CreateLiveEvent")
    suspend fun createLiveEvent(
        @Header("Authorization") token: String?,
        @Body body: LiveEventRequestEntity,
    ):Response<LiveEventResponseEntity>

    // 5-Update live event
    @POST("UpdateLiveEvents")
    suspend fun updateLiveEvent(
        @Header("Authorization") token: String?,
        @Body body: LiveEventUpdateEntity,
    ):Response<LiveEventResponseEntity>
}
