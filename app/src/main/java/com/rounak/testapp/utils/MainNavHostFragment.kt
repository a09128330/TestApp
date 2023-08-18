package com.rounak.testapp.utils

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import com.rounak.testapp.fragment_factory.MainFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainNavHostFragment : NavHostFragment(){
    @Inject
    lateinit var mainFactory: MainFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager.fragmentFactory = mainFactory
    }
}