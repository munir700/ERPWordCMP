package cmp.erp.base

/**
 * Base interface for UI State in MVI architecture
 * Represents the immutable state of the UI
 */
interface UiState {
    val isLoading: Boolean
    val error: String?
}

