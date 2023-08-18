package com.rounak.testapp.data.repository


import android.util.Log
import com.rounak.testapp.data.db.AppDao
import com.rounak.testapp.data.db.entities.FavouriteFeed
import com.rounak.testapp.data.network.RetrofitService
import com.rounak.testapp.data.network.responses.feeds_response.FeedItem
import com.rounak.testapp.utils.CustomSharedPreference
import com.rounak.testapp.utils.InternetConnection
import com.rounak.testapp.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val dao: AppDao,
    private val api: RetrofitService,
    private val internetConnection: InternetConnection,
    private val customSharedPreference: CustomSharedPreference
){


    suspend fun markFeedFavouriteStatus(favouriteFeed: FavouriteFeed){
        dao.insertOrUpdateFavouriteFeed(favouriteFeed)
    }
    internal fun getFavouriteFeed(id:Long): FavouriteFeed? = dao.getFavouriteFeed(id)

    internal fun saveUserRememberSelection(
        isRememberSelected: Boolean
    ){
        customSharedPreference.saveUserRememberSelection(isRememberSelected)
    }


    internal fun getUserRememberSelection(): Boolean
    = customSharedPreference.getUserRememberSelection()



    private fun isInternetAvailable():Boolean = internetConnection.isNetworkAvailable()

    suspend fun getFeeds(): Flow<Response<List<FeedItem>>> =
        flow {
            if (!isInternetAvailable()) {
                emit(Response.Error(errorMessage = "No Internet",errorCode = null))
            } else {
                val response = api.getFeeds()
                emit(handleFeedsResponse(response))
            }
        }.catch { exception ->
            exception.printStackTrace()
            Log.d("channel collection", "channel collection process 1: ")
            emit(Response.Error(errorMessage = exception.localizedMessage ?: "Unknown Error",errorCode = null))
        }.flowOn(Dispatchers.IO)



    private fun handleFeedsResponse(response: retrofit2.Response<ResponseBody>): Response<List<FeedItem>> =
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->

                val successResponse = resultResponse.string()
                Log.d("onCreate", "handleFeedsResponse Success: $successResponse")

                val feedsJSONArray:JSONArray = JSONArray(successResponse)
                Log.d("onCreate", "handleFeedsResponse Success json: $feedsJSONArray")

                val listOfFeeds = arrayListOf<FeedItem>()  //empty list created

                for (i in 0 until feedsJSONArray.length()) {
                    val albumId = feedsJSONArray.getJSONObject(i).getInt("albumId")
                    val id = feedsJSONArray.getJSONObject(i).getInt("id")
                    val title = feedsJSONArray.getJSONObject(i).getString("title")
                    val url = feedsJSONArray.getJSONObject(i).getString("url")
                    val thumbnailUrl = feedsJSONArray.getJSONObject(i).getString("thumbnailUrl")

                    val feedItem = FeedItem(
                        albumId=albumId,
                        id=id,
                        thumbnailUrl = thumbnailUrl,
                        title=title,
                        url=url
                    )
                    listOfFeeds.add(feedItem)
                }
                return@let Response.Success(listOfFeeds)
            } ?: Response.Error(errorMessage = "An unknown error occurred",errorCode = response.code())
        } else {
            val errorResponse: String = response.errorBody()?.string() ?: "Unknown Error"
            Log.d("onCreate", "handleFeedsResponse Error: $errorResponse")
            Response.Error(errorMessage = "4XX or 5XX or any other Api Error Occurred",errorCode = response.code())
        }
}