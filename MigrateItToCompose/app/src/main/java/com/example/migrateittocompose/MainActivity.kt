package com.example.migrateittocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.migrateittocompose.repository.Repository
import com.example.migrateittocompose.repository.localstorage.RoomLocalDatabase
import com.example.migrateittocompose.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database by lazy { RoomLocalDatabase.getDatabase(this) }
        val repository by lazy { Repository(database.getRoomDAO()) }

        setContent {
            MaterialTheme {
                MainScreen(repository)
            }
        }
    }
}

@Composable
fun MainScreen(
    repository: Repository,
    viewModel: AppViewModel = viewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    ShowUI(text = viewModel.messageData) { viewModel.getUserId(repository, lifecycleOwner) }
}

@Composable
fun ShowUI(text:String, getUserId: ()-> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, modifier = Modifier.background(color = Color.Cyan))
        Button(onClick = getUserId ) {
            Text(text = "Click me")
        }

    }
}
