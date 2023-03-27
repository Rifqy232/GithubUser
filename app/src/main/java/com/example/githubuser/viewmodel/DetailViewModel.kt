package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.api.ApiConfig
import com.example.githubuser.data.FollowResponse
import com.example.githubuser.data.FollowResponseItem
import com.example.githubuser.data.SearchItem
import com.example.githubuser.data.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(username: String) : ViewModel() {
    companion object {
        private const val TAG = "DetailViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _followings = MutableLiveData<List<FollowResponseItem>>()
    val followings: MutableLiveData<List<FollowResponseItem>> = _followings

    private val _followers = MutableLiveData<List<FollowResponseItem>>()
    val followers: MutableLiveData<List<FollowResponseItem>> = _followers

    init {
        getFollowing(username)
        getFollowers(username)
    }

    private fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followings.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "error: ${t.message}")
            }

        })
    }

    private fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followers.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "error: ${t.message}")
            }

        })
    }
}

class DetailViewModelFactory(private val username: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return DetailViewModel(username) as T
    }
}