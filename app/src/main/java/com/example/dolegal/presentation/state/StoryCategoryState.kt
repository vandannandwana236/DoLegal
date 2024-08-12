package com.example.dolegal.presentation.state

import com.example.dolegal.presentation.models.StoryBanner

data class StoryCategoryState(
    val categories: List<StoryBanner>? = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = ""
    )
