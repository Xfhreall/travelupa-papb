package com.example.travelupa.utils

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

data class TempatWisataData(
    val nama: String = "",
    val deskripsi: String = "",
    val gambarUriString: String = ""
)

suspend fun uploadImageToFirestore(
    firestore: FirebaseFirestore,
    context: Context,
    gambarUri: Uri,
    tempatWisata: TempatWisataData,
    onSuccess: (TempatWisataData) -> Unit,
    onFailure: (Exception) -> Unit
) {
    try {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")
        
        imageRef.putFile(gambarUri).await()
        
        val downloadUrl = imageRef.downloadUrl.await()
        
        val uploadedTempat = tempatWisata.copy(
            gambarUriString = downloadUrl.toString()
        )
        
        firestore.collection("tempat_wisata")
            .add(uploadedTempat)
            .await()
        
        onSuccess(uploadedTempat)
    } catch (e: Exception) {
        onFailure(e)
    }
}
