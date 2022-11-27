package com.example.migrateittocompose.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.migrateittocompose.repository.Repository

class AppViewModel: ViewModel() {
    var messageData by mutableStateOf("Hi, there!!!\nClick on the button below to get your userId")


    fun getUserId(repository: Repository, lifecycleOwner: LifecycleOwner) {
        repository.getUserId(lifecycleOwner, this).observe(lifecycleOwner){
            messageData = if (!it.isNullOrEmpty()){
                "Your userId is: $it"
            } else{
                "Sorry!!\nCan't generate your userId at the moment\nPlease retry"
            }
        }
    }
}