package com.mohalim.edokan.core.model

import com.google.firebase.firestore.Exclude
import java.math.BigInteger

data class Product(
    val productId: Int = 0,
    val productName: String = "",
    val productDescription: String = "",
    val productImageUrl: String = "",
    val productImage1Url: String = "",
    val productImage2Url: String = "",
    val productImage3Url: String = "",
    val productImage4Url: String = "",
    val productPrice: Double = 0.0,
    val productDiscount: Double = 0.0,
    val productWeight: Double = 0.0,
    val productLength: Double = 0.0,
    val productWidth: Double = 0.0,
    val productHeight: Double = 0.0,
    val marketPlaceId: Int = 0,
    val marketPlaceName: String = "",
    val marketPlaceLat: Double = 0.0,
    val marketPlaceLng: Double = 0.0,
    val productQuantity: Double = 0.0,
    val productStatus: Int = 1,
    val dateAdded: Double = 0.0,
    val dateModified: Double = 0.0,
)
