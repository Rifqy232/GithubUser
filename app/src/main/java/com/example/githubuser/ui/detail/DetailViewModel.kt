package com.example.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.remote.response.DetailUserResponse

class DetailViewModel(private val userRepository: UserRepository, private val username: String
) : ViewModel() {

    fun getDetailUser(name: String) = userRepository.getDetailUser(name)

}