package com.example.solutiontestingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.solutiontestingapp.ui.theme.SolutionTestingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SolutionTestingAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppUI()
                }
            }
        }
    }
}

@Composable
fun AppUI() {
    var text by remember{ mutableStateOf("")}
    val updateTextLambdaFn: (String) -> Unit = {text = it}
    ShowScreen(text = text, onTextChange =  updateTextLambdaFn)
}

@Composable
fun ShowScreen(text: String, onTextChange: (String) -> Unit) {
    Box{
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text("Label") }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SolutionTestingAppTheme {
        AppUI()
    }
}