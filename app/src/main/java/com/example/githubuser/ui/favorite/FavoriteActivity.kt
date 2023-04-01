package com.example.githubuser.ui.favorite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val factory: FavoriteViewModelFactory = FavoriteViewModelFactory.getInstance(this)
        val favoriteViewModel: FavoriteViewModel by viewModels {
            factory
        }

        favoriteViewModel.getFavoriteUsers().observe(this) { result ->
            setUserData(result)
        }

        val layoutMan = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, layoutMan.orientation)
        binding?.rvFavorite?.apply {
            layoutManager = layoutMan
            addItemDecoration(dividerItemDecoration)
        }

        supportActionBar?.title = "Favorite Users"
    }

    private fun setUserData(userList: List<UserEntity>) {
        val userAdapter = FavoriteAdapter(userList)
        binding?.rvFavorite?.adapter = userAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}