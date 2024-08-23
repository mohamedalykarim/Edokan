package com.mohalim.edokan.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mohalim.edokan.core.datasource.preferences.UserPreferencesRepository
import com.mohalim.edokan.core.datasource.preferences.UserSelectionPreferencesRepository
import com.mohalim.edokan.core.datasource.repository.SellerRepository
import com.mohalim.edokan.core.model.MarketPlace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val userPreferencesRepository: UserPreferencesRepository,
    val userSelectionPreferencesRepository: UserSelectionPreferencesRepository,
    val sellerRepository: SellerRepository

) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _marketplacesListStatus = MutableStateFlow<Any>(0)
    val marketplacesListStatus : MutableStateFlow<Any> = _marketplacesListStatus


    private val _uiState = MutableStateFlow<VerificationState>(VerificationState.Initial)
    val uiState: StateFlow<VerificationState> = _uiState

    private val _homePageState = MutableStateFlow("LOADING")
    val homePageState: StateFlow<String> = _homePageState

    private val _marketplaces = MutableStateFlow(emptyList<MarketPlace>())
    val marketplaces: StateFlow<List<MarketPlace>> = _marketplaces

    private lateinit var verificationId: String
    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken


    val username: Flow<String?> = userPreferencesRepository.usernameFlow
    val phoneNumber: Flow<String?> = userPreferencesRepository.phoneNumberFlow
    val imageUrl: Flow<String?> = userPreferencesRepository.imageUrlFlow
    val role: Flow<String?> = userPreferencesRepository.roleFlow
    val cityId: Flow<Int?> = userPreferencesRepository.cityIdFlow
    val city: Flow<String?> = userPreferencesRepository.cityFlow

    val selectedMarketPlaceId: Flow<String?> = userSelectionPreferencesRepository.selectedMarketplaceIdFlow
    val selectedMarketPlaceName: Flow<String?> = userSelectionPreferencesRepository.selectedMarketplaceNameFlow

    fun setHomePageState(state: String) {
        _homePageState.value = state
    }

    fun setMarketplaces(marketplaces: List<MarketPlace>) {
        _marketplaces.value = marketplaces
    }

    fun sendVerificationCode(phoneNumber: String) {
        var phone = phoneNumber
        if (phone.startsWith("0")) {
            phone.drop(1)
        }

        phone = "+20"+phone

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        _uiState.value = VerificationState.LoadingInitial

    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            _uiState.value = VerificationState.VerificationFailed(e)
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            this@MainViewModel.verificationId = verificationId
            resendingToken = token
            _uiState.value = VerificationState.CodeSent(verificationId)
        }
    }

    fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            try {
                auth.signInWithCredential(credential).await()
                _uiState.value = VerificationState.VerificationCompleted
                navigateBasedOnRole()
            } catch (e: Exception) {
                _uiState.value = VerificationState.VerificationFailed(e)
            }
        }
    }

    fun navigateBasedOnRole() {
        val user = auth.currentUser ?: return
        db.collection("Users").document(user.phoneNumber.toString()).get().addOnSuccessListener { document ->
            if (document.exists()) {
                val databaseRole = document.getString("role")
                _uiState.value = VerificationState.Navigate(databaseRole ?: "")

                viewModelScope.launch {
                    withContext(Dispatchers.IO){
                        userPreferencesRepository.saveUserDetails(
                            document.getString("uid").toString(),
                            document.getString("username").toString(),
                            document.getString("phoneNumber").toString(),
                            document.getString("imageUrl").toString(),
                            document.getString("role").toString(),
                            document.getLong("city_id")?.toInt() ?: 1,
                            document.getString("city").toString(),
                        )
                    }
                }

            } else {
                val newUser = hashMapOf(
                    "uid" to user.phoneNumber.toString(),
                    "email" to user.email,
                    "phoneNumber" to user.phoneNumber,
                    "role" to "CUSTOMER",
                    "username" to user.displayName,
                    "imageUrl" to user.photoUrl.toString(),
                    "points" to 0
                )
                db.collection("Users").document(user.phoneNumber.toString()).set(newUser)
                    .addOnSuccessListener {
                        _uiState.value = VerificationState.Navigate("CUSTOMER")
                        viewModelScope.launch {
                            withContext(Dispatchers.IO){
                                userPreferencesRepository.saveUserDetails(
                                    user.uid,
                                    user.displayName ?: "",
                                    user.phoneNumber ?: "",
                                    user.photoUrl.toString(),
                                    "CUSTOMER",
                                    1,
                                    "Higaza"
                                )
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        _uiState.value = VerificationState.VerificationFailed(e)
                    }

            }
        }.addOnFailureListener {
            _uiState.value = VerificationState.VerificationFailed(it)
        }
    }


    fun fetchApprovedMarketPlaces(cityId: Int, marketplaceOwnerId: String){
        viewModelScope.launch{
            sellerRepository.getApprovedMarketPlaces(cityId, marketplaceOwnerId).collect{
                _marketplacesListStatus.value = it
                Log.d("TAG", "fetchApprovedMarketPlaces: "+cityId+" "+marketplaceOwnerId)
            }
        }
    }

    fun setSelectedMarketplace(marketplace: MarketPlace) {
        viewModelScope.launch {
            userSelectionPreferencesRepository.setSelectedMarketplaceId(marketplace.marketplaceId)
            userSelectionPreferencesRepository.setSelectedMarketplaceName(marketplace.marketplaceName)
        }
    }


}

sealed class VerificationState {
    object LoadingInitial : VerificationState()
    object LoadingCodeSent : VerificationState()

    object Initial : VerificationState()
    object VerificationCompleted : VerificationState()
    data class VerificationFailed(val error: Exception) : VerificationState()
    data class CodeSent(val verificationId: String) : VerificationState()
    data class Navigate(val role: String) : VerificationState()
    object RoleNotFound : VerificationState()
}