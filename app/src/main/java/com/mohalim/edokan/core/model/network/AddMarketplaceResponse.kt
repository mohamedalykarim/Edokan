package com.mohalim.edokan.core.model.network

import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.model.User

data class AddMarketplaceResponse(
    val message: String,
    val result: MarketPlace
)