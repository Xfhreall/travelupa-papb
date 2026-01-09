package com.example.travelupa.data.model

import java.util.UUID

enum class FilterOperator(val displayName: String) {
    CONTAINS("mengandung");
    
    companion object {
        fun getOperatorsForField(field: FilterField): List<FilterOperator> {
            return listOf(CONTAINS)
        }
    }
}

enum class FilterField(val displayName: String) {
    NAMA("Nama"),
    KATEGORI("Kategori"),
    JENIS_TEMPAT("Jenis Tempat"),
    PROVINSI("Provinsi"),
    HARGA("Harga")
}

data class FilterCondition(
    val id: String = UUID.randomUUID().toString(),
    val field: FilterField,
    val operator: FilterOperator,
    val value: String,
    val displayValue: String = value
) {
    fun toDisplayString(): String {
        return "${field.displayName}: $displayValue"
    }
}

data class FilterState(
    val conditions: List<FilterCondition> = emptyList()
) {
    val hasActiveFilters: Boolean
        get() = conditions.isNotEmpty()
    
    val filterCount: Int
        get() = conditions.size
}
