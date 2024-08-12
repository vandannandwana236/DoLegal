package com.example.dolegal.core.common

sealed class StoryResource<S>(val story:S? = null,val message:String? = null){

    class Success<S>(story:S):StoryResource<S>(story)
    class Error<S>(message:String,story:S? = null):StoryResource<S>(story,message)
    class Loading<S>:StoryResource<S>()

}