package com.example.migrateittocompose.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.migrateittocompose.repository.localstorage.RoomDAO
import com.example.migrateittocompose.repository.localstorage.UserId
import com.example.migrateittocompose.viewmodel.AppViewModel
import kotlinx.coroutines.launch
import kotlin.random.Random

class Repository(private val roomDAO: RoomDAO) {
    fun getUserId(lifecycleOwner: LifecycleOwner, appViewModel: AppViewModel): LiveData<String> {
        val userIdLiveData = MutableLiveData("")
        roomDAO.getUserId().observe(lifecycleOwner){
            if (it.isNullOrEmpty()){
                appViewModel.messageData = "Generating userId please wait.."
                var string = ""
                for (i in (1..12)){
                    val isCapital = Random.nextBoolean()
                    string += if (isCapital){
                        ('A'..'Z').random()
                    } else {
                        ('a'..'z').random()
                    }
                }
                appViewModel.viewModelScope.launch {
                    insertToLocalStorage(string)
                }
                userIdLiveData.value = string
            } else {
                userIdLiveData.value = it
            }
        }
        return userIdLiveData
    }

    @WorkerThread
    private suspend fun insertToLocalStorage(string: String) {
        val userId = UserId(0, string)
        roomDAO.insertUserId(userId = userId)
    }
}