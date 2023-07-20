package com.example.contentlessbottomsheetsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.contentlessbottomsheetsample.ui.theme.ContentLessBottomSheetSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentLessBottomSheetSampleTheme {
                RootScreen()
            }
        }
    }
}

@Composable
private fun RootScreen() {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        NestedScrollableBottomSheetByLayout()
    }
}

@Preview(showBackground = true)
@Composable
private fun RootScreenPreview() {

}

