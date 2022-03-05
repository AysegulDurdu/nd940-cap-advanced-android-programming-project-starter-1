package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.ResponseState
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import repository.ElectionRepo

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(private val repo: ElectionRepo): ViewModel() {

    private val _upcomingElection = MutableLiveData<List<Election>>()
    val upcomingElection: LiveData<List<Election>>
        get() = _upcomingElection


    val savedElections = repo.savedElections

    private val _showMessage = MutableLiveData<String?>()
    val showMessage: LiveData<String?>
        get() = _showMessage

    private val _navToSelectedElection = MutableLiveData<Election>()
    val navToSelectedElection: LiveData<Election>
        get() = _navToSelectedElection

    init {
        getElections()
    }

    private fun getElections() {
        viewModelScope.launch {
            val electionState = repo.getElections()
            when(electionState) {
                is ResponseState.Success -> {
                    _upcomingElection.value = electionState.data
                }

                is ResponseState.Error -> {
                    _showMessage.value = electionState.message
                }
            }

        }
    }

    fun displayElectionDetails(election: Election) {
        _navToSelectedElection.value = election
    }

    fun displaElectionDetailsComplete() {
        _navToSelectedElection.value = null
    }

}