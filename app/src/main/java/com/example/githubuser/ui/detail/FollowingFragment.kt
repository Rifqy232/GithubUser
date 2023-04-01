package com.example.githubuser.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.ui.adapter.FollowAdapter
import com.example.githubuser.data.remote.response.FollowResponseItem
import com.example.githubuser.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding

    companion object {
        const val ARG_SECTION_USERNAME = "section_username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_SECTION_USERNAME).toString()
        val factory: ViewModelFactory =
            ViewModelFactory.getInstance(requireActivity())
        val detailViewModel: FollowViewModel by viewModels {
            factory
        }

        detailViewModel.getFollowing(username).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is com.example.githubuser.data.Result.Loading -> binding?.progressBar?.visibility =
                        View.VISIBLE
                    is com.example.githubuser.data.Result.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        val userData = result.data
                        setUserData(userData)
                    }
                    is com.example.githubuser.data.Result.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan: " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        val dividerItemDecoration =
            DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding?.rvFollow?.layoutManager = layoutManager
        binding?.rvFollow?.addItemDecoration(dividerItemDecoration)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUserData(followItem: List<FollowResponseItem>) {
        val followAdapter = FollowAdapter(followItem)
        binding?.rvFollow?.adapter = followAdapter
    }
}