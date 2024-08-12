package com.example.dolegal.presentation.state

import com.example.dolegal.presentation.models.Users

data class CurrentUserState(

    val user: Users? = Users("Data not Fetched","","","","","",""),
    val isLoading: Boolean = false,
    val error: String = ""

)