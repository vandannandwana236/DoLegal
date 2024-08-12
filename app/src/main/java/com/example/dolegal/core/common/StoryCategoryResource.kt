package com.example.dolegal.core.common

sealed class StoryCategoryResource<R>(val storyCategory:R?= null, val message:String?=null){

    class Success<R> (storyCategory:R):StoryCategoryResource<R>(storyCategory)
    class Error<R> (message:String,storyCategory: R?=null):StoryCategoryResource<R>(storyCategory,message)
    class Loading<R> :StoryCategoryResource<R>()

}