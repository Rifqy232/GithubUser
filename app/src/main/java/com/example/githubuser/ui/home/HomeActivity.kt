package com.example.githubuser.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.Result
import com.example.githubuser.data.remote.response.SearchItem
import com.example.githubuser.databinding.ActivityHomeBinding
import com.example.githubuser.ui.detail.DetailActivity
import com.example.githubuser.ui.favorite.FavoriteActivity
import com.example.githubuser.ui.settings.SettingPreferences
import com.example.githubuser.ui.settings.SettingsActivity
import com.example.githubuser.ui.settings.SettingsViewModel
import com.example.githubuser.ui.settings.SettingsViewModelFactory

class HomeActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val factory: HomeViewModelFactory = HomeViewModelFactory.getInstance(this@HomeActivity)
        val homeViewModel: HomeViewModel by viewModels {
            factory
        }

        val pref = SettingPreferences.getInstance(dataStore)
        val settingsViewModel =
            ViewModelProvider(this, SettingsViewModelFactory(pref))[SettingsViewModel::class.java]

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        homeViewModel.searchResult.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        val userData = result.data
                        setUserData(userData)
                    }
                    is Result.Error -> {
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

        val layoutMan = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, layoutMan.orientation)
        binding?.rvHome?.apply {
            layoutManager = layoutMan
            addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val factory: HomeViewModelFactory = HomeViewModelFactory.getInstance(this@HomeActivity)
        val homeViewModel: HomeViewModel by viewModels {
            factory
        }

        val searchView = menu?.findItem(R.id.option_search)?.actionView as SearchView
        val favoriteMenu = menu.findItem(R.id.option_favorite)
        val settingsMenu = menu.findItem(R.id.option_setting)

        searchView.apply {
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    homeViewModel.getSearchedUser(query.toString())
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        favoriteMenu.setOnMenuItemClickListener(this)
        settingsMenu.setOnMenuItemClickListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    private fun setUserData(searchItem: List<SearchItem>) {
        val userAdapter = UserAdapter(searchItem)
        binding?.rvHome?.adapter = userAdapter
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: SearchItem) {
                val intent = Intent(this@HomeActivity, DetailActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, data.login)
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

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_favorite -> {
                Log.d("TESS", "Fav")
                val favoriteIntent = Intent(this@HomeActivity, FavoriteActivity::class.java)
                startActivity(favoriteIntent)
            }
            R.id.option_setting -> {
                Log.d("TESS", "Settings")
                val settingsIntent = Intent(this@HomeActivity, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
        }

        return true
    }
}