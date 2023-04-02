package com.example.githubuser.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.data.local.room.UserDao
import com.example.githubuser.data.remote.response.DetailUserResponse
import com.example.githubuser.data.remote.response.FollowResponseItem
import com.example.githubuser.data.remote.response.SearchItem
import com.example.githubuser.data.remote.response.SearchResponse
import com.example.githubuser.data.remote.retrofit.ApiService
import com.example.githubuser.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val appExecutors: AppExecutors,
) {
    private val searchResult = MutableLiveData<Result<List<SearchItem>>>()
    private val followingResult = MutableLiveData<Result<List<FollowResponseItem>>>()
    private val followerResult = MutableLiveData<Result<List<FollowResponseItem>>>()
    private val detailUserResult = MutableLiveData<Result<DetailUserResponse>>()
    private val userResult = MediatorLiveData<Result<UserEntity>>()

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

    fun getDetailUser(username: String): LiveData<Result<DetailUserResponse>> {
        detailUserResult.value = Result.Loading
        val client = apiService.getUserByName(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    detailUserResult.value = Result.Success(response.body()!!)
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                detailUserResult.value = Result.Error(t.message.toString())
            }
        })

        return detailUserResult
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

    fun insertUser(username: String): LiveData<Result<UserEntity>> {
        val client = apiService.getUserByName(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    appExecutors.diskIO.execute {
                        val isFavorite = userDao.isUserFavorite(userResponse!!.login)
                        val user = UserEntity(
                            username = userResponse.login,
                            avatarUrl = userResponse.avatarUrl,
                            isFavorite = isFavorite,
                        )
                        userDao.deleteAll()
                        userDao.insertUsers(user)
                    }
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                userResult.value = Result.Error(t.message.toString())
            }
        })
        return userResult
    }

    fun getFavoriteUserByUsername(username: String): LiveData<UserEntity> {
        return userDao.getFavoriteUserByUsername(username)
    }

    fun getFavoriteUsers(): LiveData<List<UserEntity>> {
        return userDao.getFavoriteUsers()
    }

    fun setFavoriteUser(user: UserEntity, favoriteState: Boolean) {
        appExecutors.diskIO.execute {
            user.isFavorite = favoriteState
            userDao.updateUser(user)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            dao: UserDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, dao, appExecutors)
            }.also { instance = it }
    }
}