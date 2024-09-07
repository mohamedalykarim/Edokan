package com.mohalim.edokan.core.model

import com.google.gson.annotations.SerializedName

data class MarketPlace(
    @SerializedName("marketplace_id") var marketplaceId: Int = 0,
    @SerializedName("marketplace_name") var marketplaceName: String = "",
    @SerializedName("lat") var lat: Double = 0.0,
    @SerializedName("lng") var lng: Double = 0.0,
    @SerializedName("city_id") var cityId: Int = 0,
    @SerializedName("city_name") var city: String = "",
    @SerializedName("marketplace_owner_id") var marketplaceOwnerId: String = "",
    @SerializedName("is_approved") var isApproved: Int = 0
)