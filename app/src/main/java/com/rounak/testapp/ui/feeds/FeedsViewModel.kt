package com.rounak.testapp.ui.feeds

import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rounak.testapp.data.network.responses.feeds_response.FeedItem
import com.rounak.testapp.data.repository.AppRepository
import com.rounak.testapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FeedsViewModel @Inject constructor(
    private val repository: AppRepository
): ViewModel(), Observable {
    private val _stateFlow: MutableStateFlow<Response<List<FeedItem>>> = MutableStateFlow(Response.Loading())
    val stateFlow: StateFlow<Response<List<FeedItem>>> = _stateFlow.asStateFlow()

    private val channel = Channel<String>()
    val flowForChannel = channel.receiveAsFlow() //cold flow

    init {
        getFeeds()
    }

    private fun getFeeds(){
        viewModelScope.launch(Dispatchers.Main.immediate) {
            repository.getFeeds().collectLatest { response:Response<List<FeedItem>> ->
                when (response) {
                    is Response.Success -> {
                        val feedList:List<FeedItem> = response.successData!!
                        _stateFlow.value = Response.Success(feedList)
                    }
                    is Response.Error -> {
                        val error = response.errorMessage!!
                        val errorCode:Int? = response.errorCode
                        _stateFlow.value = Response.Error(errorMessage = error,errorCode = errorCode)
                        Log.d("channel collection", "channel collection process 2: ")
                        channel.send(error)
                    }
                    is Response.Loading -> {
                        /*NO-OP */
                    }
                }
            }
        }
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}