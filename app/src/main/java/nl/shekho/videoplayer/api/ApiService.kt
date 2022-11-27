package nl.shekho.videoplayer.api


import nl.shekho.videoplayer.models.AuthToken
import nl.shekho.videoplayer.models.JwtToken
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ApiService {

    @FormUrlEncoded
    @POST("LoginUser")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<JwtToken>

    @FormUrlEncoded
    @POST("api/Users/login")
    suspend fun logoin(
        @Field("UserName") UserName: String,
        @Field("Password") Password: String
    ): Response<AuthToken>
}