package com.example.githubuser.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.ui.adapter.UserAdapter
import com.example.githubuser.data.remote.response.SearchItem
import com.example.githubuser.databinding.ActivityHomeBinding
import com.example.githubuser.ui.detail.DetailActivity

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding

    private val factory: HomeViewModelFactory = HomeViewModelFactory.getInstance(this)
    val homeViewModel: HomeViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        homeViewModel.searchResult.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is com.example.githubuser.data.Result.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is com.example.githubuser.data.Result.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        val userData = result.data
                        setUserData(userData)
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

        val layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvHome?.layoutManager = layoutManager
        binding?.rvHome?.addItemDecoration(dividerItemDecoration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                homeViewModel.getSearchedUser(query.toString())
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

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
}