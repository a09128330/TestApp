package com.rounak.testapp.ui.feed_detail

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rounak.testapp.data.db.entities.FavouriteFeed
import com.rounak.testapp.data.network.responses.feeds_response.FeedItem
import com.rounak.testapp.data.repository.AppRepository
import com.rounak.testapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedDetailViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel(), Observable {

    var feedItemPassed: FeedItem? = null

    var isFavouriteFromDB:Boolean?=null

    private val _stateFlow: MutableStateFlow<Response<Boolean>> = MutableStateFlow(Response.Loading())
    val stateFlow: StateFlow<Response<Boolean>> = _stateFlow.asStateFlow()


    internal fun markFeedFavouriteStatus(){
        viewModelScope.launch {
            isFavouriteFromDB = !(isFavouriteFromDB!!)
            val favouriteFeed = FavouriteFeed(
                id=feedItemPassed!!.id.toLong(),
                albumId = feedItemPassed!!.albumId.toLong(),
                title = feedItemPassed!!.title,
                url=feedItemPassed!!.url,
                thumbnailUrl = feedItemPassed!!.thumbnailUrl,
                isFavourite = isFavouriteFromDB!!
            )
            repository.markFeedFavouriteStatus(favouriteFeed)
            //notify isFavouriteFromDB to ui
            notifyFavouriteStatusToUI()
            return@launch
        }
    }


    internal fun loadFavouriteFeedDetail(id:Long){
        viewModelScope.launch(Dispatchers.IO) {
            if(isFavouriteFromDB==null){
                val favouriteFeed:FavouriteFeed? = repository.getFavouriteFeed(id)

                if(favouriteFeed==null){
                    isFavouriteFromDB = false
                }else{
                    isFavouriteFromDB = favouriteFeed.isFavourite
                }
                //notify isFavouriteFromDB to ui
                notifyFavouriteStatusToUI()
                return@launch
            }else{
                //notify isFavouriteFromDB to ui
                notifyFavouriteStatusToUI()
                return@launch
            }

        }
    }

    private fun notifyFavouriteStatusToUI() {
        _stateFlow.value = Response.Success(isFavouriteFromDB!!)
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}