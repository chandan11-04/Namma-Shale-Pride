package com.nammashale.shalepride.ui.facility

import androidx.lifecycle.*
import com.nammashale.shalepride.data.model.Facility
import com.nammashale.shalepride.data.repository.FacilityRepository
import kotlinx.coroutines.launch

class FacilityViewModel(private val facilityRepository: FacilityRepository) : ViewModel() {

    val allFacilities: LiveData<List<Facility>> = facilityRepository.getAllFacilities().asLiveData()

    private val _selectedCategory = MutableLiveData<String>("")
    val selectedCategory: LiveData<String> = _selectedCategory

    fun getFacilitiesByCategory(category: String): LiveData<List<Facility>> {
        return facilityRepository.getFacilitiesByCategory(category).asLiveData()
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun addFacility(name: String, description: String, imageUrl: String, category: String) {
        viewModelScope.launch {
            facilityRepository.addFacility(name, description, imageUrl, category)
        }
    }

    fun deleteFacility(id: Long) {
        viewModelScope.launch {
            facilityRepository.deleteFacility(id)
        }
    }
}
