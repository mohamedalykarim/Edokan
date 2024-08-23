package com.mohalim.edokan.ui.main

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.mohalim.edokan.core.datasource.preferences.UserPreferencesRepository
import com.mohalim.edokan.core.datasource.preferences.UserSelectionPreferencesRepository
import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.utils.AuthUtils
import com.mohalim.edokan.core.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel by viewModels<MainViewModel>()

    @Inject lateinit var userPreferencesRepository: UserPreferencesRepository
    @Inject lateinit var userSelectionPreferencesRepository: UserSelectionPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val homePageState by viewModel.homePageState.collectAsState()
            val uiState by viewModel.uiState.collectAsState()


            val countDownTimer = object : CountDownTimer(2000, 1000) {
                override fun onTick(p0: Long) {}
                override fun onFinish() {
                    viewModel.setHomePageState("START")
                }
            }
            countDownTimer.start()


            when(homePageState){
                "LOADING"->{
                    SplashScreen(context = this@MainActivity, viewModel = viewModel)
                }

                "START"->{
                    viewModel.navigateBasedOnRole()
                    if (!AuthUtils.checkUserAuthentication()){
                        LoginScreen(viewModel = viewModel)
                    }else{
                        val role by viewModel.role.collectAsState(initial = "")
                        if (role != null) NavigateBasedInRole(viewModel, role!!)
                    }

                }
            }


        }
    }

    override fun onResume() {
        super.onResume()
        observeMarketplacesListStatus(this)

        lifecycleScope.launch {
            val cityId = viewModel.cityId.first()
            val marketplaceOwnerId =  viewModel.phoneNumber.first()


            if (viewModel.selectedMarketPlaceId.first().isNullOrEmpty()) {
                viewModel.fetchApprovedMarketPlaces(cityId ?: 0, marketplaceOwnerId ?: "")
            }
        }

    }

    private fun observeMarketplacesListStatus(context: Context) {
        lifecycleScope.launch {
            viewModel.marketplacesListStatus.collect{
                when(it){
                    is Resource.Loading->{
                    }

                    is Resource.Success<*> ->{
                        viewModel.setMarketplaces(it.data as List<MarketPlace>)
                    }

                    is Resource.Error<*> ->{
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    @Composable
    private fun NavigateBasedInRole(viewModel: MainViewModel, role : String) {
        Log.d("TAG", "NavigateBasedInRole: "+role)
        when (role){
            "CUSTOMER"->{
                CustomerMainUI(viewModel = viewModel)
            }
            "SELLER"->{
                Text(text = "Seller")
                SellerMainUI(this@MainActivity, viewModel = viewModel)
            }
            "DELIVERY"->{
            }

        }
    }
}