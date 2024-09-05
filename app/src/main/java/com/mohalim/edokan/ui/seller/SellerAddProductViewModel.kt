package com.mohalim.edokan.ui.seller

import android.content.ClipDescription
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mohalim.edokan.core.datasource.repository.SellerRepository
import com.mohalim.edokan.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.mohalim.edokan.core.datasource.preferences.UserSelectionPreferencesRepository
import com.mohalim.edokan.core.model.Category
import com.mohalim.edokan.core.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import okhttp3.internal.wait
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@HiltViewModel
class SellerAddProductViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val sellerRepository: SellerRepository,
    val userSelectionPreferencesRepository: UserSelectionPreferencesRepository
) : ViewModel() {

    private val _imageProcess = MutableStateFlow<String>("THUMBNAIL")
    val imageProcess: MutableStateFlow<String> = _imageProcess

    private val _productName = MutableStateFlow("")
    val productName: MutableStateFlow<String> = _productName

    private val _productDescription = MutableStateFlow("")
    val productDescription: MutableStateFlow<String> = _productDescription

    private val _productPrice = MutableStateFlow(0.0)
    val productPrice: MutableStateFlow<Double> = _productPrice

    private val _productQuantity = MutableStateFlow(0.0)
    val productQuantity: MutableStateFlow<Double> = _productQuantity

    private val _productWidth = MutableStateFlow(0.0)
    val productWidth: MutableStateFlow<Double> = _productWidth

    private val _productHeight = MutableStateFlow(0.0)
    val productHeight: MutableStateFlow<Double> = _productHeight

    private val _productWeight = MutableStateFlow(0.0)
    val productWeight: MutableStateFlow<Double> = _productWeight

    private val _productLength = MutableStateFlow(0.0)
    val productLength: MutableStateFlow<Double> = _productLength

    private val _productDiscount = MutableStateFlow(0.0)
    val productDiscount: MutableStateFlow<Double> = _productDiscount

    private val _categories = MutableStateFlow(ArrayList<Category>())
    val categories: MutableStateFlow<ArrayList<Category>> = _categories

    private val _chosenCategories = MutableStateFlow(ArrayList<Category>())
    val chosenCategories: MutableStateFlow<ArrayList<Category>> = _chosenCategories


    private val _isCategoriesSearchExpanded = MutableStateFlow(false)
    val isCategoriesSearchExpanded: MutableStateFlow<Boolean> = _isCategoriesSearchExpanded


    private val _imageUri =
        MutableStateFlow<Uri>(Uri.parse("android.resource://com.mohalim.edokan/drawable/image_placeholder"))
    val imageUri: MutableStateFlow<Uri> = _imageUri

    private val _image1Uri =
        MutableStateFlow<Uri>(Uri.parse("android.resource://com.mohalim.edokan/drawable/image_placeholder"))
    val image1Uri: MutableStateFlow<Uri> = _image1Uri

    private val _image2Uri =
        MutableStateFlow<Uri>(Uri.parse("android.resource://com.mohalim.edokan/drawable/image_placeholder"))
    val image2Uri: MutableStateFlow<Uri> = _image2Uri

    private val _image3Uri =
        MutableStateFlow<Uri>(Uri.parse("android.resource://com.mohalim.edokan/drawable/image_placeholder"))
    val image3Uri: MutableStateFlow<Uri> = _image3Uri

    private val _image4Uri =
        MutableStateFlow<Uri>(Uri.parse("android.resource://com.mohalim.edokan/drawable/image_placeholder"))
    val image4Uri: MutableStateFlow<Uri> = _image4Uri


    private val _currentStep = MutableStateFlow(1)
    val currentStep: MutableStateFlow<Int> = _currentStep

    var selectedMarketplaceId = ""
    var selectedMarketplaceName = ""
    var selectedMarketplaceLat = 0.0
    var selectedMarketplaceLng = 0.0

    init {
        viewModelScope.launch {
            selectedMarketplaceId =
                userSelectionPreferencesRepository.getSelectedMarketplaceId() ?: ""
            selectedMarketplaceName =
                userSelectionPreferencesRepository.getSelectedMarketplaceName() ?: ""
            selectedMarketplaceLat =
                userSelectionPreferencesRepository.getSelectedMarketplaceLat() ?: 0.0
            selectedMarketplaceLng =
                userSelectionPreferencesRepository.getSelectedMarketplaceLng() ?: 0.0
        }
    }

    private val _formState = MutableStateFlow(Product())
    val formState: StateFlow<Product> = _formState

    fun setCurrentStep(step: Int) {
        _currentStep.value = step
    }

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun setImageProcess(imageProcess: String) {
        _imageProcess.value = imageProcess
    }

    fun setProductName(productName: String) {
        _productName.value = productName
    }

    fun setProductDescription(productDescription: String) {
        _productDescription.value = productDescription
    }

    fun setProductPrice(productPrice: Double) {
        _productPrice.value = productPrice
    }

    fun setProductQuantity(productQuantity: Double) {
        _productQuantity.value = productQuantity
    }

    fun setProductWidth(productWidth: Double) {
        _productWidth.value = productWidth
    }

    fun setProductHeight(productHeight: Double) {
        _productHeight.value = productHeight
    }

    fun setProductWeight(productWeight: Double) {
        _productWeight.value = productWeight
    }

    fun setProductLength(productLength: Double) {
        _productLength.value = productLength
    }

    fun setProductDiscount(productDiscount: Double) {
        _productDiscount.value = productDiscount
    }

    fun setImage1Uri(uri: Uri) {
        _image1Uri.value = uri
    }

    fun setImage2Uri(uri: Uri) {
        _image2Uri.value = uri
    }

    fun setImage3Uri(uri: Uri) {
        _image3Uri.value = uri
    }

    fun setImage4Uri(uri: Uri) {
        _image4Uri.value = uri
    }

    fun setCategories(categories: List<Category>){
        _categories.value.clear()
        _categories.value.addAll(categories)
    }

    fun addToChosenCategories(category: Category){
        val filtered = _chosenCategories.value.filter { it.categoryId.equals(category.categoryId) }
        if (filtered.isNullOrEmpty()){
            _chosenCategories.value = _chosenCategories.value.toMutableList().apply {
                add(category)
            } as ArrayList<Category>
        }
    }

    fun removeFromChosenCategories(category: Category){
        _chosenCategories.value.remove(category)
    }

    fun setIsCategoriesSearchExpanded(expanded : Boolean){
        _isCategoriesSearchExpanded.value = expanded
    }

    fun updateFormState(update: (Product) -> Product) {
        _formState.value = update(_formState.value)
    }

    fun addProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            val formState = _formState.value
            Log.d("TAG", "addProduct: before 0")


            Log.d("TAG", "addProduct: before 1 ")

            val product = Product(
                productName = formState.productName,
                productDescription = formState.productDescription,
                productPrice = formState.productPrice,
                productDiscount = formState.productDiscount,
                marketPlaceId = selectedMarketplaceId ?: "",
                marketPlaceName = selectedMarketplaceName ?: "",
                marketPlaceLat = selectedMarketplaceLat ?: 0.0,
                marketPlaceLng = selectedMarketplaceLng ?: 0.0,
                productQuantity = formState.productQuantity,
                dateAdded = System.currentTimeMillis().toDouble(),
                dateModified = System.currentTimeMillis().toDouble()
            )

            Log.d("TAG", "addProduct: before2 ")


            sellerRepository.addProduct(product).collect {
                Log.d("TAG", "addProduct: " + it)
            }

        }
    }

    fun searchForCategories(categoryName: String) {
        Log.d("TAG", "categoryName "+ categoryName)
        firebaseAuth.currentUser?.getIdToken(true)?.addOnSuccessListener {
            viewModelScope.launch {
                sellerRepository.getCategoriesByName(it.token.toString(), categoryName).collect {
                    when(it){
                        is Resource.Loading->{

                        }

                        is Resource.Success->{
                            if(!it.data.isNullOrEmpty()){
                                setCategories(it.data)
                                if (it.data.size > 0){
                                    setIsCategoriesSearchExpanded(true)
                                }
                            }


                        }

                        is Resource.Error->{
                            Log.d("Tag", it.message+"")

                        }
                    }
                }
        }
    }
}

}


