package com.example.githubuser.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.data.remote.response.SearchItem
import com.example.githubuser.data.remote.response.SearchResponse
import com.example.githubuser.di.Injection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    val searchResult:  LiveData<com.example.githubuser.data.Result<List<SearchItem>>> = getSearchedUser()

    fun getSearchedUser(username: String = "rifqy") = userRepository.getSearchedUser(username)
}