package com.example.dolegal.presentation.models

data class TabItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)

data class BannerItem(
    val url: String
)

data class Story(
    val name: String,
    val url: String,
){
    constructor():this("", "")
}

data class Users(
    val name:String,
    val email:String,
    val password:String,
    val score:String,
    val profile_pic:String,
    val sID:String,
    val token:String,
){
    constructor() : this("", "", "","0","","","")
}

data class StoryBanner(

    val name:String,
    val image_url:String,
    val story_url:String,

){
    constructor():this("","","")
}

data class StoryCategory(

    val name:String,
    val image_url:String,

    ){
    constructor():this("","")
}


data class BottomNavigationItems(
    val title:String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
)

data class Achievement(
    val title:String,
    val image_url:String,

){
    constructor():this("","")
}