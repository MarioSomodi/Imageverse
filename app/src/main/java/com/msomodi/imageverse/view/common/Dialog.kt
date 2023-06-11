package com.msomodi.imageverse.view.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.msomodi.imageverse.R

@Composable
fun Dialog(
    dialogOpen : Boolean,
    onConfirm : () -> Unit,
    confirmText : String,
    title : String,
    bodyText : String,
    dismissText : String = stringResource(id = R.string.cancel),
    onDismiss : () -> Unit = {},
){
    var isOpen by remember {
        mutableStateOf(dialogOpen)
    }

    AlertDialog(
        onDismissRequest = {
            isOpen = !isOpen
        },
        confirmButton = {
            TextButton(
                onClick = {
                    isOpen = !isOpen
                    onConfirm()
                }
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    isOpen = !isOpen
                    onDismiss()
                }
            ) {
                Text(text = dismissText)
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = bodyText)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        shape = RoundedCornerShape(5.dp),
        backgroundColor = MaterialTheme.colors.background
    )
}