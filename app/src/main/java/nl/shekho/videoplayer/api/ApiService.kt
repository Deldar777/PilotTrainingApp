package nl.shekho.videoplayer.api

import nl.shekho.videoplayer.models.JwtToken
import nl.shekho.videoplayer.models.LoginUser
import nl.shekho.videoplayer.models.Session
import nl.shekho.videoplayer.models.User
import retrofit2.Response
import retrofit2.http.*


interface ApiService {


    @POST("LoginUser")
    suspend fun login(
        @Body user: LoginUser
    ): Response<JwtToken>

    @FormUrlEncoded
    @POST("GetSessionByCompanyId/{companyId}")
    suspend fun getSessionsByCompanyId(
        @Path("companyId") companyId: Int,
    )


    @FormUrlEncoded
    @POST("CreateSession")
    suspend fun createSession(
        @Field("UserIds") userIds: List<String>,
        @Field("CompanyId") companyId: String
    ): Response<Session>



}