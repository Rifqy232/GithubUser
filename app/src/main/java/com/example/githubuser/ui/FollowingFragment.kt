package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.FollowAdapter
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.data.FollowResponseItem
import com.example.githubuser.data.SearchItem
import com.example.githubuser.databinding.FragmentFollowingBinding
import com.example.githubuser.viewmodel.DetailViewModel
import com.example.githubuser.viewmodel.DetailViewModelFactory

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var detailViewModel: DetailViewModel

    companion object {
        const val TAG = "FollowingFragment"
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_SECTION_USERNAME = "section_username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
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

        Log.d(TAG, "ViewModel: $detailViewModel")

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        Log.d(TAG, "Index: $index")

        detailViewModel.followings.observe(viewLifecycleOwner) {followingsItem ->
            setUserData(followingsItem)
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            showLoading(isLoading)
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
    }

    private fun setUserData(followItem: List<FollowResponseItem>) {
        val followAdapter = FollowAdapter(followItem)
        binding.rvFollow.adapter = followAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}