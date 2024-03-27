package com.dsceltic.mvisample.feature.viewmodel


import com.dsceltic.mvisample.base.BaseViewModel
import com.dsceltic.mvisample.feature.OrderSummaryContract
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICK_UP = 3.00
class OrderViewModel : BaseViewModel<OrderSummaryContract.Event, OrderSummaryContract.State, OrderSummaryContract.Effect>() {

    override fun setInitialState() = OrderSummaryContract.State(
        quantity = 0,
        flavor = "",
        date = "",
        price = "",
        pickupOptions = pickupOptions()
    )

    override fun handleEvents(event: OrderSummaryContract.Event) {
        when(event){
            OrderSummaryContract.Event.onBackPress -> {
                setEffect { OrderSummaryContract.Effect.Navigation.Back }
            }
            OrderSummaryContract.Event.reset -> {
                setState { copy(pickupOptions = pickupOptions()) }
            }
            is OrderSummaryContract.Event.setQuantity -> {
                setState { copy(quantity = event.quantity, price = calculatePrice(event.quantity,date)) }
            }
            is OrderSummaryContract.Event.setFlavor -> {
                setState { copy(flavor = event.flavor) }
            }
            is OrderSummaryContract.Event.setDate -> {
                setState { copy(date = event.date, price = calculatePrice(quantity,event.date)) }
            }
        }
    }
    private fun calculatePrice(
        quantity: Int = viewState.value.quantity,
        pickupDate: String = viewState.value.date
    ) : String{
        var calculatedPrice = quantity * PRICE_PER_CUPCAKE
        if(pickupOptions()[0] == pickupDate){
            calculatedPrice += PRICE_FOR_SAME_DAY_PICK_UP
        }
        val formattedPrice = NumberFormat.getCurrencyInstance().format(calculatedPrice)
        return formattedPrice
    }

    private fun pickupOptions(): List<String>{
        val dateOptions = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MM d", Locale.getDefault())
        val calendar = Calendar.getInstance()

        repeat(4){
            dateOptions.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE,1)
        }
        return dateOptions
    }
}