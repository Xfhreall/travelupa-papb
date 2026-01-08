package com.example.travelupa.data.seeder

import android.content.Context
import android.util.Log
import com.example.travelupa.data.model.JenisTempat
import com.example.travelupa.data.model.Kategori
import com.example.travelupa.data.model.Provinsi
import com.example.travelupa.data.model.TempatWisata
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Seeder for initial wisata data
 */
object WisataSeeder {
    private const val TAG = "WisataSeeder"
    private const val PREF_NAME = "travelupa_prefs"
    private const val KEY_SEEDED = "data_seeded"

    /**
     * Check if seeder has already run
     */
    fun isSeeded(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_SEEDED, false)
    }

    /**
     * Mark data as seeded
     */
    private fun markAsSeeded(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SEEDED, true).apply()
    }

    /**
     * Seed initial data to Firestore
     */
    suspend fun seedData(context: Context, firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {
        if (isSeeded(context)) {
            Log.d(TAG, "Data already seeded, skipping...")
            return
        }

        try {
            Log.d(TAG, "Starting data seeding...")
            
            // Seed wisata data
            val wisataList = getInitialWisataData()
            val collection = firestore.collection("tempat_wisata")
            
            wisataList.forEach { wisata ->
                collection.add(wisata.toMap()).await()
                Log.d(TAG, "Seeded: ${wisata.nama}")
            }
            
            markAsSeeded(context)
            Log.d(TAG, "Data seeding completed! ${wisataList.size} items added.")
        } catch (e: Exception) {
            Log.e(TAG, "Error seeding data", e)
        }
    }

    /**
     * Reset seeder flag to allow re-seeding
     */
    fun resetSeeder(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SEEDED, false).apply()
        Log.d(TAG, "Seeder flag reset")
    }

    /**
     * Force reseed - clears old data and adds new data with coordinates
     */
    suspend fun forceReseed(context: Context, firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {
        try {
            Log.d(TAG, "Force reseeding - clearing old data...")
            
            // Delete all existing wisata documents
            val collection = firestore.collection("tempat_wisata")
            val documents = collection.get().await()
            documents.forEach { doc ->
                doc.reference.delete().await()
            }
            Log.d(TAG, "Deleted ${documents.size()} old documents")
            
            // Reset seeder flag
            resetSeeder(context)
            
            // Re-seed with new data
            val wisataList = getInitialWisataData()
            wisataList.forEach { wisata ->
                collection.add(wisata.toMap()).await()
                Log.d(TAG, "Seeded: ${wisata.nama}")
            }
            
            markAsSeeded(context)
            Log.d(TAG, "Force reseed completed! ${wisataList.size} items added.")
        } catch (e: Exception) {
            Log.e(TAG, "Error during force reseed", e)
        }
    }

    /**
     * Get initial wisata data (10+ items)
     */
    private fun getInitialWisataData(): List<TempatWisata> = listOf(
        TempatWisata(
            nama = "Tumpak Sewu",
            deskripsi = "Air terjun tercantik di Jawa Timur dengan pemandangan spektakuler. Terletak di perbatasan Kabupaten Lumajang dan Malang, air terjun ini memiliki ketinggian sekitar 120 meter dengan formasi unik menyerupai tirai.",
            gambarUrl = "https://images.unsplash.com/photo-1555400038-63f5ba517a47?w=800",
            kategoriId = Kategori.ALAM.id,
            jenisTempatId = JenisTempat.AIR_TERJUN.id,
            provinsiId = Provinsi.JAWA_TIMUR.id,
            harga = 30000,
            latitude = -8.2304,
            longitude = 113.0250
        ),
        TempatWisata(
            nama = "Gunung Bromo",
            deskripsi = "Gunung berapi aktif yang terkenal dengan pemandangan sunrise dan lautan pasirnya yang menakjubkan. Destinasi wajib di Jawa Timur dengan ketinggian 2.329 meter di atas permukaan laut.",
            gambarUrl = "https://images.unsplash.com/photo-1588668214407-6ea9a6d8c272?w=800",
            kategoriId = Kategori.ALAM.id,
            jenisTempatId = JenisTempat.GUNUNG.id,
            provinsiId = Provinsi.JAWA_TIMUR.id,
            harga = 35000,
            latitude = -7.9425,
            longitude = 112.9530
        ),
        TempatWisata(
            nama = "Kawah Ijen",
            deskripsi = "Kawah vulkanik dengan fenomena api biru (blue fire) yang langka. Terletak di perbatasan Banyuwangi dan Bondowoso, danau kawahnya merupakan danau asam terbesar di dunia.",
            gambarUrl = "https://images.unsplash.com/photo-1580619305218-8423a7ef79b4?w=800",
            kategoriId = Kategori.ALAM.id,
            jenisTempatId = JenisTempat.GUNUNG.id,
            provinsiId = Provinsi.JAWA_TIMUR.id,
            harga = 100000,
            latitude = -8.0588,
            longitude = 114.2420
        ),
        TempatWisata(
            nama = "Pantai Kuta Bali",
            deskripsi = "Pantai terkenal di Bali dengan pasir putih dan ombak yang cocok untuk berselancar. Dilengkapi berbagai fasilitas wisata, restoran, dan kehidupan malam yang ramai.",
            gambarUrl = "https://images.unsplash.com/photo-1537996194471-e657df975ab4?w=800",
            kategoriId = Kategori.ALAM.id,
            jenisTempatId = JenisTempat.PANTAI.id,
            provinsiId = Provinsi.BALI.id,
            harga = 0,
            latitude = -8.7180,
            longitude = 115.1686
        ),
        TempatWisata(
            nama = "Tanah Lot",
            deskripsi = "Pura Hindu yang ikonik di atas batu karang di tengah laut. Salah satu tujuan wisata paling populer di Bali dengan pemandangan sunset yang spektakuler.",
            gambarUrl = "https://images.unsplash.com/photo-1555400038-63f5ba517a47?w=800",
            kategoriId = Kategori.RELIGI.id,
            jenisTempatId = JenisTempat.CANDI.id,
            provinsiId = Provinsi.BALI.id,
            harga = 60000,
            latitude = -8.6213,
            longitude = 115.0867
        ),
        TempatWisata(
            nama = "Candi Borobudur",
            deskripsi = "Candi Buddha terbesar di dunia, warisan budaya UNESCO. Dibangun pada abad ke-8 dengan lebih dari 2.600 panel relief dan 504 arca Buddha.",
            gambarUrl = "https://images.unsplash.com/photo-1596402184320-417e7178b2cd?w=800",
            kategoriId = Kategori.SEJARAH.id,
            jenisTempatId = JenisTempat.CANDI.id,
            provinsiId = Provinsi.JAWA_TENGAH.id,
            harga = 50000,
            latitude = -7.6079,
            longitude = 110.2038
        ),
        TempatWisata(
            nama = "Candi Prambanan",
            deskripsi = "Kompleks candi Hindu terbesar di Indonesia, warisan budaya UNESCO. Dikenal dengan arsitektur megah dan relief yang menceritakan kisah Ramayana.",
            gambarUrl = "https://images.unsplash.com/photo-1584810359583-96fc3448beaa?w=800",
            kategoriId = Kategori.SEJARAH.id,
            jenisTempatId = JenisTempat.CANDI.id,
            provinsiId = Provinsi.JAWA_TENGAH.id,
            harga = 50000,
            latitude = -7.7520,
            longitude = 110.4914
        ),
        TempatWisata(
            nama = "Danau Toba",
            deskripsi = "Danau vulkanik terbesar di dunia dan terbesar di Indonesia. Dengan Pulau Samosir di tengahnya, danau ini menawarkan keindahan alam dan budaya Batak yang kaya.",
            gambarUrl = "https://images.unsplash.com/photo-1571366343168-631c5bcca7a4?w=800",
            kategoriId = Kategori.ALAM.id,
            jenisTempatId = JenisTempat.DANAU.id,
            provinsiId = Provinsi.SUMATERA_UTARA.id,
            harga = 0,
            latitude = 2.6845,
            longitude = 98.8588
        ),
        TempatWisata(
            nama = "Raja Ampat",
            deskripsi = "Kepulauan dengan keanekaragaman hayati laut tertinggi di dunia. Surga bagi penyelam dengan lebih dari 1.500 spesies ikan dan 75% spesies karang dunia.",
            gambarUrl = "https://images.unsplash.com/photo-1544551763-46a013bb70d5?w=800",
            kategoriId = Kategori.ALAM.id,
            jenisTempatId = JenisTempat.PANTAI.id,
            provinsiId = Provinsi.PAPUA_BARAT.id,
            harga = 500000,
            latitude = -0.2348,
            longitude = 130.5167
        ),
        TempatWisata(
            nama = "Labuan Bajo",
            deskripsi = "Gerbang menuju Taman Nasional Komodo. Destinasi wisata dengan pantai eksotis, pulau-pulau indah, dan rumah bagi komodo, hewan purba yang dilindungi.",
            gambarUrl = "https://images.unsplash.com/photo-1570789210967-2cac24ba7f26?w=800",
            kategoriId = Kategori.ALAM.id,
            jenisTempatId = JenisTempat.PANTAI.id,
            provinsiId = Provinsi.NTT.id,
            harga = 150000,
            latitude = -8.4965,
            longitude = 119.8772
        ),
        TempatWisata(
            nama = "Malioboro",
            deskripsi = "Jalan legendaris di jantung Yogyakarta yang menjadi pusat wisata belanja dan kuliner. Menawarkan pengalaman budaya Jawa yang autentik dengan berbagai kerajinan tradisional.",
            gambarUrl = "https://images.unsplash.com/photo-1565967511849-76a60a516170?w=800",
            kategoriId = Kategori.BUDAYA.id,
            jenisTempatId = JenisTempat.TAMAN.id,
            provinsiId = Provinsi.YOGYAKARTA.id,
            harga = 0,
            latitude = -7.7925,
            longitude = 110.3659
        ),
        TempatWisata(
            nama = "Pantai Pink Lombok",
            deskripsi = "Salah satu dari tujuh pantai berpasir pink di dunia. Warna pink berasal dari campuran pasir putih dengan pecahan karang merah mikroskopis.",
            gambarUrl = "https://images.unsplash.com/photo-1559128010-7c1ad6e1b6a5?w=800",
            kategoriId = Kategori.ALAM.id,
            jenisTempatId = JenisTempat.PANTAI.id,
            provinsiId = Provinsi.NTB.id,
            harga = 25000,
            latitude = -8.8391,
            longitude = 116.5201
        )
    )
}
