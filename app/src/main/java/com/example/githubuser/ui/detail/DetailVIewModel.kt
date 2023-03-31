package com.example.githubuser.ui.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.remote.response.FollowResponseItem
import com.example.githubuser.di.Injection

class DetailViewModel(private val userRepository: UserRepository, private val username: String
) : ViewModel() {

    val followings: LiveData<com.example.githubuser.data.Result<List<FollowResponseItem>>> = getFollowing(username)
    val followers: LiveData<com.example.githubuser.data.Result<List<FollowResponseItem>>> = getFollowers(username)

    private fun getFollowing(username: String) = userRepository.getFollowing(username)

    private fun getFollowers(username: String) = userRepository.getFollower(username)
}

class DetailViewModelFactory(private val userRepository: UserRepository, private val username: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(userRepository, username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailViewModelFactory? = null
        fun getInstance(context: Context, username: String): DetailViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailViewModelFactory(Injection.provideRepository(context), username)
            }.also { instance = it }
    }
}