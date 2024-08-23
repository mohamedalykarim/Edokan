package com.mohalim.edokan.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Color.parseColor
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.utils.SealedSellerScreen
import com.mohalim.edokan.ui.seller.SellerAddMarketplaceActivity


@Composable
fun SellerMainUI(context: Context, viewModel: MainViewModel) {
    val navController = rememberNavController()
    val username by viewModel.username.collectAsState(initial = "")
    val marketplaces by viewModel.marketplaces.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
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
                SellerProductsScreen()
            }
            composable(SealedSellerScreen.Orders.route) {
                SellerOrdersScreen()
            }

            composable(SealedSellerScreen.User.route) {
                SellerUserScreen()
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
                Icon(Icons.Default.Add, contentDescription = "Add Marketplace")
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

            Text(text = "Welcome back! "+ username, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)

            if (selectedMarketplaceId.isNullOrEmpty()){
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
            }else{
                /**
                 * there is selected marketplace
                 */

                Text(text = "Selected Marketplace", style = MaterialTheme.typography.h6)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
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
                        Text(
                            text = selectedMarketplaceName ?: "",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }

            }





        }






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
            Text(
                text = marketplace.marketplaceName,
                style = MaterialTheme.typography.body1,
                color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            )
        }
    }
}


@Composable
fun SellerProductsScreen() {
    Text(text = "Product Screen")
}

@Composable
fun SellerOrdersScreen() {
    Text(text = "Order Screen")
}

@Composable
fun SellerUserScreen() {
    Text(text = "User Screen")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
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
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
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
}

