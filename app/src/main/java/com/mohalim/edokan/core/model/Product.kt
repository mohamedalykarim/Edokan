package com.mohalim.edokan.core.model

import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class Product(
    @SerializedName("product_id") val productId: Int = 0,
    @SerializedName("product_name") val productName: String = "",
    @SerializedName("product_description") val productDescription: String = "",
    @SerializedName("product_image_url") val productImageUrl: String = "",
    @SerializedName("product_image1_url") val productImage1Url: String = "",
    @SerializedName("product_image2_url") val productImage2Url: String = "",
    @SerializedName("product_image3_url") val productImage3Url: String = "",
    @SerializedName("product_image4_url") val productImage4Url: String = "",
    @SerializedName("product_price") val productPrice: Double = 0.0,
    @SerializedName("product_discount") val productDiscount: Double = 0.0,
    @SerializedName("product_weight") val productWeight: Double = 0.0,
    @SerializedName("product_length") val productLength: Double = 0.0,
    @SerializedName("product_width") val productWidth: Double = 0.0,
    @SerializedName("product_height") val productHeight: Double = 0.0,
    @SerializedName("marketplace_id") val marketPlaceId: Int = 0,
    @SerializedName("marketplace_name") val marketPlaceName: String = "",
    @SerializedName("marketplace_lat") val marketPlaceLat: Double = 0.0,
    @SerializedName("marketplace_lng") val marketPlaceLng: Double = 0.0,
    @SerializedName("product_quantity") val productQuantity: Double = 0.0,
    @SerializedName("product_status") val productStatus: Int = 1,
    @SerializedName("is_global") val isGlobal: Int = 0,
    @SerializedName("product_owner_id") val productOwnerId: String = "",
    @SerializedName("date_added") val dateAdded: Double = 0.0,
    @SerializedName("date_modified") val dateModified: Double = 0.0,
)
