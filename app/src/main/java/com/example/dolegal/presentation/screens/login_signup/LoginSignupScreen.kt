package com.example.dolegal.presentation.screens.login_signup

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.dolegal.R
import com.example.dolegal.presentation.viewmodel.HomeViewModel
import com.example.dolegal.presentation.models.TabItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginSignup(
    innerPadding: PaddingValues,
    lifecycleOwner: LifecycleOwner,
    context: Context,
    vModel: HomeViewModel,
    onLoginClick: () -> Unit,
    modifier: Modifier
) {

    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes (R.raw.doraemon))

    val logintabItems = listOf(

        TabItem(
            title = "Login",
            selectedIcon = R.drawable.login_filled,
            unselectedIcon = R.drawable.login_outlined
        ),
        TabItem(
            title = "Signup",
            selectedIcon = R.drawable.signup_filled,
            unselectedIcon = R.drawable.signup_outlined
        )

    )
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState {
        logintabItems.size
    }

    val imInset = WindowInsets.ime.getBottom(LocalDensity.current)
    Log.d("Vandan", imInset.toString())

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF00A5E3))
    ) {
        if (imInset <= 0) {
            Column(modifier = modifier.padding(innerPadding)) {

                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = modifier
                        .padding(start = 90.dp, end = 90.dp, top = 20.dp, bottom = 10.dp)
                        .clip(RoundedCornerShape(60.dp)),
                    indicator = {
                        TabRowDefaults.PrimaryIndicator(color = Color.Transparent);TabRowDefaults.SecondaryIndicator(
                        color = Color.Transparent
                    )
                    }
                ) {

                    logintabItems.forEachIndexed { index, item ->

                        Tab(modifier = modifier.background(Color(0xCD8DD7BF)),
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(
                                        index,
                                        animationSpec = tween(1200)
                                    )
                                }
                            },
                            text = { Text(text = item.title) },
                            selectedContentColor = Color(0xFF365349),
                            unselectedContentColor = Color(0x50082C28),
                            icon = {
                                Icon(
                                    painter = painterResource(
                                        id = if (pagerState.currentPage == index) {
                                            item.selectedIcon
                                        } else item.unselectedIcon
                                    ), modifier = modifier.size(16.dp),
                                    contentDescription = "home"
                                )
                            }
                        )
                    }
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth(),
            pageSpacing = 10.dp
        ) { index ->

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = if (imInset <= 0) Alignment.Center else Alignment.TopCenter
            ) {
                if (index == 0) {
                    LoginPage(
                        modifier = modifier,
                        context = context,
                        vModel = vModel,
                        composition = composition,
                        isLoading = isLoading,
                        isLoadingChange = {isLoading = it},
                        onLoginClick = onLoginClick,
                        imInset = imInset
                    )
                } else {
                    SignUpPage(
                        modifier = modifier,
                        context = context,
                        vModel = vModel,
                        composition = composition,
                        isLoading = isLoading,
                        isLoadingChange = {isLoading = it},
                        onLoginClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0, animationSpec = tween(1200))
                        }
                    }, imInset =  imInset)
                }

            }

        }
    }

}

