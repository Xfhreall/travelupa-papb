package com.example.travelupa.data.model

data class Provinsi(
    val id: String = "",
    val nama: String = "",
    val kode: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        "nama" to nama,
        "kode" to kode
    )

    companion object {
        fun fromMap(id: String, map: Map<String, Any?>): Provinsi {
            return Provinsi(
                id = id,
                nama = map["nama"] as? String ?: "",
                kode = map["kode"] as? String ?: ""
            )
        }
        
        val JAWA_TIMUR = Provinsi(id = "jawa_timur", nama = "Jawa Timur", kode = "JT")
        val JAWA_TENGAH = Provinsi(id = "jawa_tengah", nama = "Jawa Tengah", kode = "JTG")
        val JAWA_BARAT = Provinsi(id = "jawa_barat", nama = "Jawa Barat", kode = "JB")
        val BALI = Provinsi(id = "bali", nama = "Bali", kode = "BA")
        val NTT = Provinsi(id = "ntt", nama = "Nusa Tenggara Timur", kode = "NTT")
        val NTB = Provinsi(id = "ntb", nama = "Nusa Tenggara Barat", kode = "NTB")
        val SUMATERA_UTARA = Provinsi(id = "sumatera_utara", nama = "Sumatera Utara", kode = "SU")
        val PAPUA_BARAT = Provinsi(id = "papua_barat", nama = "Papua Barat", kode = "PB")
        val YOGYAKARTA = Provinsi(id = "yogyakarta", nama = "DI Yogyakarta", kode = "DIY")
        val DKI_JAKARTA = Provinsi(id = "dki_jakarta", nama = "DKI Jakarta", kode = "DKI")
        
        val ALL = listOf(
            JAWA_TIMUR, JAWA_TENGAH, JAWA_BARAT, BALI, NTT, NTB, 
            SUMATERA_UTARA, PAPUA_BARAT, YOGYAKARTA, DKI_JAKARTA
        )
    }
}
