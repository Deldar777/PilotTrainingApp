package nl.shekho.videoplayer.helpers.dependencyInjection

import android.app.Application
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import nl.shekho.videoplayer.api.LogBookMapper
import nl.shekho.videoplayer.api.SessionMapper
import nl.shekho.videoplayer.api.UserMapper
import nl.shekho.videoplayer.api.SessionPropertiesMapper
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.helpers.MetaDataReader

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun providerVideoPlayer(app: Application): Player {
        return SimpleExoPlayer.Builder(app).build()
    }

    @Provides
    @ViewModelScoped
    fun providerMetaDataReader(app: Application): MetaDataReader {
        return MetaDataReader(app)
    }

    @Provides
    @ViewModelScoped
    fun providerConnectivityChecker(app: Application): ConnectivityChecker {
        return ConnectivityChecker(app)
    }

    @Provides
    @ViewModelScoped
    fun providerSessionMapper(): SessionMapper {
        return SessionMapper()
    }

    @Provides
    @ViewModelScoped
    fun providerUserMapper(): UserMapper {
        return UserMapper()
    }

    @Provides
    @ViewModelScoped
    fun providerVideoMapper(): SessionPropertiesMapper {
        return SessionPropertiesMapper()
    }

    @Provides
    @ViewModelScoped
    fun providerLogBookMapper(): LogBookMapper {
        return LogBookMapper()
    }
}