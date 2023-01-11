package nl.shekho.videoplayer.api

import nl.shekho.videoplayer.api.entities.*
import nl.shekho.videoplayer.models.Asset
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.models.JwtToken
import nl.shekho.videoplayer.models.Record
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

    //Post video and create logbook
    @POST("PostVideo")
    suspend fun postVideo(
        @Body body: VideoRequestEntity,
        @Header("Authorization") token: String?
    ):Response<VideoResponseEntity>

    //Update HLS url to the posted video
    @PUT("EditVideoDetails")
    suspend fun editVideoDetails(
        @Body body: VideoDetailsEntity,
        @Header("Authorization") token: String?
    ):Response<Void>

    //Get video by session id to fetch video URL and events that belongs to a logbook
    @GET("GetVideoBySessionId/{sessionId}")
    suspend fun getVideoBySessionId(
        @Path("sessionId") sessionId: String,
        @Header("Authorization") token: String?
    ): Response<List<VideoResponseEntity>>

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

    //Create event
    @POST("Records/create")
    suspend fun createRecord(
        @Body body: RecordEntity,
        @Header("Authorization") token: String?
    ):Response<Record>

    //Update event
    @PUT("Events/update-single/{eventId}")
    suspend fun updateEvent(
        @Path("eventId") eventId: String,
        @Body body: EventRequestEntity,
        @Header("Authorization") token: String?
    ):Response<Event>

    @PUT("UpdateSessionStatusById/{sessionId}")
    suspend fun updateSessionStatusById(
        @Path("sessionId") sessionId: String,
        @Header("Authorization") token: String?
    ): Response<Void>
}
