package com.rounak.testapp.utils

sealed class Response<T>(
    val successData:T?=null,
    val errorMessage:String?=null,
    val errorCode:Int?=null
){
    class Success<T>(successData:T):Response<T>(successData = successData)
    class Error<T>(errorMessage:String,errorCode:Int?):Response<T>(errorMessage = errorMessage, errorCode = errorCode)
    class Loading<T>:Response<T>()
}
