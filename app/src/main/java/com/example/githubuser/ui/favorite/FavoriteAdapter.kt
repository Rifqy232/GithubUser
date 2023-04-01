package com.example.githubuser.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.local.entity.UserEntity
import com.example.githubuser.databinding.FavoriteItemBinding

class FavoriteAdapter(private val listFavoriteUsers: List<UserEntity>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    class ViewHolder(private var binding: FavoriteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity) {
            binding.tvUser.text = user.username
            Glide.with(itemView)
                .load(user.avatarUrl)
                .into(binding.ivUserAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavoriteUsers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavoriteUsers[position])
    }
}