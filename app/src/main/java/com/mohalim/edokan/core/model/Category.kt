package com.mohalim.edokan.core.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("category_name_ar") val categoryNameAr: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("parent_id") val parentId: String,
)
