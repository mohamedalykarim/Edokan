package com.mohalim.edokan.core.model

data class MarketPlace(
    var marketplaceId : Int,
    var marketplaceName : String,
    var lat: Double,
    var lng : Double,
    var cityId : Int,
    var marketplaceOwnerId : String
)