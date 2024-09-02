package com.mohalim.edokan.core.model.network

import com.mohalim.edokan.core.model.Category
import com.mohalim.edokan.core.model.User

data class CategoriesResponse(
    val message: String,
    val result: List<Category>
)