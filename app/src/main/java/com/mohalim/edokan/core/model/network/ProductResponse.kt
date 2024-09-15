package com.mohalim.edokan.core.model.network

import com.mohalim.edokan.core.model.Product

data class ProductResponse(
    val message: String,
    val result: List<Product>
)