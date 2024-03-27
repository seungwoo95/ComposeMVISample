package com.dsceltic.mvisample.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dsceltic.mvisample.R

@Composable
fun PriceLabel(
    subtotal: String,
    modifier: Modifier = Modifier
){
    Text(
       text = stringResource(id = R.string.subtotal_price,subtotal),
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall
    )
}