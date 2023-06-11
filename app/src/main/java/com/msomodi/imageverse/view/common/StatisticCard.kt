package com.msomodi.imageverse.view.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.w3c.dom.Text

@Composable
fun StatisticsCard(
    modifier : Modifier = Modifier,
    icon : ImageVector,
    statisticValue: String,
    title : Int,
    biggerCard : Boolean = false
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .shadow(10.dp, RoundedCornerShape(15.dp))
            .background(MaterialTheme.colors.primary)
            .size(if(biggerCard) 140.dp else 115.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = modifier
                .padding(horizontal = 5.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Icon(
                imageVector = icon,
                tint = Color.White,
                contentDescription = "Clock"
            )
            Text(
                text = statisticValue,
                fontWeight = FontWeight.ExtraBold
            )
            Text(text = stringResource(id = title), textAlign = TextAlign.Center)
        }
    }
}