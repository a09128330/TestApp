package com.rounak.testapp.data.network
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitService {
    @GET("photos")
    suspend fun getFeeds(): Response<ResponseBody>
}