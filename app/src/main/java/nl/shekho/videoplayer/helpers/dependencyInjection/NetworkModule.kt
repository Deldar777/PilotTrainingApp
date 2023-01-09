package nl.shekho.videoplayer.helpers.dependencyInjection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nl.shekho.videoplayer.api.ApiMediaService
import nl.shekho.videoplayer.api.ApiService
import nl.shekho.videoplayer.config.ApiConfig
import nl.shekho.videoplayer.config.ApiConfig.BASE_URL
import nl.shekho.videoplayer.config.ApiConfig.MEDIA_SERVICE_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Provides
    fun provideMediaServicesBaseUrl() = MEDIA_SERVICE_BASE_URL

    @Singleton
    @Provides
    @Named("ApiService")
    fun provideApiServiceRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    @Singleton
    @Provides
    @Named("ApiMediaService")
    fun provideApiMediaServiceRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(MEDIA_SERVICE_BASE_URL)
        .build()

    @Singleton
    @Provides
    fun provideApiService(@Named("ApiService") retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideApiMediaService(@Named("ApiMediaService") retrofit: Retrofit): ApiMediaService =
        retrofit.create(ApiMediaService::class.java)
}