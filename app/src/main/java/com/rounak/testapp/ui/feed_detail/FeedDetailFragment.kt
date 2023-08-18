package com.rounak.testapp.ui.feed_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rounak.testapp.R
import com.rounak.testapp.data.network.responses.feeds_response.FeedItem
import com.rounak.testapp.databinding.FragmentFeedDetailBinding
import com.rounak.testapp.utils.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedDetailFragment(
    var viewModel: FeedDetailViewModel? = null
) : Fragment() {
    private lateinit var binding: FragmentFeedDetailBinding
    private lateinit var feedItemPassed: FeedItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        feedItemPassed = requireArguments().getParcelable("feedItem")!!
        Log.d("onCreate", "feedItemPassed: $feedItemPassed")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed_detail, container, false)
        setDataBinding()
        storeFeedItemPassedInViewModel()
        createCollectors()
        setOnClicks()
        loadDetailOnUi()
        loadFavouriteFeedDetail()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun loadFavouriteFeedDetail() {
        viewModel!!.loadFavouriteFeedDetail(viewModel!!.feedItemPassed!!.id.toLong())
    }

    private fun loadDetailOnUi() {
        binding.feedItem = viewModel!!.feedItemPassed
    }


    private fun storeFeedItemPassedInViewModel() {
        if (viewModel!!.feedItemPassed == null) {
            viewModel!!.feedItemPassed = feedItemPassed
        }
    }

    private fun setOnClicks() {
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.heartImgView.setOnClickListener {
            viewModel!!.markFeedFavouriteStatus()
        }
    }

    private fun setDataBinding() {
        viewModel = viewModel ?: ViewModelProvider(this)[FeedDetailViewModel::class.java]
        binding.feedDetailViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun createCollectors() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main.immediate) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel!!.stateFlow.collectLatest { response: Response<Boolean> ->  //collect can also be used based on requirement
                        //update ui
                        when (response) {
                            is Response.Success -> {
                                val isFavourite: Boolean = response.successData!!
                                if (isFavourite) {
                                    binding.heartImgView.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            requireActivity(),
                                            R.drawable.ic_favourite
                                        )
                                    )
                                } else {
                                    binding.heartImgView.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            requireActivity(),
                                            R.drawable.ic_un_favourite
                                        )
                                    )
                                }
                                hideFavouriteUI(false)
                            }

                            is Response.Error -> {
                                // no operation
                            }

                            is Response.Loading -> {
                                hideFavouriteUI(true)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hideFavouriteUI(b: Boolean) {
        if (b) {
            binding.heartImgView.visibility = View.INVISIBLE
        } else {
            binding.heartImgView.visibility = View.VISIBLE
        }

    }


}