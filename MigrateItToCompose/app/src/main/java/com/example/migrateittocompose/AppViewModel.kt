package com.example.migrateittocompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AppViewModel: ViewModel() {
    var messageData by mutableStateOf(MainScreenDataModel().message)
        private set

    var counter = 1

    fun updateMessageText(){
        messageData = "Button clicked $counter time(s)"
        counter ++
    }
}