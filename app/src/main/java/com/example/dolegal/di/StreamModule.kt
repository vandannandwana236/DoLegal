package com.example.dolegal.di

import android.content.Context
import com.example.dolegal.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.models.UploadAttachmentsNetworkType
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StreamModule {

    @Provides
    fun provideOfflinePluginFactory(@ApplicationContext context: Context): StreamOfflinePluginFactory {
        return StreamOfflinePluginFactory(context)
    }

    @Provides
    fun providesStatePluginFactory(@ApplicationContext context: Context): StreamStatePluginFactory {
        return StreamStatePluginFactory(
            appContext = context,
            config = StatePluginConfig(backgroundSyncEnabled = true, userPresence = true),
        )
    }

    @Provides
    @Singleton
    fun providesChatClient(@ApplicationContext context: Context,offlinePluginFactory: StreamOfflinePluginFactory,streamStatePluginFactory: StreamStatePluginFactory):ChatClient{
        return ChatClient.Builder(context.getString(R.string.api_key), context)
            .withPlugins(streamStatePluginFactory, offlinePluginFactory)
            .logLevel(ChatLogLevel.ALL)
            .uploadAttachmentsNetworkType(
                UploadAttachmentsNetworkType.NOT_ROAMING
            )
            .build()
    }

}