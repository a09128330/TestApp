package com.rounak.testapp.di

import com.rounak.testapp.data.network.RemoteDataSource
import com.rounak.testapp.data.network.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    //for retrofit starts
    @Singleton
    @Provides
    fun provideRetrofitService(
        remoteDataSource: RemoteDataSource
    ): RetrofitService {
        return remoteDataSource.buildService(RetrofitService::class.java)
    }
    //for retrofit ends
}