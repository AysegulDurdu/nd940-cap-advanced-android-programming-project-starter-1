package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.ResponseState
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import repository.ElectionRepo

class VoterInfoViewModel(private val repo: ElectionRepo) : ViewModel() {

    val voterAdministrationBody = MutableLiveData<AdministrationBody>()

    private val _electionUrl = MutableLiveData<String>()
    val electionUrl: LiveData<String>
        get() = _electionUrl

    private val _votingLocationUrl = MutableLiveData<String>()
    val votingLocationUrl: LiveData<String>
        get() = _votingLocationUrl

    private val _ballotoUrl = MutableLiveData<String>()
    val ballotoUrl: LiveData<String>
        get() = _ballotoUrl

    val election = MutableLiveData<Election>()
    val savedElection = MutableLiveData<Election>()

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage


    fun getVoterInfo(division: Division, electionId: Int) {
        val address = "${division.state} ${division.country}"
        viewModelScope.launch {
            val voterResponse = repo.getVoterInfo(address, electionId)
            when (voterResponse) {
                is ResponseState.Success -> {
                    election.value = voterResponse.data.election
                    voterAdministrationBody.value = voterResponse.data.state?.get(0)?.electionAdministrationBody
                }
                is ResponseState.Error -> {
                    _errorMessage.value = voterResponse.message
                }
            }
        }
    }

    fun setBallotUrl() {
        _ballotoUrl.value = voterAdministrationBody.value?.ballotInfoUrl
    }

    fun getElectionRepo(electionId: Int) {
        viewModelScope.launch {
            savedElection.value = repo.getElectionById(electionId)
        }
    }

    fun setVotingLocationUrl() {
        _votingLocationUrl.value = voterAdministrationBody.value?.votingLocationFinderUrl
    }

    fun setElectionUrl() {
        _electionUrl.value = voterAdministrationBody.value?.electionInfoUrl
    }

    fun followElectionButton() {
        viewModelScope.launch {
            if (savedElection.value == null) {
                election.value?.let { electionValue ->
                    repo.saveElection(electionValue)
                    savedElection.value = electionValue
                }
            } else {
                election.value?.let { electionValue ->
                    savedElection.value = null
                    repo.deleteElection(electionValue)
                }
            }
        }
    }
}