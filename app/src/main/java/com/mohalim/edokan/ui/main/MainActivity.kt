package com.mohalim.edokan.ui.main

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mohalim.edokan.core.datasource.UserPreferencesRepository
import com.mohalim.edokan.core.model.City
import com.mohalim.edokan.core.utils.AuthUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel by viewModels<LoginViewModel>()

    @Inject lateinit var userPreferencesRepository: UserPreferencesRepository

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
                        val role by viewModel.role.collectAsState(initial = "CUSTOMER")
                        if (role != null) NavigateBasedInRole(viewModel, role!!)
                    }

                }
            }


        }

    }

    @Composable
    private fun NavigateBasedInRole(viewModel: LoginViewModel, role : String) {
        Log.d("TAG", "NavigateBasedInRole: "+role)
        when (role){
            "CUSTOMER"->{
                CustomerMainUI(viewModel = viewModel)
            }
            "SELLER"->{
                Text(text = "Seller")
                SellerMainUI(viewModel = viewModel)
            }
            "DELIVERY"->{
            }

        }
    }
}