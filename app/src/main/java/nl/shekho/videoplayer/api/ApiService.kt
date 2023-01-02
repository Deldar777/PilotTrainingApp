package nl.shekho.videoplayer.api

import nl.shekho.videoplayer.api.entities.*
import nl.shekho.videoplayer.models.Asset
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.models.JwtToken
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

    @POST("PostVideo")
    suspend fun postVideo(
        @Body body: VideoRequestEntity,
        @Header("Authorization") token: String?
    ):Response<VideoResponseEntity>

    @GET("Logbooks/get-single/{logbookId}")
    suspend fun getLogBookById(
        @Path("logbookId") logBookId: String,
        @Header("Authorization") token: String?
    ): Response<LogBookEntity>

    @GET("GetUserBySessionId/{sessionId}")
    suspend fun getUsersBySessionId(
        @Path("sessionId") sessionId: String,
        @Header("Authorization") token: String?
    ): Response<UsersEntity>

    //Create event
    @POST("Events/create")
    suspend fun createEvent(
        @Body body: EventRequestEntity,
        @Header("Authorization") token: String?
    ):Response<Event>

    //Update event
    @PUT("Events/update-single/{eventId}")
    suspend fun updateEvent(
        @Path("eventId") eventId: String,
        @Body body: EventRequestEntity,
        @Header("Authorization") token: String?
    ):Response<Event>

    @GET("GetUserBySessionId/{sessionId}")
    suspend fun updateSessionStatusById(
        @Path("sessionId") sessionId: String,
        @Header("Authorization") token: String?
    ): Response<String>
}
