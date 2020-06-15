package fr.ec.arridle.fragments.user

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.ec.arridle.network.KeypointProperty

/**
 * Simple ViewModel factory that provides the MarsProperty and context to the ViewModel.
 */
class KeypointViewModelFactory(
    private val keypointId: Int,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KeypointViewModel::class.java)) {
            return KeypointViewModel(keypointId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}