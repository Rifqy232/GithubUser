package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.data.SearchItem
import com.example.githubuser.databinding.FragmentHomeBinding
import com.example.githubuser.viewmodel.MainViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.searchItems.observe(this) {searchItem ->
            setUserData(searchItem)
        }

        mainViewModel.isLoading.observe(this) {isLoading ->
            showLoading(isLoading)
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.option_menu, menu)

                val searchView = menu.findItem(R.id.search).actionView as SearchView

                searchView.queryHint = resources.getString(R.string.search_hint)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        mainViewModel.findUser(query.toString())
                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(view.context)
        val dividerItemDecoration = DividerItemDecoration(view.context, layoutManager.orientation)
        binding.rvHome.layoutManager = layoutManager
        binding.rvHome.addItemDecoration(dividerItemDecoration)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUserData(searchItem: List<SearchItem>) {
        val userAdapter = UserAdapter(searchItem)
        binding.rvHome.adapter = userAdapter
        userAdapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: SearchItem) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra("extra_username", data.login)
                startActivity(intent)
            }
        })
    }
}