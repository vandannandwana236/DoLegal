package com.example.dolegal.presentation.state

import com.example.dolegal.presentation.models.Story

data class StoryStates (

    val stories:List<Story>? = emptyList(),
    val errorMessage:String? = "",
    val isLoading:Boolean = false

)