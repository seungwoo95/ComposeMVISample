package com.dsceltic.mvisample.feature

import com.dsceltic.mvisample.base.ViewEvent
import com.dsceltic.mvisample.base.ViewSideEffect
import com.dsceltic.mvisample.base.ViewState

class OrderSummaryContract {

    sealed class Event: ViewEvent {
        data class setQuantity(val quantity: Int) : Event()
        data class setFlavor(val flavor: String) : Event()
        data class setDate(val date: String) : Event()
        object reset : Event()
        object onBackPress : Event()
    }

    data class State(
        val quantity: Int,
        val flavor: String,
        val date: String,
        val price: String,
        val pickupOptions: List<String>
    ) : ViewState

    sealed class Effect: ViewSideEffect {
        sealed class Navigation: Effect(){
            object Back : Navigation()
        }
    }
}