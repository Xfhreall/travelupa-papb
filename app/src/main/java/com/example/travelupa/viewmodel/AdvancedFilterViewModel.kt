package com.example.travelupa.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.travelupa.data.model.FilterCondition
import com.example.travelupa.data.model.FilterField
import com.example.travelupa.data.model.FilterOperator
import com.example.travelupa.data.model.FilterState
import com.example.travelupa.data.model.TempatWisata

class AdvancedFilterViewModel {
    
    var filterState by mutableStateOf(FilterState())
        private set

    fun addFilter(condition: FilterCondition) {
        filterState = filterState.copy(
            conditions = filterState.conditions + condition
        )
    }
    
    fun removeFilter(conditionId: String) {
        filterState = filterState.copy(
            conditions = filterState.conditions.filter { it.id != conditionId }
        )
    }
    
    fun updateFilter(conditionId: String, newCondition: FilterCondition) {
        filterState = filterState.copy(
            conditions = filterState.conditions.map { 
                if (it.id == conditionId) newCondition.copy(id = conditionId) else it 
            }
        )
    }
    
    fun clearFilters() {
        filterState = FilterState()
    }
    
    fun applyFilters(wisataList: List<TempatWisata>): List<TempatWisata> {
        if (!filterState.hasActiveFilters) {
            return wisataList
        }
        
        return wisataList.filter { wisata ->
            filterState.conditions.all { condition ->
                matchesCondition(wisata, condition)
            }
        }
    }
    
    private fun matchesCondition(wisata: TempatWisata, condition: FilterCondition): Boolean {
        val fieldValue = when (condition.field) {
            FilterField.NAMA -> wisata.nama
            FilterField.KATEGORI -> wisata.kategoriId
            FilterField.JENIS_TEMPAT -> wisata.jenisTempatId
            FilterField.PROVINSI -> wisata.provinsiId
            FilterField.HARGA -> wisata.harga.toString()
        }
        return fieldValue.contains(condition.value, ignoreCase = true)
    }
}
