package com.keralarecipemaster.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.user.prefsstore.PrefsStore
import com.keralarecipemaster.user.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationNotificationViewModel @Inject constructor(
    private val prefsStore: PrefsStore
) :
    ViewModel() {

    val isNotificationEnabled: StateFlow<Boolean>
        get() = _isNotificationEnabled

    private val _isNotificationEnabled =
        MutableStateFlow(false)

    val locationPermissionStatusMsg: StateFlow<String>
        get() = _locationPermissionStatusMsg

    private val _locationPermissionStatusMsg =
        MutableStateFlow(Constants.EMPTY_STRING)

    val isLocationPermissionGranted: StateFlow<Boolean>
        get() = _isLocationPermissionGranted

    private val _isLocationPermissionGranted =
        MutableStateFlow(false)

    init {
        viewModelScope.launch {
            prefsStore.getNotificationStatus().catch { }.collect {
                _isNotificationEnabled.value = it
            }
        }
    }

    fun updateNotificationStatus(status: Boolean) {
        viewModelScope.launch {
            prefsStore.setNotificationStatus(status)
            _isNotificationEnabled.value = status
        }
        _isNotificationEnabled.value = status
    }

    fun updateLocationPermissionStatus(isGranted: Boolean) {
        _isLocationPermissionGranted.value = isGranted
    }

    fun updateLocationPermissionStatusMsg(msg: String) {
        _locationPermissionStatusMsg.value = msg
    }
}
