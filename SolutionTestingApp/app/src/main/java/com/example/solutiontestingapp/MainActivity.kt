package com.example.solutiontestingapp

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import kotlinx.coroutines.CoroutineScope
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
        val list = (0..99).toList()
        val scope = rememberCoroutineScope()
        val listScrollStateHolder = rememberLazyListState(initialFirstVisibleItemIndex = list.size - 1)
        val isKeyboardVisibleBefore = remember{ mutableStateOf(false)}
        val lastVisibleItemIndex = remember{ mutableStateOf(0)}
        val listItemAbsoluteSizeList = remember { (1..list.size).map { 0 }.toMutableList()}

        rememberIsKeyboardOpen(scope, listScrollStateHolder, isKeyboardVisibleBefore, lastVisibleItemIndex, listItemAbsoluteSizeList)

        Box (modifier = Modifier
            .weight(1f)){
            LazyColumn(state = listScrollStateHolder, modifier = Modifier){
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
    }
}

@Composable
fun rememberIsKeyboardOpen(
    scope: CoroutineScope,
    listScrollStateHolder: LazyListState,
    isKeyboardVisibleBefore: MutableState<Boolean>,
    lastVisibleItemIndex: MutableState<Int>,
    listItemAbsoluteSizeList: MutableList<Int>
): State<Boolean> {
    val view = LocalView.current
    return produceState(initialValue = view.isKeyboardOpen(scope, listScrollStateHolder, isKeyboardVisibleBefore, lastVisibleItemIndex, listItemAbsoluteSizeList)) {
        val viewTreeObserver = view.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener { value = view.isKeyboardOpen(
            scope,
            listScrollStateHolder,
            isKeyboardVisibleBefore,
            lastVisibleItemIndex,
            listItemAbsoluteSizeList
        )
        }
        viewTreeObserver.addOnGlobalLayoutListener(listener)
        awaitDispose { viewTreeObserver.removeOnGlobalLayoutListener(listener)  }
    }
}


fun View.isKeyboardOpen(
    scope: CoroutineScope,
    listScrollStateHolder: LazyListState,
    isKeyboardVisibleBefore: MutableState<Boolean>,
    lastVisibleItemIndex: MutableState<Int>,
    listItemAbsoluteSizeList: MutableList<Int>
): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect)
    val screenHeight = rootView.height
    val absoluteKeyboardHeight = if (rect.bottom > 0) screenHeight - rect.bottom else 0
    val visibleItemList = listScrollStateHolder.layoutInfo.visibleItemsInfo
    if(visibleItemList.isNotEmpty()){
        if(absoluteKeyboardHeight > 100){
            if (!isKeyboardVisibleBefore.value){
                scope.launch { listScrollStateHolder.scrollBy(absoluteKeyboardHeight.toFloat()) }
                isKeyboardVisibleBefore.value = true
            }
            lastVisibleItemIndex.value = visibleItemList[visibleItemList.size-1].index
            listItemAbsoluteSizeList[visibleItemList[visibleItemList.size-1].index] = visibleItemList[visibleItemList.size-1].size
        } else {
            if (isKeyboardVisibleBefore.value) {
                var scrollOffset = 0
                var i = listScrollStateHolder.layoutInfo.totalItemsCount - 1
                while (i > lastVisibleItemIndex.value){
                    scrollOffset += listItemAbsoluteSizeList[i]
                    i -= 1
                }
                scope.launch {listScrollStateHolder.scrollBy(-scrollOffset.toFloat())}
                isKeyboardVisibleBefore.value = false
            }
        }
    }

    return absoluteKeyboardHeight > screenHeight * 0.15
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SolutionTestingAppTheme {
        AppUI()
    }
}