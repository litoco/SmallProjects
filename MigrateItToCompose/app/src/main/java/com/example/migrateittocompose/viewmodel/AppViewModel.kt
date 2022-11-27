package com.example.migrateittocompose.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import com.example.migrateittocompose.repository.Repository
import com.example.migrateittocompose.repository.localstorage.RoomLocalDatabase

class AppViewModel(application: Application) : AndroidViewModel(application) {
    var messageData by mutableStateOf("Hi, there!!!\nClick on the button below to get your userId")
    private val database by lazy { RoomLocalDatabase.getDatabase(application) }
    private val repository by lazy { Repository(database.getRoomDAO()) }


    fun getUserId(lifecycleOwner: LifecycleOwner) {
        repository.getUserId(lifecycleOwner, this).observe(lifecycleOwner){
            messageData = if (!it.isNullOrEmpty()){
                "Your userId is: $it"
            } else{
                "Sorry!!\nCan't generate your userId at the moment\nPlease retry"
            }
        }
    }
}