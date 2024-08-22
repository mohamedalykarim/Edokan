package com.mohalim.edokan.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class SealedSellerScreen(val route: String, val label: String, val icon: ImageVector) {
    object Home : SealedSellerScreen(route = "home", label = "Home", icon = Icons.Default.Home)
    object Products : SealedSellerScreen("products", label = "Products", icon = Icons.Default.Menu)
    object Orders : SealedSellerScreen("orders", label = "Orders", icon = Icons.Default.ShoppingCart)
    object User : SealedSellerScreen("user", label = "User", icon = Icons.Default.AccountCircle)
}