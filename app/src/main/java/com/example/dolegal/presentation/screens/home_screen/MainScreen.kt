package com.example.dolegal.presentation.screens.home_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.dolegal.R
import com.example.dolegal.presentation.models.BottomNavigationItems
import com.example.dolegal.presentation.models.StoryBanner
import com.example.dolegal.presentation.screens.home_screen.coponents.StoryCategories
import com.example.dolegal.presentation.state.CurrentUserState
import com.example.dolegal.presentation.state.StoryCategoryState
import com.example.dolegal.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, DelicateCoroutinesApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen(
    innerPadding: PaddingValues = PaddingValues(16.dp),
    viewModel: HomeViewModel,
    context: Context,
    onLogoutClick: () -> Unit,
    lifecycleOwner: LifecycleOwner,
    onCategoryClick: (category: String) -> Unit,
    onStoryClick: (String) -> Unit,
    modifier: Modifier
) {
    var selectedItemIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState {
        3
    }

    val currentUserState = viewModel.currentUser.collectAsStateWithLifecycle(lifecycleOwner).value

    LaunchedEffect(selectedItemIndex) {
        pagerState.scrollToPage(selectedItemIndex)
    }
    LaunchedEffect(pagerState) {
        if (pagerState.currentPage != selectedItemIndex) {
            selectedItemIndex = pagerState.currentPage
        }
    }

    (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = modifier,
                onClick = {
                    GlobalScope.launch {
                        selectedItemIndex = it
                    }
                }, pagerState.currentPage
            )
        }
    ) {
        Column(
            modifier = modifier
                .padding(bottom = it.calculateBottomPadding())
                .background(Color(0xFF4C7BF4)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            HorizontalPager(state = pagerState, userScrollEnabled = false) { index ->
                when (index) {

                    1 -> PublicScreen(
                        modifier = modifier,
                        innerPadding = it.calculateTopPadding(),
                        viewModel = viewModel,
                        lifecycleOwner = lifecycleOwner,
                    )

                    2 -> ProfileScreen(
                        viewModel,
                        onLogoutClick,
                        currentUserState,
                        modifier = modifier
                    )

                    else -> HomeScreen(
                        modifier = modifier,
                        currentUserState,
                        context,
                        lifecycleOwner,
                        innerPadding,
                        viewModel,
                        onCategoryClick,
                        onStoryClick
                    )

                }

            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(
    modifier: Modifier,
    currentUserState: CurrentUserState,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    innerPadding: PaddingValues = PaddingValues(16.dp),
    viewModel: HomeViewModel,
    onCategoryClick: (category: String) -> Unit,
    onStoryClick: (String) -> Unit
) {
    val storyBanners = viewModel.banners.collectAsStateWithLifecycle(lifecycleOwner).value

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(R.drawable.do_legal_logo_notext),
                    contentDescription = "myLogo",
                    modifier = modifier
                        .padding(14.dp)
                        .clip(CircleShape)
                        .size(64.dp)
                )
                if (currentUserState.user != null) {
                    Row {
                        Text(
                            text = "Hello ",
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = FontFamily(Font(R.font.story_categoryy))
                        )

                        Text(
                            text = currentUserState.user.name,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = FontFamily(Font(R.font.story_categoryy))
                        )

                    }


                    AsyncImage(
                        placeholder = painterResource(id = R.drawable.loading_placeholder),
                        model = currentUserState.user.profile_pic,
                        contentDescription = "myLogo",
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .padding(14.dp)
                            .clip(CircleShape)
                            .size(64.dp)
                            .clickable {
                                Log.d("Vandan_Current_User", currentUserState.user.toString())
//                                viewModel.connectUserInStream(
//                                    context = context,
//                                    currentUserState.user.sID,
//                                    currentUserState.user.name,
//                                    currentUserState.user.token
//                                )
                            }
                    )
                }

            }

        }

        item {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                text = "Today's Top Stories",
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xE8272727),
                fontSize = 37.sp,
                fontFamily = FontFamily(Font(R.font.story_category))
            )
        }

        item {
            BannerList(modifier, storyBanners, onStoryClick)

        }

        item {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                text = "Stories Categories",
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xE8272727),
                fontSize = 37.sp,
                fontFamily = FontFamily(Font(R.font.story_category))
            )
        }

        item {

            StoryCategories(onCategoryClick)

        }

    }

}

@Composable
fun BannerList(
    modifier: Modifier,
    storyCategories: StoryCategoryState,
    onStoryClick: (String) -> Unit
) {

    if (storyCategories.isLoading) {
        CircularProgressIndicator()
    } else if (storyCategories.errorMessage?.isEmpty() != true) {
        Text(text = storyCategories.errorMessage.toString())
    }
    if (storyCategories.categories?.isNotEmpty() == true) {
        LazyRow {
            items(storyCategories.categories) { item ->

                Banner(modifier, item, onStoryClick)

            }
        }

    }


}

@Composable
fun Banner(modifier: Modifier, banner: StoryBanner, onStoryClick: (String) -> Unit) {
    Box(
        modifier = modifier
            .padding(24.dp)
            .height(290.dp)
            .width(250.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        AsyncImage(
            placeholder = painterResource(id = R.drawable.loading_placeholder),
            model = banner.image_url,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = modifier
                .fillMaxSize()
                .clickable {
                    onStoryClick(banner.story_url)
                }
        )
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun BottomNavigation(modifier: Modifier, onClick: (index: Int) -> Unit, pagerState: Int) {
    val navigationItems = listOf(
        BottomNavigationItems(
            title = "home",
            selectedIcon = R.drawable.story_filled,
            unselectedIcon = R.drawable.story_outlined
        ),
        BottomNavigationItems(
            title = "public",
            selectedIcon = R.drawable.leaderboard_filled,
            unselectedIcon = R.drawable.leaderboard_outlined
        ),
        BottomNavigationItems(
            title = "profile",
            selectedIcon = R.drawable.profile_filled,
            unselectedIcon = R.drawable.profile_outlined
        )
    )

    NavigationBar(
        containerColor = Color(0xB942665A)
    ) {
        navigationItems.forEachIndexed { index, item ->

            NavigationBarItem(
                selected = pagerState == index,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFFFF96C5),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Black
                ),
                onClick = { onClick(index) },
                label = { Text(item.title) },
                icon = {
                    Icon(
                        painter = painterResource(id = if (pagerState == index) item.selectedIcon else item.unselectedIcon),
                        tint = Color.White,
                        modifier = modifier.size(24.dp),
                        contentDescription = "bottom"
                    )
                })

        }
    }


}

