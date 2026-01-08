package com.example.travelupa.data.repository

import com.example.travelupa.data.model.JenisTempat
import com.example.travelupa.data.model.Kategori
import com.example.travelupa.data.model.Provinsi
import com.example.travelupa.data.model.TempatWisata
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class WisataRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    companion object {
        const val COLLECTION_WISATA = "tempat_wisata"
        const val COLLECTION_KATEGORI = "kategori"
        const val COLLECTION_JENIS_TEMPAT = "jenis_tempat"
        const val COLLECTION_PROVINSI = "provinsi"
    }

    fun getAllWisata(): Flow<List<TempatWisata>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_WISATA)
            .orderBy("nama", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val wisataList = snapshot?.documents?.mapNotNull { doc ->
                    doc.data?.let { TempatWisata.fromMap(doc.id, it) }
                } ?: emptyList()
                
                trySend(wisataList)
            }
        
        awaitClose { listener.remove() }
    }

    fun getFavoriteWisata(): Flow<List<TempatWisata>> = callbackFlow {
        val listener = firestore.collection(COLLECTION_WISATA)
            .whereEqualTo("isFavorite", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val wisataList = snapshot?.documents?.mapNotNull { doc ->
                    doc.data?.let { TempatWisata.fromMap(doc.id, it) }
                } ?: emptyList()
                
                trySend(wisataList)
            }
        
        awaitClose { listener.remove() }
    }

    suspend fun getWisataById(id: String): TempatWisata? {
        return try {
            val doc = firestore.collection(COLLECTION_WISATA).document(id).get().await()
            doc.data?.let { TempatWisata.fromMap(doc.id, it) }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addWisata(wisata: TempatWisata): Result<String> {
        return try {
            val docRef = firestore.collection(COLLECTION_WISATA).add(wisata.toMap()).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateWisata(wisata: TempatWisata): Result<Unit> {
        return try {
            firestore.collection(COLLECTION_WISATA)
                .document(wisata.id)
                .set(wisata.toMap())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteWisata(id: String): Result<Unit> {
        return try {
            firestore.collection(COLLECTION_WISATA).document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(id: String, isFavorite: Boolean): Result<Unit> {
        return try {
            firestore.collection(COLLECTION_WISATA)
                .document(id)
                .update("isFavorite", isFavorite)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getKategoriList(): List<Kategori> = Kategori.ALL
    fun getJenisTempatList(): List<JenisTempat> = JenisTempat.ALL
    fun getProvinsiList(): List<Provinsi> = Provinsi.ALL
    fun getKategoriById(id: String): Kategori? = Kategori.ALL.find { it.id == id }
    fun getJenisTempatById(id: String): JenisTempat? = JenisTempat.ALL.find { it.id == id }
    fun getProvinsiById(id: String): Provinsi? = Provinsi.ALL.find { it.id == id }
    
    suspend fun searchWisata(query: String): List<TempatWisata> {
        return try {
            val snapshot = firestore.collection(COLLECTION_WISATA)
                .orderBy("nama")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                doc.data?.let { TempatWisata.fromMap(doc.id, it) }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getWisataByKategori(kategoriId: String): List<TempatWisata> {
        return try {
            val snapshot = firestore.collection(COLLECTION_WISATA)
                .whereEqualTo("kategoriId", kategoriId)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                doc.data?.let { TempatWisata.fromMap(doc.id, it) }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
