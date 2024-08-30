package com.mohalim.edokan.core.model.network

import com.google.gson.annotations.SerializedName

data class ProductByOwnerRequest(
    @SerializedName("query_text") var searchQuery: String,
    var limit: Long,
    @SerializedName("owner_id") var ownerId: String
)
