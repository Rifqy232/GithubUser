package com.example.githubuser.ui.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.Result
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.remote.response.FollowResponseItem
import com.example.githubuser.di.Injection

class FollowViewModel(private val userRepository: UserRepository, username: String) :
    ViewModel() {
    val followings: LiveData<Result<List<FollowResponseItem>>> = getFollowing(username)
    val followers: LiveData<Result<List<FollowResponseItem>>> = getFollowers(username)

    private fun getFollowing(username: String) = userRepository.getFollowing(username)

    private fun getFollowers(username: String) = userRepository.getFollower(username)

}