package com.example.githubuser.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.ui.adapter.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding

    private var isFavorite: Boolean = false

    companion object {
        private const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val intent = intent
        val name = intent.getStringExtra(EXTRA_USERNAME).toString()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@DetailActivity)
        val detailViewModel: DetailViewModel by viewModels {
            factory
        }

        showSectionsPager()

        detailViewModel.getDetailUser(name).observe(this@DetailActivity) { result ->
            if (result != null) {
                when (result) {
                    is com.example.githubuser.data.Result.Loading -> binding?.progressBar?.visibility =
                        View.VISIBLE
                    is com.example.githubuser.data.Result.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        val userData = result.data
                        userData.let { user ->
                            binding?.apply {
                                Glide.with(this@DetailActivity)
                                    .load(user.avatarUrl)
                                    .into(ivUserAvatar)
                                tvUsername.text = user.login
                                tvName.text = user.name
                                tvFollower.text =
                                    String.format(getString(R.string.follower), user.followers)
                                tvFollowing.text =
                                    String.format(getString(R.string.following), user.following)
                            }
                        }
                    }
                    is com.example.githubuser.data.Result.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan: " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        supportActionBar?.apply {
            title = "Github Detail User"
            elevation = 0f
        }

        detailViewModel.getFavoriteUserByUsername(name).observe(this) { result ->
            if (result != null) {
                isFavorite = result.isFavorite
                if (isFavorite) {
                    binding?.fabFavorite?.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding?.fabFavorite?.context!!,
                            R.drawable.baseline_favorite_24
                        )
                    )
                } else {
                    binding?.fabFavorite?.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding?.fabFavorite?.context!!,
                            R.drawable.baseline_favorite_border_24
                        )
                    )
                }
            }
        }

        binding?.fabFavorite?.setOnClickListener {
            detailViewModel.insertUser(name)
            detailViewModel.getDetailUser(name).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is com.example.githubuser.data.Result.Loading -> binding?.favProgressBar?.visibility = View.VISIBLE
                        is com.example.githubuser.data.Result.Success -> {
                            val user = UserEntity(
                                result.data.login,
                                result.data.avatarUrl,
                                isFavorite,
                            )
                            if (user.isFavorite) {
                                detailViewModel.deleteFavoriteUser(user)
                            } else {
                                detailViewModel.saveFavoriteUser(user)
                            }
                        }
                        is com.example.githubuser.data.Result.Error -> {
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan: " + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showSectionsPager() {
        val intent = intent
        val name = intent.getStringExtra(EXTRA_USERNAME).toString()

        val sectionsPagerAdapter = SectionsPagerAdapter(this@DetailActivity, name)
        val viewPager: ViewPager2? = binding?.viewPager
        viewPager?.adapter = sectionsPagerAdapter
        val tabs: TabLayout? = binding?.tabs
        TabLayoutMediator(tabs!!, viewPager!!) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}