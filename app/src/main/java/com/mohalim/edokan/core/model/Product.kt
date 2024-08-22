package com.mohalim.edokan.core.model

import java.math.BigInteger

data class Product(
    var productId : Int,
    var productName : String,
    var productDescription : String,
    var productImage : String,
    var productPrice : Float,
    var productDiscount : Float,
    var productWeight : Double,
    var productLength : Double,
    var productWidth : Double,
    var productHeight : Double,
    var marketPlaceId : Int,
    var marketPlaceName : String,
    var marketPlaceLat : Double,
    var marketPlaceLng: Double,
    var productQuantity : Int,
    var productStatus : Int,
    var dateAdded : BigInteger,
    var dateModified : BigInteger
)
