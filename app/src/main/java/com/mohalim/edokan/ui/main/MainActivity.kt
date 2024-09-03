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
import com.google.firebase.auth.FirebaseAuth
import com.mohalim.edokan.core.datasource.preferences.UserPreferencesRepository
import com.mohalim.edokan.core.datasource.preferences.UserSelectionPreferencesRepository
import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.utils.AuthUtils
import com.mohalim.edokan.core.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel by viewModels<MainViewModel>()

    @Inject lateinit var userPreferencesRepository: UserPreferencesRepository
    @Inject lateinit var userSelectionPreferencesRepository: UserSelectionPreferencesRepository
    @Inject lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val role by viewModel.role.collectAsState(initial = "")
            val showLoading by viewModel.showLoading.collectAsState(initial = true)



            val timer = object : CountDownTimer(4000, 1000) {
                override fun onTick(p0: Long) {}
                override fun onFinish() {
                    viewModel.setShowLoading(false)
                }
            }
            timer.start()


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

                "", "null", null ->{
                    Log.d("TAG", "onCreate: ${role}")
                    LoginScreen(context = this@MainActivity, viewModel = viewModel)
                }

                else->{
                }

            }

            if (showLoading){
                SplashScreen(context = this, viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        observeRole()

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.checkIfUserDataIsExists(firebaseAuth)

        }

    }

    private fun observeRole() {
        lifecycleScope.launch {
            viewModel.role.collect{
                /**
                 * Seller | CHECK IF MARKETPLACE IS SELECTED
                 */
                if (it.equals("SELLER")){
                    observeSellerMarketplacesListStatus(this@MainActivity)

                    val cityId = viewModel.cityId.first()
                    val selectedMarketPlace = viewModel.selectedMarketPlaceId.first()

                    if (selectedMarketPlace.isNullOrEmpty()) {
                        viewModel.fetchApprovedMarketPlaces(cityId ?: 0)
                    }
                }
            }
        }
    }

    private fun observeSellerMarketplacesListStatus(context: Context) {
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
}