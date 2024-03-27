package com.dsceltic.mvisample.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dsceltic.mvisample.R
import com.dsceltic.mvisample.base.SIDE_EFFECTS_KEY
import com.dsceltic.mvisample.common.PriceLabel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

@Composable
fun OrderSummaryScreen(
    state: OrderSummaryContract.State,
    effectFlow: Flow<OrderSummaryContract.Effect>,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: (String, String) -> Unit,
    onNavigation: (OrderSummaryContract.Effect.Navigation) -> Unit,
    modifier: Modifier = Modifier
){

    val resources = LocalContext.current.resources

    val numberOfCupcakes = resources.getQuantityString(
        R.plurals.cupcakes,
        state.quantity,
        state.quantity
    )

    val orderSummary = stringResource(
        id = R.string.order_summary,
        numberOfCupcakes,
        state.flavor,
        state.date,
        state.quantity)

    val newOrder = stringResource(id = R.string.new_cupcake_order)

    val items = listOf(
        Pair(stringResource(id = R.string.quantity), numberOfCupcakes),
        Pair(stringResource(id = R.string.flavor), state.flavor),
        Pair(stringResource(id = R.string.pickup_date), state.date)
    )

    LaunchedEffect(key1 = SIDE_EFFECTS_KEY) {
        effectFlow.onEach { effect ->
            when(effect){
                is OrderSummaryContract.Effect.Navigation.Back -> onNavigation(effect)
            }
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                Text(item.first.uppercase())
                Text(text = item.second, fontWeight = FontWeight.Bold)
                HorizontalDivider(thickness = 1.dp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            PriceLabel(
                subtotal = state.price,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSendButtonClicked(newOrder, orderSummary) }
                ) {
                    Text(stringResource(id = R.string.send))
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        }
    }
}