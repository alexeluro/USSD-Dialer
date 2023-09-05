package com.inspiredcoda.ussddialer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspiredcoda.ussddialer.R
import com.inspiredcoda.ussddialer.model.UssdCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UssdHomeViewModel @Inject constructor(

) : ViewModel() {

    private var _uiEvent: MutableStateFlow<UssdUiEvent> = MutableStateFlow(
        UssdUiEvent.OnUssdCodesGenerated(
            emptyList()
        )
    )
    val uiEvent: StateFlow<UssdUiEvent>
        get() = _uiEvent?.asStateFlow()!!

    fun generateUssdCodes() {
        viewModelScope.launch {
            val ussdCodes = listOf(
                UssdCode("Data", "*312#", R.drawable.ic_bar_chart),
                UssdCode("Recharge", "*311#", R.drawable.ic_bar_chart),
                UssdCode("Borrow Airtime", "*303#", R.drawable.ic_bar_chart),
                UssdCode("Data Balance", "*323#", R.drawable.ic_bar_chart),
                UssdCode("Account Balance", "*310#", R.drawable.ic_bar_chart),
                UssdCode("MTN Share", "*321#", R.drawable.ic_bar_chart),
                UssdCode("VAS", "*305#", R.drawable.ic_bar_chart),
                UssdCode("NIN", "*996#", R.drawable.ic_bar_chart),
            )

            _uiEvent.emit(UssdUiEvent.OnUssdCodesGenerated(ussdCodes))
        }
    }

    sealed interface UssdUiEvent {
        data class OnUssdCodesGenerated(val ussdCodes: List<UssdCode>) : UssdUiEvent
    }

    init {
        generateUssdCodes()
    }

}