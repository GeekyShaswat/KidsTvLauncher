package com.shaswat.data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PinRepository {
    // In a real app, this would be stored securely and use DataStore
    private val defaultPin = "1234"

    suspend fun validatePin(enteredPin: String): Boolean = withContext(Dispatchers.IO) {
        enteredPin == defaultPin
    }

    suspend fun changePin(newPin: String): Boolean = withContext(Dispatchers.IO) {
        // In a real app, we would save the new PIN
        true
    }
}

