package com.mohalim.edokan.ui.main

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mohalim.edokan.core.datasource.preferences.UserPreferencesRepository
import com.mohalim.edokan.core.datasource.preferences.UserSelectionPreferencesRepository
import com.mohalim.edokan.core.datasource.repository.SellerRepository
import com.mohalim.edokan.core.datasource.repository.UserRepository
import com.mohalim.edokan.core.model.MarketPlace
import com.mohalim.edokan.core.model.Product
import com.mohalim.edokan.core.model.User
import com.mohalim.edokan.core.utils.AuthUtils
import com.mohalim.edokan.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val userPreferencesRepository: UserPreferencesRepository,
    val userSelectionPreferencesRepository: UserSelectionPreferencesRepository,
    val sellerRepository: SellerRepository,
    val userRepository: UserRepository,
    val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _showLoading = MutableStateFlow(true)
    val showLoading : StateFlow<Boolean> = _showLoading

    private val _marketplacesListStatus = MutableStateFlow<Any>(0)
    val marketplacesListStatus : MutableStateFlow<Any> = _marketplacesListStatus


    private val _uiState = MutableStateFlow<VerificationState>(VerificationState.Initial)
    val uiState: StateFlow<VerificationState> = _uiState

    private val _homePageState = MutableStateFlow("LOADING")

    private val _marketplaces = MutableStateFlow(emptyList<MarketPlace>())
    val marketplaces: StateFlow<List<MarketPlace>> = _marketplaces

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    fun searchProducts(query: String) {
        _searchQuery.value = query
        fetchProducts(query)
    }

    private fun fetchProducts(query: String) {
        viewModelScope.launch {
            firebaseAuth.currentUser!!.getIdToken(true).addOnSuccessListener {
                viewModelScope.launch {
                    sellerRepository.getProducts(it.token.toString(), query, 5L, selectedMarketPlaceId.toString()).collect{

                    }

                }

            }
        }
    }


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

    fun setProducts(products: List<Product>) {
        _products.value = products
    }

    fun setShowLoading(value: Boolean) {
        _showLoading.value = value
    }

    fun sendVerificationCode(context: Context, phoneNumber: String) {
        var phone = phoneNumber
        if (phone.startsWith("0")) {
            phone.drop(1)
        }

        phone = "+20"+phone

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
            .setCallbacks(callbacks)
            .setActivity(context as AppCompatActivity)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        _uiState.value = VerificationState.LoadingInitial

    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.d("onVerificationFailed", ""+e.message)
            _uiState.value = VerificationState.VerificationFailed(e)
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            Log.d("onCodeSent", ""+token)

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
                checkIfUserDataIsExists(auth)


            } catch (e: Exception) {
                Log.d("TAG", "signInWithPhoneAuthCredential: "+e.message)
            }
        }
    }

    fun checkIfUserDataIsExists(firebaseAuth: FirebaseAuth) {
        if (!AuthUtils.checkUserAuthentication(firebaseAuth)){
            return
        }

        firebaseAuth.currentUser!!.getIdToken(true).addOnSuccessListener {
            viewModelScope.launch {
                val token = it.token.toString()
                userRepository.getUserDataFromDatabase(token).collect{
                    when(it){
                        is Resource.Loading->{}
                        is Resource.Success->{
                            /**
                             * User is exist
                             * Loading data to the Prefs
                             */
                            withContext(Dispatchers.IO){
                                userPreferencesRepository.saveUserDetails(
                                    it.data?.userId.toString(),
                                    it.data?.username.toString(),
                                    it.data?.phoneNumber.toString(),
                                    it.data?.imageUrl.toString(),
                                    it.data?.role.toString(),
                                    it.data?.cityId ?: 0,
                                    it.data?.cityName.toString(),
                                )

                                /**
                                 * Start Fetching the Marketplaces
                                 * Get all marketplaces for the current city of the seller
                                 * #variables cityId, marketplaceOwnerId
                                 */
                                fetchApprovedMarketPlaces(it.data?.cityId ?: 0)

                            }

                        }
                        is Resource.Error->{
                            if (it.message == "User not found"){
                                val phoneNumber:String = firebaseAuth.currentUser?.phoneNumber.toString()
                                userRepository.addNewUser(token, "", "", phoneNumber, "", 1, "Higaza").collect{
                                    Log.d("TAG", "checkIfUserDataIsExists: "+it.message)
                                }
                            }
                        }
                    }
                }
            }

        }




        val user = firebaseAuth.currentUser ?: return

        db.collection("Users").document(user.phoneNumber.toString()).get().addOnSuccessListener { document ->
            if (document.exists()) {
                viewModelScope.launch {

                }

            } else {
                val cityId = 1;
                val cityName = "Higaza"

                val newUser = hashMapOf(
                    "uid" to user.uid,
                    "email" to user.email,
                    "phoneNumber" to user.phoneNumber,
                    "role" to "CUSTOMER",
                    "username" to user.displayName,
                    "imageUrl" to user.photoUrl.toString(),
                    "points" to 0,
                    "cityId" to cityId,
                    "cityName" to cityName
                )
                db.collection("Users").document(user.phoneNumber.toString()).set(newUser)
                    .addOnSuccessListener {

                        viewModelScope.launch {
                            withContext(Dispatchers.IO){
                                userPreferencesRepository.saveUserDetails(
                                    user.uid,
                                    user.displayName ?: "",
                                    user.phoneNumber ?: "",
                                    user.photoUrl.toString(),
                                    "CUSTOMER",
                                    cityId = cityId,
                                    "Higaza"
                                )

                                fetchApprovedMarketPlaces(cityId)

                            }
                        }

                    }
                    .addOnFailureListener { e ->
                        Log.d("TAG", ""+ e.message)
                    }

            }
        }.addOnFailureListener {
            Log.d("TAG", "error")
        }
    }


    fun fetchApprovedMarketPlaces(cityId: Int){
        val uid = firebaseAuth.currentUser!!.uid
        firebaseAuth.currentUser!!.getIdToken(true).addOnSuccessListener {
            viewModelScope.launch{
                sellerRepository.getApprovedMarketPlaces(it.token.toString(), cityId, uid).collect{
                    _marketplacesListStatus.value = it
                }
            }
        }


    }



    fun setSelectedMarketplace(marketplace: MarketPlace) {
        viewModelScope.launch {
            userSelectionPreferencesRepository.setSelectedMarketplaceId(marketplace.marketplaceId)
            userSelectionPreferencesRepository.setSelectedMarketplaceName(marketplace.marketplaceName)
            userSelectionPreferencesRepository.setSelectedMarketplaceLat(marketplace.lat)
            userSelectionPreferencesRepository.setSelectedMarketplaceLng(marketplace.lng)
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
    object RoleNotFound : VerificationState()
}