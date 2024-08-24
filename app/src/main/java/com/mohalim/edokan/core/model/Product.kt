package com.mohalim.edokan.core.model

import com.google.firebase.firestore.Exclude
import java.math.BigInteger

data class Product(
    val productId: String = "",
    val productName: String = "",
    val productDescription: String = "",
    val productImage: String = "",
    val productPrice: Double = 0.0,
    val productDiscount: Double = 0.0,
    val productWeight: Double = 0.0,
    val productLength: Double = 0.0,
    val productWidth: Double = 0.0,
    val productHeight: Double = 0.0,
    val marketPlaceId: String = "",
    val marketPlaceName: String = "",
    val marketPlaceLat: Double = 0.0,
    val marketPlaceLng: Double = 0.0,
    val productQuantity: Double = 0.0,
    val productStatus: Boolean = false,
    val dateAdded: Double = 0.0,
    val dateModified: Double = 0.0,
    @Exclude val errorMessage: String? = null,
    @Exclude val isLoading: Boolean = false
)
