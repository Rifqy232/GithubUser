package com.example.githubuser.ui.detail

import androidx.lifecycle.ViewModel
import com.example.githubuser.data.UserRepository

class FollowViewModel(private val userRepository: UserRepository) :
    ViewModel() {

    fun getFollowing(username: String) = userRepository.getFollowing(username)

    fun getFollowers(username: String) = userRepository.getFollower(username)

}