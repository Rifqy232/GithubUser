package com.example.githubuser.ui.detail

import androidx.lifecycle.ViewModel
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.local.entity.UserEntity

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getDetailUser(name: String) = userRepository.getDetailUser(name)

    fun insertUser(name: String) = userRepository.insertUser(name)

    fun getFavoriteUserByUsername(username: String) = userRepository.getFavoriteUserByUsername(username)

    fun saveFavoriteUser(user: UserEntity) {
        userRepository.setFavoriteUser(user, true)
    }

    fun deleteFavoriteUser(user: UserEntity) {
        userRepository.setFavoriteUser(user, false)
    }

}