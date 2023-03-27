package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.FollowResponseItem
import com.example.githubuser.databinding.UserItemBinding

class FollowAdapter(private val listUser: List<FollowResponseItem>) : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {
    class ViewHolder(private var binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindUser(user: FollowResponseItem) {
            binding.tvUser.text = user.login
            Glide.with(itemView)
                .load(user.avatarUrl)
                .into(binding.ivUserAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUser(listUser[position])
    }
}