package com.rounak.testapp.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.rounak.testapp.constants.Constant
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CustomSharedPreference @Inject constructor (@ApplicationContext context: Context){
    private val appContext = context.applicationContext

    fun saveUserRememberSelection(isRememberSelected: Boolean) {
        val sharedPreferences: SharedPreferences =
            appContext.getSharedPreferences(Constant.SHARED_PREF_ID, Application.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(Constant.IS_REMEMBER, isRememberSelected)
        editor.apply()
    }



    fun getUserRememberSelection(): Boolean {
        val getSharedPrefs: SharedPreferences =
            appContext.getSharedPreferences(Constant.SHARED_PREF_ID, Application.MODE_PRIVATE)
        return getSharedPrefs.getBoolean(Constant.IS_REMEMBER, false)
    }





}