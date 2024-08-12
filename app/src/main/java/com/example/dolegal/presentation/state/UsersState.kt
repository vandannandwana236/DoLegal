package com.example.dolegal.presentation.state

import com.example.dolegal.presentation.models.Users

data class UsersState(

    val users:List<Users>? = emptyList(),
    val errorMessage:String? = "",
    val isLoading:Boolean = false

)