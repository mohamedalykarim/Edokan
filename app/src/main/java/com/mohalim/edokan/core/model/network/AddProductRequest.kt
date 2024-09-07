package com.mohalim.edokan.core.model.network

import com.google.gson.annotations.SerializedName

data class AddProductRequest(
    @SerializedName("product_name") var productName: String,
    @SerializedName("product_description") var productDescription: String,
    @SerializedName("product_price") var productPrice: Double,
    @SerializedName("product_quantity") var productQuantity: Double,
    @SerializedName("product_width") var productWidth: Double,
    @SerializedName("product_height") var productHeight: Double,
    @SerializedName("product_weight") var productWeight: Double,
    @SerializedName("product_length") var productLength: Double,
    @SerializedName("product_discount") var productDiscount: Double,
    @SerializedName("marketplace_id") var marketplaceId : Int,
    @SerializedName("marketplace_name") var marketplaceName : String,
    @SerializedName("marketplace_lat") var marketplaceLat: Double,
    @SerializedName("marketplace_lng") var marketplaceLng: Double,
    @SerializedName("date_added") var dateAdded: Double,
    @SerializedName("date_modified") var dateModifier: Double
)
