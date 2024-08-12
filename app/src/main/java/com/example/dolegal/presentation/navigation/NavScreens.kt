package com.example.dolegal.presentation.navigation

sealed class NavScreens(val route:String) {

    data object HomeScreen:NavScreens("home")
    data object LoginScreen:NavScreens("login")
    data object StoryScreen:NavScreens("story")
    data object StoryPLayerScreen:NavScreens("story_player")

}