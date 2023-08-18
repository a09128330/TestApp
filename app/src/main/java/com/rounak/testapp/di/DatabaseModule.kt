package com.rounak.testapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import android.content.Context
import androidx.room.Room
import com.rounak.testapp.constants.Constant
import com.rounak.testapp.data.db.AppDao
import com.rounak.testapp.data.db.AppDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    //for local db starts
    @Singleton
    @Provides
    fun providesAppDao(@Named("app_db") appDatabase: AppDatabase): AppDao = appDatabase.appDao()

    @Singleton
    @Provides
    @Named("app_db")
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase
            = Room.databaseBuilder(context, AppDatabase::class.java, Constant.DB_NAME).build()
    //for local db ends
}