package com.example.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.FollowAdapter
import com.example.githubuser.data.FollowResponseItem
import com.example.githubuser.databinding.FragmentFollowerBinding
import com.example.githubuser.viewmodel.DetailViewModel
import com.example.githubuser.viewmodel.DetailViewModelFactory

class FollowerFragment : Fragment() {
    private lateinit var binding: FragmentFollowerBinding
    private lateinit var detailViewModel: DetailViewModel

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_SECTION_USERNAME = "section_username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = arguments?.getString(ARG_SECTION_USERNAME)
        val factory = DetailViewModelFactory(username.toString())
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailViewModel.followers.observe(viewLifecycleOwner) {followersItem ->
            setUserData(followersItem)
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            showLoading(isLoading)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        val dividerItemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollow.layoutManager = layoutManager
        binding.rvFollow.addItemDecoration(dividerItemDecoration)
    }

    private fun setUserData(followItem: List<FollowResponseItem>) {
        val followAdapter = FollowAdapter(followItem)
        binding.rvFollow.adapter = followAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}