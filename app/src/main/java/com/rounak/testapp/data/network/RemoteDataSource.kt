package com.rounak.testapp.data.network

import com.rounak.testapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RemoteDataSource @Inject constructor()  {

    private fun getRetrofitClient(): OkHttpClient {

        //Create OkHttp Client
        val okHttp = OkHttpClient.Builder()
            .callTimeout(
                15,
                TimeUnit.SECONDS
            ) //best practice is to reduce timeout and show retry on failure


        if (BuildConfig.DEBUG) {
            okHttp.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))//logcat->verbose->okhttp  (debug process)
        }

        return okHttp.build()
    }





    fun <T> buildService(serviceType: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getRetrofitClient())  //without 401 refresh
            .build()
            .create(serviceType)
    }


}