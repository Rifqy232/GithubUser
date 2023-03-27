package com.example.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.api.ApiConfig
import com.example.githubuser.data.DetailUserResponse
import com.example.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var name: String? = null

    companion object {
        private const val EXTRA_USERNAME = "extra_username"
        private const val TAG = "DetailActivity"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github Detail User"

        val intent = intent
        name = intent.getStringExtra(EXTRA_USERNAME)

        getDetailUser()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, name)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
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
                        binding.tvName.text = responseBody.name
                        binding.tvFollowing.text = String.format(getString(R.string.following), responseBody.following)
                        binding.tvFollower.text = String.format(getString(R.string.follower), responseBody.followers)
                    }
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "error: ${t.message}")
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