package com.example.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.ui.detail.DetailActivity

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
        val favoriteAdapter = FavoriteAdapter(userList)
        binding?.rvFavorite?.adapter = favoriteAdapter
        favoriteAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, data.username)
                startActivity(intent)
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val EXTRA_USERNAME = "extra_username"
    }
}