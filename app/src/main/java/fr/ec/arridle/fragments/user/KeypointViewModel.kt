package fr.ec.arridle.fragments.user

import android.app.Application
import androidx.lifecycle.*

class KeypointViewModel( keypointId: Int,
                       app: Application
) : AndroidViewModel(app) {

    // The internal MutableLiveData for the selected property
    private val _keypointId = MutableLiveData<Int>()

    // The external LiveData for the SelectedProperty
    val keypointId: LiveData<Int>
        get() = _keypointId

    // Initialize the _selectedProperty MutableLiveData
    init {
        _keypointId.value = keypointId
    }
}