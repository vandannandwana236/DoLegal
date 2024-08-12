package com.example.dolegal.core.common

sealed class CurrentUserResource<T>(val user:T? = null,val message:String?= null){

    class Success<T> (user:T):CurrentUserResource<T>(user)
    class Error<T> (message:String,user:T? = null):CurrentUserResource<T>(user,message)
    class Loading<T> :CurrentUserResource<T>()

}