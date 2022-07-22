package com.example.solutiontestingapp

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.*
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

        SyncLazyColumnScroll(scope = scope, listScrollStateHolder = listScrollStateHolder)
    }
}

@Composable
fun SyncLazyColumnScroll(
    scope: CoroutineScope,
    listScrollStateHolder: LazyListState
) {
    val isKeyboardOpen by rememberIsKeyboardOpen()
    var prevScreenSize by remember { mutableStateOf(0)}
    var lastVisibleItemIndex by remember { mutableStateOf(0)}
    var lastVisibleItemOffset by remember { mutableStateOf(0)}
    var hasScrolledOnce by remember { mutableStateOf(false)}
    
    LaunchedEffect(key1 = isKeyboardOpen, key2 = listScrollStateHolder.isScrollInProgress){
        if (listScrollStateHolder.layoutInfo.viewportEndOffset > prevScreenSize)
            prevScreenSize = listScrollStateHolder.layoutInfo.viewportEndOffset
        if (listScrollStateHolder.layoutInfo.visibleItemsInfo.isNotEmpty()) {
            if (isKeyboardOpen) {
                if (!listScrollStateHolder.isScrollInProgress && !hasScrolledOnce) {
                    val size = listScrollStateHolder.layoutInfo.viewportEndOffset
                    scope.launch { listScrollStateHolder.animateScrollBy((prevScreenSize - size).toFloat()) }
                    hasScrolledOnce = true
                }
                lastVisibleItemIndex = listScrollStateHolder.layoutInfo.visibleItemsInfo[listScrollStateHolder.layoutInfo.visibleItemsInfo.size - 1].index
                lastVisibleItemOffset = listScrollStateHolder.layoutInfo.viewportEndOffset - listScrollStateHolder.layoutInfo.visibleItemsInfo[listScrollStateHolder.layoutInfo.visibleItemsInfo.size - 1].offset
            } else {
                if (!listScrollStateHolder.isScrollInProgress) {
                    var itemDetails: LazyListItemInfo? = null
                    for (item in listScrollStateHolder.layoutInfo.visibleItemsInfo){
                        if (item.index == lastVisibleItemIndex){
                            itemDetails = item
                            break
                        }
                    }
                    if (itemDetails != null){
                        val lastItemCurrentOffset = (listScrollStateHolder.layoutInfo.viewportEndOffset - itemDetails.offset) - lastVisibleItemOffset
                        if (hasScrolledOnce){
                            scope.launch { listScrollStateHolder.animateScrollBy(-lastItemCurrentOffset.toFloat()) }
                        }
                    }
                    hasScrolledOnce = false
                }
            }
        }
    }

}

fun View.getKeyboardHeight(): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect)
    val screenHeight = rootView.height
    val absoluteKeyboardHeight = screenHeight - rect.bottom
    return if (rect.bottom > 0) absoluteKeyboardHeight > screenHeight * .15 else false
}

@Composable
fun rememberIsKeyboardOpen(): State<Boolean> {
    val view = LocalView.current

    return produceState(initialValue = view.getKeyboardHeight()) {
        val viewTreeObserver = view.viewTreeObserver
        val listener = ViewTreeObserver.OnGlobalLayoutListener { value = view.getKeyboardHeight()}
        viewTreeObserver.addOnGlobalLayoutListener(listener)

        awaitDispose { viewTreeObserver.removeOnGlobalLayoutListener(listener)  }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SolutionTestingAppTheme {
        AppUI()
    }
}