package com.example.dolegal.presentation.screens.home_screen

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.dolegal.R
import com.example.dolegal.presentation.models.Story
import com.example.dolegal.presentation.viewmodel.HomeViewModel

@Composable
fun StoryScreen(
    viewModel: HomeViewModel,
    innerPadding: PaddingValues,
    category: String?,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    onStoryClick: (storyID: String) -> Unit,
    modifier: Modifier.Companion
) {

    (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    val storyState = viewModel.stories.collectAsStateWithLifecycle(lifecycleOwner).value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00A5E3)), contentAlignment = Alignment.Center
    ) {
        if (storyState.isLoading) {

            CircularProgressIndicator()

        } else if (storyState.errorMessage?.isEmpty() != false) {

            Text(text = storyState.errorMessage.toString())

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            if (storyState.stories?.isNotEmpty() == true) {

                LazyColumn(modifier = Modifier.padding(innerPadding)) {
                    items(storyState.stories.size) {

                        StoryItem(storyState.stories[it], onStoryClick)

                    }
                }

            } else if (storyState.stories?.isEmpty() == true) {
                Text(text = "No Data Found", fontSize = 20.sp)
            }

        }

    }

}

@Composable
fun StoryItem(it: Story, onStoryClick: (storyID: String) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(7.dp))
    ) {

        Column(modifier = Modifier.background(Color.White)) {

            AsyncImage(
                placeholder = painterResource(id = R.drawable.loading_placeholder),
                model = it.url,
                contentScale = ContentScale.Crop,
                contentDescription = "story_image",
                modifier = Modifier
                    .clickable { onStoryClick("5yAaZjKy3iM") }
                    .fillMaxWidth()
            )
            Text(text = it.name, Modifier.padding(2.dp))

        }

    }


}
