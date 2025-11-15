package cmp.erp.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Android ci - test-1
 * Abstract base ViewModel implementing MVI pattern
 * Provides common functionality for state management, event handling, and side effects
 */
abstract class BaseViewModel<Event : UiEvent, State : UiState, Effect : UiEffect>(
    private val initialState: State
) : MviViewModel<Event, State, Effect> {

    // State management
    private val _uiState = MutableStateFlow(initialState)
    override val uiState: Flow<State> = _uiState.asStateFlow()

    // Side effects channel
    private val _uiEffect = Channel<Effect>()
    override val uiEffect: Flow<Effect> = _uiEffect.receiveAsFlow()

    protected val coroutineScope: CoroutineScope? = null

    override fun onEvent(event: Event) {
        when (event) {
            else -> handleEvent(event)
        }
    }

    override fun getCurrentState(): State = _uiState.value

    /**
     * Update the current state
     */
    protected fun setState(reducer: (State) -> State) {
        _uiState.value = reducer(_uiState.value)
    }

    /**
     * Emit a side effect
     */
    protected suspend fun sendEffect(effect: Effect) {
        _uiEffect.send(effect)
    }

    /**
     * Abstract method to handle events - must be implemented by subclasses
     */
    abstract fun handleEvent(event: Event)
}

