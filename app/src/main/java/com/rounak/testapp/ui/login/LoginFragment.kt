package com.rounak.testapp.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.rounak.testapp.R
import com.rounak.testapp.data.network.responses.feeds_response.FeedItem
import com.rounak.testapp.databinding.FragmentLoginBinding
import com.rounak.testapp.other_models.LoginResponse
import com.rounak.testapp.utils.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment(
    var viewModel: LoginViewModel? = null
) : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        setDataBinding()
        createCollectors()
        setOnClicks()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkRememberStatus()
    }

    private fun checkRememberStatus() {
        if(viewModel!!.getUserRememberSelection()){
            moveToFeedsScreen()
        }
    }

    private fun setOnClicks() {
        binding.loginButton.setOnClickListener {
            viewModel!!.loginUser()
        }

        binding.rememberCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel!!.isUserRemembered = isChecked
        }
    }


    private fun setDataBinding() {
        viewModel = viewModel ?: ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun createCollectors() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main.immediate) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel!!.emailErrorStateFlow.collectLatest { emailError: String? ->  //collect can also be used based on requirement
                        //update ui
                        binding.emailInputLayout.error = emailError
                    }
                }

                launch {
                    viewModel!!.pwdErrorStateFlow.collectLatest { pwdError: String? ->  //collect can also be used based on requirement
                        //update ui
                        binding.pwdInputLayout.error = pwdError
                    }
                }
                launch {
                    viewModel!!.loginStatusFlowForChannel.collectLatest { response: Response<LoginResponse> ->  //collect can also be used based on requirement
                        when (response) {
                            is Response.Success -> {
                                enableOnClicks(true)
                                Log.d(
                                    "onCreate",
                                    "current thread main =>: ${Thread.currentThread().name}"
                                )
                                val loginResponse: LoginResponse = response.successData!!
                                Log.d("onCreate", "loginResponse: $loginResponse")
                                if (loginResponse.isLoggedIn) {
                                    moveToFeedsScreen()
                                }
                            }

                            is Response.Error -> {
                                enableOnClicks(true)
                                val error = response.errorMessage!!
                                val errorCode:Int? = response.errorCode
                                Log.d("onCreate", "error: $error")
                                Log.d("onCreate", "errorCode: $errorCode")
                                Snackbar.make(
                                    binding.root,
                                    error,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }

                            is Response.Loading -> {
                                enableOnClicks(false)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun enableOnClicks(b: Boolean) {
        binding.loginButton.isClickable = b
        binding.rememberCheckBox.isClickable = b
    }

    private fun moveToFeedsScreen() {
        Log.d("onCreate", "moveToFeedsScreen: called")
        findNavController().navigate(R.id.action_loginFragment_to_feedsFragment)
    }


}