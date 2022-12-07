package nl.shekho.videoplayer.helpers.dependencyInjection

import android.app.Application
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import nl.shekho.videoplayer.api.SessionMapper
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.helpers.MetaDataReader

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun providerVideoPlayer(app: Application): Player{
        return ExoPlayer.Builder(app)
            .build()
    }

    @Provides
    @ViewModelScoped
    fun providerMetaDataReader(app: Application): MetaDataReader{
        return MetaDataReader(app)
    }

    @Provides
    @ViewModelScoped
    fun providerConnectivityChecker(app: Application): ConnectivityChecker{
        return ConnectivityChecker(app)
    }

    @Provides
    @ViewModelScoped
    fun providerSessionMapper(): SessionMapper{
        return SessionMapper()
    }
}