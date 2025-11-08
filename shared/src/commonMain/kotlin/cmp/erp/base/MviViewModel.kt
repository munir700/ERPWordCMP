package cmp.erp.base

import kotlinx.coroutines.flow.Flow

/**
 * Base interface for MVI ViewModel
 * Defines the contract that all ViewModels must follow
 */
interface MviViewModel<Event : UiEvent, State : UiState, Effect : UiEffect> {
    val uiState: Flow<State>
    val uiEffect: Flow<Effect>

    fun onEvent(event: Event)
    fun getCurrentState(): State
}

/**
 * Base interface for UI Events/Intents in MVI architecture
 * Represents user actions and events
 */
interface UiEvent {
    object Idle : UiEvent
}

