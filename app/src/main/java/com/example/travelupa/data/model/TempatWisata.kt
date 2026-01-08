package com.example.travelupa.data.model

data class TempatWisata(
    val id: String = "",
    val nama: String = "",
    val deskripsi: String = "",
    val gambarUrl: String? = null,
    val gambarResId: Int? = null,
    val isFavorite: Boolean = false,
    val kategoriId: String = "",
    val jenisTempatId: String = "",
    val provinsiId: String = "",
    val harga: Long = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "nama" to nama,
        "deskripsi" to deskripsi,
        "gambarUrl" to gambarUrl,
        "isFavorite" to isFavorite,
        "kategoriId" to kategoriId,
        "jenisTempatId" to jenisTempatId,
        "provinsiId" to provinsiId,
        "harga" to harga,
        "latitude" to latitude,
        "longitude" to longitude
    )

    companion object {
        fun fromMap(id: String, map: Map<String, Any?>): TempatWisata {
            return TempatWisata(
                id = id,
                nama = map["nama"] as? String ?: "",
                deskripsi = map["deskripsi"] as? String ?: "",
                gambarUrl = map["gambarUrl"] as? String,
                isFavorite = map["isFavorite"] as? Boolean ?: false,
                kategoriId = map["kategoriId"] as? String ?: "",
                jenisTempatId = map["jenisTempatId"] as? String ?: "",
                provinsiId = map["provinsiId"] as? String ?: "",
                harga = (map["harga"] as? Number)?.toLong() ?: 0,
                latitude = (map["latitude"] as? Number)?.toDouble() ?: 0.0,
                longitude = (map["longitude"] as? Number)?.toDouble() ?: 0.0
            )
        }
    }
}
