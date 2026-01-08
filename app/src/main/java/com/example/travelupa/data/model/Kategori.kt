package com.example.travelupa.data.model

data class Kategori(
    val id: String = "",
    val nama: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        "nama" to nama
    )

    companion object {
        fun fromMap(id: String, map: Map<String, Any?>): Kategori {
            return Kategori(
                id = id,
                nama = map["nama"] as? String ?: ""
            )
        }
        
        val ALAM = Kategori(id = "alam", nama = "Alam")
        val SEJARAH = Kategori(id = "sejarah", nama = "Sejarah")
        val KULINER = Kategori(id = "kuliner", nama = "Kuliner")
        val RELIGI = Kategori(id = "religi", nama = "Religi")
        val BUDAYA = Kategori(id = "budaya", nama = "Budaya")
        
        val ALL = listOf(ALAM, SEJARAH, KULINER, RELIGI, BUDAYA)
    }
}
