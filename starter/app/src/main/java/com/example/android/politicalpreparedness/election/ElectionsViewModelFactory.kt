package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import repository.ElectionRepo

class ElectionsViewModelFactory(private val repo: ElectionRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            return ElectionsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }


}