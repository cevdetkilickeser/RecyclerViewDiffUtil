package com.cevdetkilickeser.recyclerviewdiffutil

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cevdetkilickeser.recyclerviewdiffutil.databinding.ItemUserBinding

class UserAdapterNotifyDataChange : RecyclerView.Adapter<UserViewHolder>() {
    private var userList: List<User> = emptyList()

    fun updateList(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(layoutInflater)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
        Log.d("Log of RecyclerView", "Yeniden Çizilen Kullanıcı -> ${user.name}")
    }

    override fun getItemCount(): Int = userList.size
}