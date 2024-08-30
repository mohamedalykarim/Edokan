package com.mohalim.edokan.ui.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mohalim.edokan.core.datasource.preferences.UserPreferencesRepository
import com.mohalim.edokan.core.datasource.repository.SellerRepository
import com.mohalim.edokan.core.model.MarketPlace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SellerAddMarketplaceViewModel @Inject constructor(val firebaseAuth: FirebaseAuth, val sellerRepository: SellerRepository, val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _screenStatus = MutableStateFlow<Any>(0)
    val screenStatus : MutableStateFlow<Any> = _screenStatus

    private val _showAddedDialog = MutableStateFlow(false)
    val showAddedDialog : MutableStateFlow<Boolean> = _showAddedDialog

    private val _lat = MutableStateFlow<Double>(0.0)
    val lat : MutableStateFlow<Double> = _lat

    private val _lng = MutableStateFlow<Double>(0.0)
    val lng : MutableStateFlow<Double> = _lng

    fun setLat(lat: Double) {
        _lat.value = lat
    }

    fun setLng(lng: Double) {
        _lng.value = lng
    }

    fun setShowAddedDialog(value: Boolean) {
        _showAddedDialog.value = value
    }

    val username: Flow<String?> = userPreferencesRepository.usernameFlow
    val phoneNumber: Flow<String?> = userPreferencesRepository.phoneNumberFlow
    val imageUrl: Flow<String?> = userPreferencesRepository.imageUrlFlow
    val role: Flow<String?> = userPreferencesRepository.roleFlow
    val cityId: Flow<Int?> = userPreferencesRepository.cityIdFlow
    val city: Flow<String?> = userPreferencesRepository.cityFlow


    fun addMarketplace(
        name: String, lat: Double, lng: Double,
        cityId: Int, cityName: String, ownerId: String, isApproved: Int
        ) {
        firebaseAuth.currentUser!!.getIdToken(true).addOnSuccessListener {
            viewModelScope.launch {
                sellerRepository.addMarketPlace(it.token.toString(), name, lat, lng, cityId, cityName, ownerId, isApproved).collect{
                    _screenStatus.value = it
                }
            }
        }
        }






}
