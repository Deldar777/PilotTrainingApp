package nl.shekho.videoplayer.api

import nl.shekho.videoplayer.api.entities.*
import nl.shekho.videoplayer.models.Asset
import retrofit2.Response
import retrofit2.http.*


interface ApiMediaService {

    // Asset ive streaming endpoints

    // 1-Create empty asset
//    @Headers(
//        "Accept-Encoding: gzip",
//        "Content-Type: text/plain; charset=utf-8",
//        "Transfer-Encoding: chunked"
//    )
    @POST("CreateEmptyAsset")
    suspend fun createEmptyAsset(
        @Header("Authorization") token: String?,
        @Body body: AssetRequestBodyEntity,
    ):Response<Asset>


}
