package com.example.dolegal.presentation.screens.chatScreen

import androidx.activity.compose.setContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dolegal.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.channels.ChannelViewModelFactory
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.querysort.QuerySortByField

@AndroidEntryPoint
class ChatsListScreen : ComponentActivity() {

    private val factory by lazy {

        ChannelViewModelFactory(
            chatClient = ChatClient.instance(),
            querySort = QuerySortByField.descByName("last_updated"),
            filters = Filters.`in`(
                fieldName = "type",
                values = listOf("messaging","team","gaming")
            )
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val viewModel = viewModel(HomeViewModel::class.java)
            ChatTheme{
                ChannelsScreen(
                    viewModelFactory = factory,
                    title = "Channel List",
                    isShowingSearch = true,
                    onItemClick = {
                        val id = "${it.type}:${it.id}"
                                  ChattingScreen.getChannelId(this,id)
                    },
                    onBackPressed = {
                        finish()
                        viewModel.logoutStream()
                    }
                )
            }
        }

    }

}