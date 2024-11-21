package com.cevdetkilickeser.recyclerviewdiffutil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cevdetkilickeser.recyclerviewdiffutil.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private val binding: ActivitySecondBinding by lazy {
        ActivitySecondBinding.inflate(layoutInflater)
    }
    private val userAdapterNotifyDataChange: UserAdapterNotifyDataChange by lazy {
        UserAdapterNotifyDataChange()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.adapter = userAdapterNotifyDataChange

        val userList = listOf(
            User(1, "Ahmet", 25),
            User(2, "Ayşe", 30),
            User(3, "Mehmet", 20)
        )
        userAdapterNotifyDataChange.updateList(userList)

        val updatedList = listOf(
            User(1, "Ahmet", 26),
            User(2, "Ayşe", 32),
            User(3, "Mehmet", 20),
            User(4, "Fatma", 22)
        )

        binding.buttonReset.setOnClickListener {
            userAdapterNotifyDataChange.updateList(userList)
        }

        binding.buttonUpdate.setOnClickListener {
            userAdapterNotifyDataChange.updateList(updatedList)
        }
    }
}