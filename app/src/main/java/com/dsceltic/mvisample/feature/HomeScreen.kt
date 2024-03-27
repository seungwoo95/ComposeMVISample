package com.dsceltic.mvisample.feature

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dsceltic.mvisample.feature.viewmodel.OrderViewModel
import com.dsceltic.mvisample.R
import com.dsceltic.mvisample.data.DataSource

enum class Screen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Flavor(title = R.string.choose_flavor),
    Pickup(title = R.string.pickup_date),
    Summary(title = R.string.order_summary)
}

@Composable
fun HomeScreen(
    viewModel: OrderViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val navController = rememberNavController()

    Scaffold(

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(
                route = Screen.Start.name
            ) {
                StartOrderScreen(
                    onNextButtonClicked = {
                        viewModel.setEvent(OrderSummaryContract.Event.setQuantity(it))
                        navController.navigate(Screen.Flavor.name)
                    })
            }

            composable(
                route = Screen.Flavor.name
            ){
                val context = LocalContext.current
                SelectOptionScreen(
                    subtotal = viewModel.viewState.value.price,
                    effectFlow = viewModel.effect,
                    onNextButtonClicked = { navController.navigate(Screen.Pickup.name) },
                    onCancelButtonClicked = {
                        viewModel.setEvent(OrderSummaryContract.Event.onBackPress)
                    },
                    options = DataSource.flavors.map { id -> context.resources.getString(id) },
                    onSelectionChanged = { viewModel.setEvent(OrderSummaryContract.Event.setFlavor(it)) },
                    onNavigation = {
                                   if(it is OrderSummaryContract.Effect.Navigation.Back){
                                       viewModel.setEvent(OrderSummaryContract.Event.reset)
                                       navController.popBackStack(Screen.Start.name,inclusive = false)
                                   }
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = Screen.Pickup.name) {
                SelectOptionScreen(
                    subtotal = viewModel.viewState.value.price,
                    effectFlow = viewModel.effect,
                    onNextButtonClicked = { navController.navigate(Screen.Summary.name) },
                    onCancelButtonClicked = {
                        viewModel.setEvent(OrderSummaryContract.Event.onBackPress)
                    },
                    options = viewModel.viewState.value.pickupOptions,
                    onSelectionChanged = { viewModel.setEvent(OrderSummaryContract.Event.setDate(it)) },
                    onNavigation = {
                        if(it is OrderSummaryContract.Effect.Navigation.Back){
                            viewModel.setEvent(OrderSummaryContract.Event.reset)
                            navController.popBackStack(Screen.Start.name,inclusive = false)
                        }
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = Screen.Summary.name) {
                val context = LocalContext.current
                OrderSummaryScreen(
                    state = viewModel.viewState.value,
                    effectFlow = viewModel.effect,
                    onCancelButtonClicked = {
                        viewModel.setEvent(OrderSummaryContract.Event.onBackPress)
                    },
                    onSendButtonClicked = { subject: String, summary: String ->
                        shareOrder(context, subject, summary)
                    },
                    onNavigation = {
                        if(it is OrderSummaryContract.Effect.Navigation.Back){
                            viewModel.setEvent(OrderSummaryContract.Event.reset)
                            navController.popBackStack(Screen.Start.name,inclusive = false)
                        }
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }


}

private fun shareOrder(context: Context, subject: String, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_cupcake_order)
        )
    )
}