package com.mohalim.edokan.core.model.network

import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.model.User

data class MarketplacesResponse(
    val message: String,
    val result: List<MarketPlace>
)



