package com.example.foodie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodie.api.SpoonacularService
import com.example.foodie.data.*
import kotlinx.coroutines.launch

class RecipeSearchViewModel: ViewModel() {
    private val repository = RecipeResultsRepository(SpoonacularService.create())

    private val _searchResults = MutableLiveData<List<Recipe>?>(null)
    val searchResults: LiveData<List<Recipe>?> = _searchResults

    private val _loadingStatus = MutableLiveData(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun searchRecipes(
        query: String,
    ) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            _errorMessage.value = null
            val result = repository.loadRecipes(query, SPOONACULAR_API_KEY)
            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
            _searchResults.value = result.getOrNull()
            _errorMessage.value = result.exceptionOrNull()?.message
        }
    }
}