package com.mohalim.edokan.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Color.parseColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.firebase.auth.FirebaseAuth
import com.mohalim.edokan.R
import com.mohalim.edokan.core.datasource.preferences.UserPreferencesRepository
import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.model.Product
import com.mohalim.edokan.core.utils.AuthUtils
import com.mohalim.edokan.core.utils.SealedSellerScreen
import com.mohalim.edokan.ui.seller.SellerAddMarketplaceActivity
import com.mohalim.edokan.ui.seller.SellerAddProductActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


@Composable
fun SellerMainUI(context: Context, viewModel: MainViewModel) {
    val navController = rememberNavController()
    val username by viewModel.username.collectAsState(initial = "")
    val marketplaces by viewModel.marketplaces.collectAsState()
    val role by viewModel.role.collectAsState(initial = "")

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, viewModel) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SealedSellerScreen.Home.route,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            composable(SealedSellerScreen.Home.route) {
                SellerHomeScreen(context = context, viewModel, username, marketplaces)
            }
            composable(SealedSellerScreen.Products.route) {
                SellerProductsScreen(context, viewModel)

            }
            composable(SealedSellerScreen.Orders.route) {
                SellerOrdersScreen()
            }

            composable(SealedSellerScreen.User.route) {
                SellerUserScreen(viewModel)
            }
        }
    }

}

@Composable
fun SellerHomeScreen(context: Context, viewModel: MainViewModel, username: String?, marketplaces: List<MarketPlace> = emptyList()) {
    var selectedMarketplace by remember { mutableStateOf<MarketPlace?>(null) }
    val selectedMarketplaceId by viewModel.selectedMarketPlaceId.collectAsState(initial = "")
    val selectedMarketplaceName by viewModel.selectedMarketPlaceName.collectAsState(initial = "")

    val cityId by viewModel.cityId.collectAsState(initial = 0)
    val phoneNumber by viewModel.phoneNumber.collectAsState(initial = "")
    

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context, SellerAddMarketplaceActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Icon(Icons.Default.AddBusiness, contentDescription = "Add Marketplace")
            }
        }

    )
    { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            Text(
                text = "Welcome back! " + username,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )


            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

            if (selectedMarketplaceId.isNullOrEmpty()) {
                /**
                 * No selected Marketplace so show lazy column
                 */

                MarketplaceSelectionList(
                    marketplaces = marketplaces,
                    selectedMarketplace = selectedMarketplace,
                    onMarketplaceSelected = { marketplace ->
                        viewModel.setSelectedMarketplace(marketplace)
                    }
                )
            } else {
                /**
                 * there is selected marketplace
                 */

                Text(text = "Selected Marketplace", style = MaterialTheme.typography.h6)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                        .clickable {
                            viewModel.setSelectedMarketplace(MarketPlace())
                            viewModel.fetchApprovedMarketPlaces(cityId ?: 0, phoneNumber ?: "")
                        },
                    backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.2f),
                    elevation = 4.dp
                ) {

                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = selectedMarketplaceName ?: "",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }

            }

            }}







    }
}

@Composable
fun MarketplaceSelectionList(
    marketplaces: List<MarketPlace>,
    selectedMarketplace: MarketPlace?,
    onMarketplaceSelected: (MarketPlace) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Choose a Marketplace", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)) {
            items(marketplaces) { marketplace ->
                MarketplaceItem(
                    marketplace = marketplace,
                    isSelected = marketplace == selectedMarketplace,
                    onClick = { onMarketplaceSelected(marketplace) }
                )
            }
        }
    }
}

@Composable
fun MarketplaceItem(
    marketplace: MarketPlace,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        backgroundColor = if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.2f) else MaterialTheme.colors.surface,
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Store,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = marketplace.marketplaceName,
                style = MaterialTheme.typography.body1,
                color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            )
        }
    }
}


@Composable
fun SellerProductsScreen(context: Context, viewModel: MainViewModel) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context, SellerAddProductActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Icon(Icons.Default.AddShoppingCart, contentDescription = "Add Marketplace")
            }
        }

    )
    { innerPadding ->
        var searchQuery by remember { mutableStateOf("") }
        val products by viewModel.products.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchProducts(it)
                },
                label = { Text("Search Products") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductItem(product)
                }
            }
        }




    }
}

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                    .data(product.productImage)
                    .size(Size.ORIGINAL)
                    .build()
                ),
                contentDescription = product.productName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = product.productDescription,
                    style = MaterialTheme.typography.body2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$${product.productPrice}",
                        style = MaterialTheme.typography.subtitle2,
                        color = Color.Green,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Qty: ${product.productQuantity}",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}


@Composable
fun SellerOrdersScreen() {
    Text(text = "Order Screen")
}

@Composable
fun SellerUserScreen(viewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "User Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))


            IconButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    AuthUtils.signOut(viewModel.auth)
                    coroutineScope.launch {
                        viewModel.userPreferencesRepository.clearUserDetails()
                        viewModel.userSelectionPreferencesRepository.clearSelectedMarketplace()
                    }

                }) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Log out")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Log out")
                }
            }

    }
}

@Composable
fun BottomNavigationBar(navController: NavController, viewModel: MainViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val selectedMarketplaceId by viewModel.selectedMarketPlaceId.collectAsState(initial = "")


    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        containerColor = Color(parseColor("#f6192a")),
        contentColor = Color.Red
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val items = listOf(
            SealedSellerScreen.Home,
            SealedSellerScreen.Products,
            SealedSellerScreen.Orders,
            SealedSellerScreen.User,
        )

        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        if(screen.route == SealedSellerScreen.Products.route && selectedMarketplaceId.isNullOrEmpty()){
                            showDialog = true
                        }else if(screen.route == SealedSellerScreen.Orders.route && selectedMarketplaceId.isNullOrEmpty()){
                            showDialog = true
                        } else{
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                label = { Text(text = screen.label, color = Color.White) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(parseColor("#FFC107")),
                    indicatorColor = Color(parseColor("#CE0F1E")),
                    unselectedIconColor = Color.LightGray
                ),
            )
        }
    }


    // Dialog that shows when showDialog is true
    DialogWithImage(
        showDialog = showDialog,
        onDismissRequest = { showDialog = false },
        onConfirmation = { showDialog = false },
        painter = painterResource(id = R.drawable.error_icon),
        imageDescription = "Error Image",
        dialogText = "Error: Please select a marketplace first"
    )
}


