package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import repository.ElectionRepo

class RepresentativeViewModelFactory(private val repo: ElectionRepo) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            return RepresentativeViewModel(repo) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}