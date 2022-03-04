package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.ResponseState
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import repository.ElectionRepo

class RepresentativeViewModel(private val repository: ElectionRepo): ViewModel() {


    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    var address = MutableLiveData<Address>()

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage


    init {
        address.value = Address("","","","","")
    }

    fun getRepresentativesByAddress(address: Address) {
        viewModelScope.launch {
            val response = repository.getRepresentative(address.toFormattedString())
            when (response) {
                is ResponseState.Success -> {
                    _representatives.value = response.data
                }
                is ResponseState.Error -> {
                    _errorMessage.value = response.message
                }
            }
        }
    }

    fun findRepresentatives() {
        address.value?.let { addressValue ->
            getRepresentativesByAddress(addressValue)
        }
    }

    fun useLocation(adressValue: Address) {
        address.value = adressValue
        findRepresentatives()

    }

}
