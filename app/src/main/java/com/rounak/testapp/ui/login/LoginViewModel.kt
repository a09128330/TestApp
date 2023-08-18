package com.rounak.testapp.ui.login

import android.util.Log
import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rounak.testapp.data.repository.AppRepository
import com.rounak.testapp.other_models.LoginResponse
import com.rounak.testapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel(), Observable {
    @Bindable
    val inputEmail = MutableStateFlow("")   //edit text with dual data binding

    @Bindable
    val inputPwd = MutableStateFlow("")   //edit text with dual data binding

    var isUserRemembered: Boolean = false

    private val _emailErrorStateFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    val emailErrorStateFlow: StateFlow<String?> = _emailErrorStateFlow.asStateFlow()

    private val _pwdErrorStateFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    val pwdErrorStateFlow: StateFlow<String?> = _pwdErrorStateFlow.asStateFlow()

    private val loginStatusChannel = Channel<Response<LoginResponse>>()
    val loginStatusFlowForChannel = loginStatusChannel.receiveAsFlow().flowOn(Dispatchers.Main.immediate) //cold flow


    internal fun getUserRememberSelection():Boolean = repository.getUserRememberSelection()

    internal fun loginUser() {
        viewModelScope.launch(Dispatchers.IO) {
            loginStatusChannel.send(Response.Loading())
            Log.d("onCreate", "current thread background =>: ${Thread.currentThread().name}")
            val email: String = inputEmail.value
            val password: String = inputPwd.value

            val emailValid:Boolean = if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _emailErrorStateFlow.value = "Invalid Email"
                Log.d("onCreate", "current thread background =>: ${Thread.currentThread().name}")
                false
            } else {
                Log.d("onCreate", "current thread background =>: ${Thread.currentThread().name}")
                _emailErrorStateFlow.value = null //clear email error
                true
            }

            val pwdValid:Boolean = if (password.isBlank() || password.length < 6) {
                _pwdErrorStateFlow.value = "Password must be at least 6 characters"
                Log.d("onCreate", "current thread background =>: ${Thread.currentThread().name}")
                false
            } else {
                Log.d("onCreate", "current thread background =>: ${Thread.currentThread().name}")
                _pwdErrorStateFlow.value = null // clear pwd error
                true
            }


            if(!emailValid || !pwdValid){
                loginStatusChannel.send(Response.Error(errorMessage = "Please check your email and password !",errorCode = null))
                return@launch
            }

            // all valid input

            //save remember status
            Log.d("onCreate", "current thread background =>: ${Thread.currentThread().name}")
            repository.saveUserRememberSelection(isUserRemembered)

            Log.d("onCreate", "current thread background =>: ${Thread.currentThread().name}")
            loginStatusChannel.send(Response.Success(LoginResponse(remember = isUserRemembered,isLoggedIn = true)))
        }
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}