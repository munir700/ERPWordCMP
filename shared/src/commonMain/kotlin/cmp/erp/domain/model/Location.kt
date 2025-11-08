package cmp.erp.domain.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Clock

/**
 * Location data class for geo-location based check-in/out
 */
@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Double? = null,
    val address: String? = null,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)

