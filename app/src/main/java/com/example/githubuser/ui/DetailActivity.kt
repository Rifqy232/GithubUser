package com.example.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.api.ApiConfig
import com.example.githubuser.data.DetailUserResponse
import com.example.githubuser.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var name: String? = null

    companion object {
        private const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github Detail User"

        val intent = intent
        name = intent.getStringExtra(EXTRA_USERNAME)

        getDetailUser()
    }

    private fun getDetailUser() {
        showLoading(true)
        val client = name?.let { ApiConfig.getApiService().getUserByName(it) }
        client?.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        binding.tvUsername.text = responseBody.login
                        Glide.with(this@DetailActivity)
                            .load(responseBody.avatarUrl)
                            .into(binding.ivUserAvatar)
                        binding.tvFollowing.text = String.format(getString(R.string.following), responseBody.following)
                        binding.tvFollower.text = String.format(getString(R.string.follower), responseBody.followers)
                    }
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}