package com.example.ccred_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ccred_3.data.CarbonSavingsData
import com.example.ccred_3.data.DummyCarbonData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarbonSavingsViewModel : ViewModel() {
    private val _carbonData = MutableStateFlow<List<CarbonSavingsData>>(emptyList())
    val carbonData: StateFlow<List<CarbonSavingsData>> = _carbonData.asStateFlow()

    private val _selectedMinistry = MutableStateFlow<CarbonSavingsData?>(null)
    val selectedMinistry: StateFlow<CarbonSavingsData?> = _selectedMinistry.asStateFlow()

    init {
        loadCarbonData()
    }

    private fun loadCarbonData() {
        viewModelScope.launch {
            // Simulate loading from backend
            _carbonData.value = DummyCarbonData.ministriesData
        }
    }

    fun selectMinistry(ministry: CarbonSavingsData) {
        _selectedMinistry.value = ministry
    }

    fun clearSelection() {
        _selectedMinistry.value = null
    }

    fun getTotalCarbonSaved(): Double {
        return _carbonData.value.sumOf { it.totalCarbonSaved }
    }
}
