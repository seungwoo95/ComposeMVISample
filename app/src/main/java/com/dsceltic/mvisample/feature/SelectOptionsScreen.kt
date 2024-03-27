package com.dsceltic.mvisample.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dsceltic.mvisample.R
import com.dsceltic.mvisample.base.SIDE_EFFECTS_KEY
import com.dsceltic.mvisample.common.PriceLabel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

@Composable
fun SelectOptionScreen(
    subtotal: String,
    options: List<String>,
    effectFlow: Flow<OrderSummaryContract.Effect>,
    onSelectionChanged: (String) -> Unit = {},
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: () -> Unit = {},
    onNavigation: (OrderSummaryContract.Effect.Navigation) -> Unit,
    modifier: Modifier = Modifier
){

    LaunchedEffect(key1 = SIDE_EFFECTS_KEY) {
        effectFlow.onEach { effect ->
            when(effect){
                OrderSummaryContract.Effect.Navigation.Back ->{
                    onNavigation(OrderSummaryContract.Effect.Navigation.Back)
                }
            }

        }
    }

    var selectedValue by rememberSaveable { mutableStateOf("") }

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column (modifier = Modifier.padding(16.dp)){
                options.forEach{ item ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = selectedValue == item,
                            onClick = {
                                selectedValue = item
                                onSelectionChanged(item)
                            }
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        RadioButton(
                            selected = selectedValue == item,
                            onClick = {
                                selectedValue = item
                                onSelectionChanged(item)
                            }
                        )
                        Text(item)
                    }
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier =  Modifier.padding(bottom = 16.dp)
                )
                PriceLabel(
                    subtotal = subtotal,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(
                            top = 16.dp,
                            bottom = 16.dp
                        )
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onCancelButtonClicked
            ) {
                Text(stringResource(id = R.string.cancel))
            }
            Button(
                modifier = Modifier.weight(1f),
                enabled = selectedValue.isNotEmpty(),
                onClick = onNextButtonClicked
            ) {
                Text(stringResource(id = R.string.next))
            }
        }
    }
}
@Preview
@Composable
fun SelectOptionPreview() {
    MaterialTheme {
        SelectOptionScreen(
            subtotal = "299.99",
            effectFlow = flow {  },
            options = listOf("Option 1", "Option 2", "Option 3", "Option 4"),
            onNavigation = {},
            modifier = Modifier.fillMaxHeight()
        )
    }
}