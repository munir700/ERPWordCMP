package cmp.erp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cmp.erp.navigation.NavigationContainer
import cmp.erp.navigation.rememberNavigationStateHolder

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navigationStateHolder = rememberNavigationStateHolder()
        NavigationContainer(navigationStateHolder)
    }
}