package com.rounak.testapp.data.network.responses.feeds_response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FeedItem(
    val albumId: Int,
    val id: Int,
    val thumbnailUrl: String,
    val title: String,
    val url: String
): Parcelable
