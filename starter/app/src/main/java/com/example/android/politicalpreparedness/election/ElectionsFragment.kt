package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.google.android.material.snackbar.Snackbar
import repository.ElectionRepo

class ElectionsFragment: Fragment() {

    //TODO: Declare ViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val db = ElectionDatabase.getInstance(requireContext())
        val repo = ElectionRepo(db)
        val viewModel by viewModels<ElectionsViewModel>(factoryProducer = { ElectionsViewModelFactory(repo) })
        val binding: FragmentElectionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)


        val upcomingElectionListAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.displayElectionDetails(election)
        })

        binding.upcomingElectionsRv.adapter = upcomingElectionListAdapter
        viewModel.upcomingElection.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                upcomingElectionListAdapter.submitList(list)
            }
        })


        val savedElectionListAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.displayElectionDetails(election)
        })
        binding.savedElectionsRv.adapter = savedElectionListAdapter

        viewModel.savedElections.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                savedElectionListAdapter.submitList(list)
            }
        })

        viewModel.navToSelectedElection.observe(viewLifecycleOwner, Observer { election ->
            election?.let {
                findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division))
                viewModel.displaElectionDetailsComplete()
            }
        })


        viewModel.showMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
            }

        })

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}