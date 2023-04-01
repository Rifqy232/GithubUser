package com.example.githubuser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.remote.response.SearchItem

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    val searchResult:  LiveData<com.example.githubuser.data.Result<List<SearchItem>>> = getSearchedUser()

    fun getSearchedUser(username: String = "rifqy") = userRepository.getSearchedUser(username)
}