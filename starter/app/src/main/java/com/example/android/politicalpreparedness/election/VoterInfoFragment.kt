package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import repository.ElectionRepo

class VoterInfoFragment : Fragment() {

    val db = ElectionDatabase.getInstance(requireContext())
    val repo = ElectionRepo(db)

    val viewModel: VoterInfoViewModel by viewModels(factoryProducer = { VoterInfoViewModelFactory(repo) })
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val args: VoterInfoFragmentArgs by navArgs()
        val electionId = args.argElectionId
        val division = args.argDivision


        val binding =
            DataBindingUtil.inflate<FragmentVoterInfoBinding>(inflater, R.layout.fragment_voter_info, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observe()

        viewModel.getVoterInfo(division, electionId)
        viewModel.getElectionRepo(electionId)


        viewModel.savedElection.observe(viewLifecycleOwner, Observer { election ->
            if (election == null) {
                binding.followElectionButton.text = getString(R.string.follow_election)
            } else {
                binding.followElectionButton.text = getString(R.string.unfollow_election)
            }
        })

        return binding.root
    }

    fun observe() {
        viewModel.votingLocationUrl.observe(viewLifecycleOwner, Observer { url ->
            url?.let {
                loadUrlIntent(it)
            }
        })
        viewModel.electionUrl.observe(viewLifecycleOwner, Observer { url ->
            url?.let {
                loadUrlIntent(it)
            }
        })
        viewModel.ballotoUrl.observe(viewLifecycleOwner, Observer { url ->
            url?.let {
                loadUrlIntent(it)
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        })

    }

    fun loadUrlIntent(urlString: String) {
        val uri = Uri.parse(urlString)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}