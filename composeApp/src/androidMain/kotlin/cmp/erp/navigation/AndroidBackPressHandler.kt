package cmp.erp.navigation

import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner

/**
 * Composable to handle back press events (Android specific)
 */
@Composable
fun BackPressHandler(
    navigationStateHolder: NavigationStateHolder,
    onBackPressed: (() -> Unit)? = null
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navigationStateHolder.canGoBack.value) {
                    navigationStateHolder.goBack()
                } else if (onBackPressed != null) {
                    onBackPressed()
                } else {
                    // Default behavior: exit app
                    isEnabled = false
                    this.handleOnBackPressed()
                }
            }
        }

        // Note: This requires access to OnBackPressedDispatcher from Activity
        // For now, just mark it as a placeholder
        // In actual Android implementation, get it from Activity context

        onDispose {
            callback.remove()
        }
    }
}

