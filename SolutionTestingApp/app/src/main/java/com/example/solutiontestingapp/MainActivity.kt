package com.example.solutiontestingapp

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.solutiontestingapp.ui.theme.SolutionTestingAppTheme
import kotlinx.coroutines.launch

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

    Column (modifier = Modifier.fillMaxSize(1f)) {
        var text by remember { mutableStateOf("") }
        val listScrollStateHolder = rememberLazyListState()
        val list = (0..99).toList()
        val scope = rememberCoroutineScope()
        val keyboardVisible by rememberIsKeyboardOpen()
        var firstVisibleItemIndexedValue by remember { mutableStateOf(0)}
        var screenSize by remember { mutableStateOf(0)}
        var scrollOffset by remember{ mutableStateOf(0)}

        Box (modifier = Modifier
            .weight(1f)){
            LazyColumn(state = listScrollStateHolder){
                items(list){ item ->
                    Text(modifier =  Modifier.fillMaxWidth(1f), text = "Item number: ${item + 1}", textAlign = TextAlign.Center)
                }
            }
        }

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Label") },
            modifier = Modifier
                .fillMaxWidth(1f)
                .focusable(true)
        )

        LaunchedEffect(key1 = keyboardVisible){
            if (listScrollStateHolder.layoutInfo.visibleItemsInfo.size - screenSize > 10){
                screenSize = listScrollStateHolder.layoutInfo.visibleItemsInfo.size
            }
            if(keyboardVisible){
                firstVisibleItemIndexedValue = listScrollStateHolder.firstVisibleItemIndex
                scrollOffset = if(screenSize - listScrollStateHolder.layoutInfo.visibleItemsInfo.size > 0) screenSize + 1 - listScrollStateHolder.layoutInfo.visibleItemsInfo.size else 0
                scope.launch {
                    listScrollStateHolder.animateScrollToItem(firstVisibleItemIndexedValue + scrollOffset)
                }
            } else{
                if (listScrollStateHolder.firstVisibleItemIndex + screenSize + 2 < list.size) {
                    firstVisibleItemIndexedValue = if (listScrollStateHolder.firstVisibleItemIndex - scrollOffset > 0)  listScrollStateHolder.firstVisibleItemIndex - scrollOffset else 0
                    scope.launch {
                        listScrollStateHolder.animateScrollToItem(firstVisibleItemIndexedValue)
                    }
                }
            }
        }
    }
}

@Composable
fun rememberIsKeyboardOpen(): State<Boolean> {
    val view = LocalView.current
    return produceState(initialValue = view.isKeyboardOpen()) {
        val viewTreeObserver = view.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener { value = view.isKeyboardOpen() }
        viewTreeObserver.addOnGlobalLayoutListener(listener)
        awaitDispose { viewTreeObserver.removeOnGlobalLayoutListener(listener)  }
    }
}


fun View.isKeyboardOpen(): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect)
    val screenHeight = rootView.height
    val keypadHeight = screenHeight - rect.bottom
    return keypadHeight > screenHeight * 0.1
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SolutionTestingAppTheme {
        AppUI()
    }
}