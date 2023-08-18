package com.rounak.testapp.ui.feeds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rounak.testapp.R
import com.rounak.testapp.data.network.responses.feeds_response.FeedItem
import com.rounak.testapp.databinding.FragmentFeedsBinding
import com.rounak.testapp.ui.feeds.adapter.FeedListAdapter
import com.rounak.testapp.utils.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class FeedsFragment(
    var viewModel: FeedsViewModel? = null
) : Fragment() {

    @Inject
    lateinit var feedListAdapter: FeedListAdapter
    private lateinit var binding:FragmentFeedsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        feedListAdapter.setOnItemClickListener { feedItem: FeedItem, position: Int ->
            listItemClicked(
                feedItem = feedItem,
                position = position
            )
        }
        feedListAdapter.setContext(requireActivity())
        feedListAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feeds, container, false)
        setDataBinding()
        initRv()
        createCollectors()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showLoader(b: Boolean) {
        binding.progBar.isVisible = b
    }

    private fun createCollectors() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main.immediate) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel!!.stateFlow.collectLatest { response: Response<List<FeedItem>> ->  //collect can also be used based on requirement
                        //update ui
                        when (response) {
                            is Response.Success -> {
                                val feedList:List<FeedItem> = response.successData!!
                                feedListAdapter.differ.submitList(feedList)
                                showLoader(false)
                            }
                            is Response.Error -> {
                                val error = response.errorMessage!!
                                val errorCode:Int? = response.errorCode
                                Log.d("onCreate", "error: $error")
                                Log.d("onCreate", "errorCode: $errorCode")
                                showLoader(false)
                            }
                            is Response.Loading -> {
                                showLoader(true)
                            }
                        }
                    }
                }
                launch {
                    viewModel!!.flowForChannel.collectLatest { error ->  //collect can also be used based on requirement
                        Log.d("channel collection", "channel collection process 3: ")
                        Snackbar.make(
                            binding.root,
                            error,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun initRv() {
        binding.feedsRv.setHasFixedSize(true)
        binding.feedsRv.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            this.adapter = this@FeedsFragment.feedListAdapter
        }
    }

    private fun setDataBinding() {
        viewModel = viewModel ?: ViewModelProvider(this)[FeedsViewModel::class.java]
        binding.feedsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    //ITEM Click on each item of the Recycler View
    private fun listItemClicked(feedItem: FeedItem, position: Int) {
        val bundle= bundleOf(
            "feedItem" to feedItem
        )
        requireParentFragment().findNavController().navigate(R.id.action_feedsFragment_to_feedDetailFragment,bundle)
    }


}