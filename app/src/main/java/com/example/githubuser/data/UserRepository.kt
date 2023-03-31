package com.example.githubuser.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.githubuser.data.remote.response.FollowResponseItem
import com.example.githubuser.data.remote.response.SearchItem
import com.example.githubuser.data.remote.response.SearchResponse
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.githubuser.data.remote.retrofit.ApiService
import com.example.githubuser.ui.detail.DetailViewModel
import com.example.githubuser.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val appExecutors: AppExecutors,
) {
    private val searchResult = MediatorLiveData<Result<List<SearchItem>>>()
    private val followingResult = MediatorLiveData<Result<List<FollowResponseItem>>>()
    private val followerResult = MediatorLiveData<Result<List<FollowResponseItem>>>()

    fun getFollower(username: String): LiveData<Result<List<FollowResponseItem>>> {
        followerResult.value = Result.Loading
        val client = apiService.getFollowers(username)
        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                if (response.isSuccessful) {
                    followerResult.value = Result.Success(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                followerResult.value = Result.Error(t.message.toString())
            }

        })

        return followerResult
    }

    fun getFollowing(username: String): LiveData<Result<List<FollowResponseItem>>> {
        followingResult.value = Result.Loading
        val client = apiService.getFollowing(username)
        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                if (response.isSuccessful) {
                    followingResult.value = Result.Success(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                followingResult.value = Result.Error(t.message.toString())
            }

        })

        return followingResult
    }

    fun getSearchedUser(username: String): LiveData<Result<List<SearchItem>>> {
        searchResult.value = Result.Loading
        val client = apiService.getUser(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    appExecutors.mainThread.execute {
                        searchResult.value = Result.Success(response.body()!!.items)
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                searchResult.value = Result.Error(t.message.toString())
            }

        })
        return searchResult
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, appExecutors)
            }.also { instance = it }
    }
}