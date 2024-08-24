package com.mohalim.edokan.ui.seller

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
import com.mohalim.edokan.core.datasource.preferences.UserSelectionPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.internal.wait
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@HiltViewModel
class SellerAddProductViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val sellerRepository: SellerRepository,
    val userSelectionPreferencesRepository: UserSelectionPreferencesRepository
) : ViewModel(){

    var selectedMarketplaceId = ""
    var selectedMarketplaceName = ""
    var selectedMarketplaceLat = 0.0
    var selectedMarketplaceLng = 0.0

    init {
        viewModelScope.launch {
            selectedMarketplaceId = userSelectionPreferencesRepository.getSelectedMarketplaceId() ?: ""
            selectedMarketplaceName = userSelectionPreferencesRepository.getSelectedMarketplaceName() ?: ""
            selectedMarketplaceLat = userSelectionPreferencesRepository.getSelectedMarketplaceLat() ?: 0.0
            selectedMarketplaceLng = userSelectionPreferencesRepository.getSelectedMarketplaceLng() ?: 0.0
        }
    }

    private val _formState = MutableStateFlow(Product())
    val formState: StateFlow<Product> = _formState

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
                marketPlaceLng = selectedMarketplaceLng ?: 0.0 ,
                productQuantity = formState.productQuantity,
                dateAdded = System.currentTimeMillis().toDouble(),
                dateModified = System.currentTimeMillis().toDouble()
            )

            Log.d("TAG", "addProduct: before2 ")


            sellerRepository.addProduct(product).collect{
                Log.d("TAG", "addProduct: "+it)
            }

        }
    }


    fun uploadImageToFirebase(storageRef: FirebaseStorage, uri: Uri, onSuccess: (String) -> Unit) {
        val fileName = UUID.randomUUID().toString()
        val ref = storageRef.reference.child("product_images/$fileName")
        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString())
                }
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        val fileName = "JPEG_${System.currentTimeMillis()}.jpg"
        var file: File? = null
        var fos: FileOutputStream? = null

        return try {
            // Get the directory for the app's private pictures directory.
            val directory = File(context.cacheDir, "images").apply { mkdirs() }

            // Create the image file in the directory
            file = File(directory, fileName)

            // Save the bitmap to the file
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)

            // Return the Uri using FileProvider (make sure to set up FileProvider in your manifest)
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            fos?.close()
        }
    }
}