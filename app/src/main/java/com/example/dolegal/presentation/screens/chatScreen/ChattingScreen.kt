package com.example.dolegal.presentation.screens.chatScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.ContextCompat.startActivity
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory


class ChattingScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Load the ID of the channel you've opened
        val channelId = "team:adminchat"

        setContent {
            ChatTheme {
                MessagesScreen(
                    viewModelFactory = MessagesViewModelFactory(
                        context = this,
                        channelId = channelId
                    ),
                    onBackPressed = {finish()}
                )
            }
        }
    }

    companion object {

        private const val CHANNEL_ID = "channel_id"

        fun getChannelId(context: Context, channelID: String) {
            val intent = Intent(context,ChattingScreen::class.java)
            intent.putExtra(CHANNEL_ID,channelID)
            startActivity(context,intent,null)
        }


    }
}


