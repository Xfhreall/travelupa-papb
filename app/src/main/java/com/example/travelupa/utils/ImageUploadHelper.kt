package com.example.travelupa.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.room.Room
import com.example.travelupa.data.database.AppDatabase
import com.example.travelupa.data.entity.ImageEntity
import com.example.travelupa.data.model.TempatWisata
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

/**
 * Upload image to Firebase Storage and save wisata to Firestore
 */
suspend fun uploadImageToFirestore(
    firestore: FirebaseFirestore,
    context: Context,
    imageUri: Uri,
    tempatWisata: TempatWisata,
    onSuccess: (TempatWisata) -> Unit,
    onFailure: (Exception) -> Unit
) {
    try {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "travelupa-database"
        ).build()
        
        val imageDao = db.imageDao()
        
        // Save locally first
        val localPath = saveImageLocally(context, imageUri)
        
        withContext(Dispatchers.IO) {
            // Save to Room DB
            imageDao.insert(ImageEntity(localPath = localPath))
            
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")
            
            // Use the original imageUri for upload, not the local file path
            imageRef.putFile(imageUri).await()
            
            val downloadUrl = imageRef.downloadUrl.await()
            
            val updatedTempatWisata = tempatWisata.copy(
                gambarUrl = downloadUrl.toString()
            )
            
            firestore.collection("tempat_wisata")
                .add(updatedTempatWisata.toMap())
                .addOnSuccessListener {
                    onSuccess(updatedTempatWisata)
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
        }
    } catch (e: Exception) {
        Log.e("ImageUpload", "Error uploading image", e)
        onFailure(e)
    }
}

/**
 * Save image to local storage
 */
fun saveImageLocally(context: Context, uri: Uri): String {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        
        val file = File(
            context.filesDir,
            "image_${System.currentTimeMillis()}.jpg"
        )
        
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        
        Log.d("ImageSave", "Image saved successfully to: ${file.absolutePath}")
        file.absolutePath
    } catch (e: Exception) {
        Log.e("ImageSave", "Error saving image", e)
        throw e
    }
}

/**
 * Upload image to Firebase Storage and return the download URL
 */
suspend fun uploadImageToStorage(imageUri: Uri): String? {
    return try {
        val storage = FirebaseStorage.getInstance()
        val imageRef = storage.reference.child("wisata/${UUID.randomUUID()}.jpg")
        imageRef.putFile(imageUri).await()
        imageRef.downloadUrl.await().toString()
    } catch (e: Exception) {
        Log.e("ImageUpload", "Error uploading to storage", e)
        null
    }
}
