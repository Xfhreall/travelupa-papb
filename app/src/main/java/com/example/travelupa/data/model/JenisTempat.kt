package com.example.travelupa.data.model

data class JenisTempat(
    val id: String = "",
    val nama: String = "",
    val deskripsi: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        "nama" to nama,
        "deskripsi" to deskripsi
    )

    companion object {
        fun fromMap(id: String, map: Map<String, Any?>): JenisTempat {
            return JenisTempat(
                id = id,
                nama = map["nama"] as? String ?: "",
                deskripsi = map["deskripsi"] as? String ?: ""
            )
        }
        
        val PANTAI = JenisTempat(id = "pantai", nama = "Pantai", deskripsi = "Wisata pantai dan laut")
        val GUNUNG = JenisTempat(id = "gunung", nama = "Gunung", deskripsi = "Wisata pegunungan")
        val DANAU = JenisTempat(id = "danau", nama = "Danau", deskripsi = "Wisata danau")
        val AIR_TERJUN = JenisTempat(id = "air_terjun", nama = "Air Terjun", deskripsi = "Wisata air terjun")
        val MUSEUM = JenisTempat(id = "museum", nama = "Museum", deskripsi = "Wisata museum dan galeri")
        val CANDI = JenisTempat(id = "candi", nama = "Candi", deskripsi = "Wisata candi dan situs bersejarah")
        val TAMAN = JenisTempat(id = "taman", nama = "Taman", deskripsi = "Wisata taman dan kebun")
        
        val ALL = listOf(PANTAI, GUNUNG, DANAU, AIR_TERJUN, MUSEUM, CANDI, TAMAN)
    }
}
