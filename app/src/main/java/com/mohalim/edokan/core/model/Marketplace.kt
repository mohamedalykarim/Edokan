package com.mohalim.edokan.core.model

data class MarketPlace(
    var marketplaceId: String = "",
    var marketplaceName: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var cityId: Int = 0,
    var city: String = "",
    var marketplaceOwnerId: String = "",
    var isApproved: Boolean = false
)