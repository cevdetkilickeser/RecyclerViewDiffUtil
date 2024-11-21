package com.cevdetkilickeser.recyclerviewdiffutil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cevdetkilickeser.recyclerviewdiffutil.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val userAdapterDiffUtil: UserAdapterDiffUtil by lazy {
        UserAdapterDiffUtil()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.adapter = userAdapterDiffUtil

        val userList = listOf(
            User(1, "Ahmet", 25),
            User(2, "Ayşe", 30),
            User(3, "Mehmet", 20)
        )
        userAdapterDiffUtil.updateList(userList)

        val updatedList = listOf(
            User(1, "Ahmet", 26),
            User(2, "Ayşe", 32),
            User(3, "Mehmet", 20),
            User(4, "Fatma", 22)
        )

        binding.buttonReset.setOnClickListener {
            Log.d("Log of RecyclerView", "Reset clicked.")
            userAdapterDiffUtil.updateList(userList)
        }

        binding.buttonUpdate.setOnClickListener {
            Log.d("Log of RecyclerView", "Update clicked.")
            userAdapterDiffUtil.updateList(updatedList)
        }

        binding.buttonNextPage.setOnClickListener {
            Log.d("Log of RecyclerView", "Second Activity clicked.")
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}