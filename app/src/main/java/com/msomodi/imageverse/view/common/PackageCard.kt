package com.msomodi.imageverse.view.common

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.packages.response.PackageResponse
import com.msomodi.imageverse.ui.theme.ImageverseTheme

@Composable
fun PackageCard(
    modifier : Modifier = Modifier,
    smallerCard : Boolean,
    packageObj : PackageResponse,
    onSelectPackage : (String) -> Unit,
    selectedPackageId : String,
){
    val selected : Boolean = selectedPackageId == packageObj.id
    Card(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .border(width = 2.dp, shape = RoundedCornerShape(30.dp), color = if(selected) MaterialTheme.colors.primary else MaterialTheme.colors.background),
        shape = RoundedCornerShape(30.dp),
        elevation = 5.dp
    ) {
        Column(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if(smallerCard) Arrangement.spacedBy(3.dp) else Arrangement.spacedBy(15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if(smallerCard) Arrangement.spacedBy(3.dp) else Arrangement.spacedBy(15.dp)
            ) {
                Text(
                    text = packageObj.name,
                    style = typography.h5
                )
                Text(
                    text = "EUR ${packageObj.price}",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(R.string.a_month),
                    style = typography.h5
                )
            }
            Divider(
                modifier = modifier
                    .clip(RoundedCornerShape(20.dp)),
                color = MaterialTheme.colors.primary,
                thickness = 2.dp
            )
            Column(
                modifier = modifier
                    .width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text(
                    text = "Upload images up to ${packageObj.uploadSizeLimit}MB",
                    style = typography.h5,
                    textAlign = TextAlign.Center

                )
                Text(
                    text = "Max daily upload is ${packageObj.dailyUploadLimit}MB",
                    style = typography.h5,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${packageObj.dailyImageUploadLimit} images daily",
                    style = typography.h5,
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                modifier = modifier
                    .clip(RoundedCornerShape(20.dp)),
                color = MaterialTheme.colors.primary,
                thickness = 2.dp
            )
            Button(
                modifier = modifier
                    .fillMaxWidth(),
                onClick = {onSelectPackage(packageObj.id)},
                shape = RoundedCornerShape(15.dp),
                enabled = !selected
            ) {
                Text(text = stringResource(R.string.choose_this_package))
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PackageCardPreview(){
    val packageObj = PackageResponse(
        "1",
        "Default",
        9.99.toDouble(),
        10,
        10,
        10
    )
    ImageverseTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            PackageCard(packageObj = packageObj, onSelectPackage = {}, selectedPackageId = packageObj.id, smallerCard = false)
        }
    }
}