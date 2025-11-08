package cmp.erp.base

/**
 * Base interface for UI Effects/Side Effects in MVI architecture
 * Represents one-time events that should be handled
 */
interface UiEffect {
    object Idle : UiEffect
}

