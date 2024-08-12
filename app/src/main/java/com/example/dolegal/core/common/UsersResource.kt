package com.example.dolegal.core.common

sealed class UsersResource <T>(val user:T?= null, val message:String?=null){

    class Success<T> (user:T):UsersResource<T>(user)
    class Error<T> (message:String,user:T?=null):UsersResource<T>(user,message)
    class Loading<T> :UsersResource<T>()

}