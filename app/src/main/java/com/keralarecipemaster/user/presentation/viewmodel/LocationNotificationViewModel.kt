package com.keralarecipemaster.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.user.prefsstore.PrefsStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationNotificationViewModel @Inject constructor(
    private val prefsStore: PrefsStore
) :
    ViewModel() {

    val notificationStatus: StateFlow<Boolean>
        get() = _notificationStatus

    private val _notificationStatus =
        MutableStateFlow(false)

    init {
        viewModelScope.launch {
            prefsStore.getNotificationStatus().catch { }.collect {
                _notificationStatus.value = it
            }
        }
    }

    fun updateNotificationStatus(status: Boolean) {
        viewModelScope.launch {
            prefsStore.setNotificationStatus(status)
            _notificationStatus.value = status
        }
    }
}
