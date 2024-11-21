package com.cevdetkilickeser.recyclerviewdiffutil

import androidx.recyclerview.widget.RecyclerView
import com.cevdetkilickeser.recyclerviewdiffutil.databinding.ItemUserBinding

class UserViewHolder(private var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        with(binding) {
            tvName.text = user.name
            tvAge.text = user.age.toString()
        }
    }
}