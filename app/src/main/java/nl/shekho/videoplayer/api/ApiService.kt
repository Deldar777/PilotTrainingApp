package nl.shekho.videoplayer.api

import nl.shekho.videoplayer.api.entities.*
import nl.shekho.videoplayer.models.JwtToken
import nl.shekho.videoplayer.models.Session
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @POST("LoginUser")
    suspend fun login(
        @Body user: LoginEntity
    ): Response<JwtToken>

    @GET("GetUserByCompanyID/{CompanyId}")
    suspend fun getUsersByCompanyId(
        @Path("CompanyId") companyId: String,
        @Header("Authorization") token: String?
    ):Response<UsersEntity>

    @GET("GetUserByID/{UserId}")
    suspend fun getUserById(
        @Path("UserId") userId: String,
        @Header("Authorization") token: String?
    ):Response<OneUserEntity>

    @GET("GetSessionByUserId/{UserId}")
    suspend fun getSessionsByUserId(
        @Path("UserId") userId: String,
        @Header("Authorization") token: String?
    ): Response<List<SessionEntity>>

    @POST("CreateSession")
    suspend fun createSession(
        @Body body: NewSessionEntity,
        @Header("Authorization") token: String?
    ):Response<SessionEntity>

    @FormUrlEncoded
    @POST("GetSessionByCompanyId/{companyId}")
    suspend fun getSessionsByCompanyId(
        @Path("companyId") companyId: Int,
    )


    // Asset endpoints
    // 1-Create empty asset
    @POST("/CreateEmptyAsset")
    suspend fun createEmptyAsset(
        @Path("companyId") companyId: Int,
    )

    // Live streaming endpoints

}
