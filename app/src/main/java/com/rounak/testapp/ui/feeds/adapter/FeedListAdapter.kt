package com.rounak.testapp.ui.feeds.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rounak.testapp.R
import com.rounak.testapp.data.network.responses.feeds_response.FeedItem
import com.rounak.testapp.databinding.RvItemFeedBinding
import javax.inject.Inject


class FeedListAdapter @Inject constructor(
) : RecyclerView.Adapter<FeedListAdapter.FeedViewHolder>() {
    private lateinit var ctx: Context
    private lateinit var clickListener:(FeedItem, Int)->Unit

    internal fun setOnItemClickListener(
        itemClickListener:(FeedItem, Int)->Unit
    ){
        this.clickListener =  itemClickListener
    }

    internal fun setContext(ctx: Context){
        this.ctx = ctx
    }

    private val differCallback = object : DiffUtil.ItemCallback<FeedItem>() {
        override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : RvItemFeedBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.rv_item_feed,parent,false)
        return FeedViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(
            differ.currentList[position],
            clickListener
        )
    }




    inner class FeedViewHolder(private val binding: RvItemFeedBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(
            feedItem: FeedItem,
            clickListener:(FeedItem, Int)->Unit
        ){

            binding.feedItem = feedItem

            binding.feedItemLayout.setOnClickListener{
                clickListener(feedItem,bindingAdapterPosition)
            }

            binding.executePendingBindings() //added later
        }
    }

}

