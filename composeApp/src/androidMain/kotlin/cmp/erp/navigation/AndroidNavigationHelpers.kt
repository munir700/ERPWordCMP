package cmp.erp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Composable to provide navigation state (Android specific)
 */
@Composable
fun rememberNavigationStateHolder(): NavigationStateHolder {
    return remember { NavigationStateHolder() }
}

