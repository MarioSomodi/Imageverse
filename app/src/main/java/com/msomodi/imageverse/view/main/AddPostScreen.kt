package com.msomodi.imageverse.view.main

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ShortText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.model.hashtag.response.HashtagResponse
import com.msomodi.imageverse.util.noRippleClickable
import com.msomodi.imageverse.view.BottomNavScreen
import com.msomodi.imageverse.view.common.Chip
import com.msomodi.imageverse.view.common.PackageCard
import com.msomodi.imageverse.viewmodel.posts.AddPostViewModel
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AddPostScreen(
    modifier : Modifier = Modifier,
    userId: String?,
    navigateToPostsOnSuccess: () -> Unit,
    addPostViewModel: AddPostViewModel = hiltViewModel()
) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val navController = rememberNavController()

    var step2 by remember { mutableStateOf(false) }

    var loadingStateHashtag by remember { mutableStateOf(false) }

    var loadingStateAddingPost by remember { mutableStateOf(false) }

    var failureOccurred by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val addPostState = addPostViewModel.addPostState.value

    val stateHashtags by addPostViewModel.addPostHashtagsRequestState.collectAsState(null)

    val addingPostState by addPostViewModel.addPostRequestState.collectAsState(null)

    var expandedSaveImageAs by remember { mutableStateOf(false) }

    var expandedHashtags by remember { mutableStateOf(false) }

    val saveImageAsOptions = arrayOf("png", "jpeg", "bmp")

    val hashtags : List<HashtagResponse>? = addPostViewModel.hashtags.observeAsState(initial = null).value

    var selectedHashtag by remember { mutableStateOf("") }

    val context = LocalContext.current

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
                bitmap = ImageDecoder.decodeBitmap(source)
            }
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            addPostViewModel.onBase64ImageChanged(Base64.encode(byteArray))
        } else {
            val exception = result.error
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        val cropOptions = CropImageContractOptions(uri, CropImageOptions())
        imageCropLauncher.launch(cropOptions)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if(!step2){
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    modifier = modifier
                        .fillMaxWidth()
                        .heightIn(0.dp, 415.dp),
                    contentScale = ContentScale.Fit,
                    contentDescription = stringResource(R.string.image_to_post),
                )
            }else{
                Text(text = stringResource(R.string.pick_an_image_to_post_to_be_able_to_continue_with_adding_a_new_post), textAlign = TextAlign.Center)
            }
            Button(
                modifier = modifier
                    .fillMaxWidth(),
                onClick = { imagePickerLauncher.launch("image/*") },
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(text = stringResource(R.string.pick_image_to_post))
            }
            Button(
                modifier = modifier
                    .fillMaxWidth(),
                onClick = { step2 = true },
                shape = RoundedCornerShape(15.dp),
                enabled = addPostState.isBase64ImageValid
            ) {
                Text(text = stringResource(id = R.string.next))
            }
        }else if(!loadingStateAddingPost){
            if(loadingStateHashtag){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                    Text(text = stringResource(R.string.loading_existing_hashtags))
                }
            }else if(failureOccurred){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.fillMaxSize()
                ) {
                    Text(text = stringResource(R.string.internet_connection_error_or_server_connection_error_occurred), textAlign = TextAlign.Center)
                    Button(onClick = {addPostViewModel.getHashtags()}) {
                        Text(text = stringResource(R.string.try_again))
                    }
                }
            }else if (hashtags != null){
                ExposedDropdownMenuBox(expanded = expandedHashtags, onExpandedChange = { expandedHashtags = !expandedHashtags }) {
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .imePadding(),
                        value = selectedHashtag,
                        onValueChange = {
                            selectedHashtag = it
                            expandedHashtags = true
                        },
                        label = { Text(text = stringResource(R.string.hashtags))},
                        isError = addPostState.isHashtagsValid,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedHashtags)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    val filterOpts = hashtags.filter { it.name.contains(selectedHashtag, ignoreCase = true) && !addPostState.hashtags.contains(it.name) }
                    ExposedDropdownMenu(expanded = expandedHashtags, onDismissRequest = { expandedHashtags = false }) {
                        if(selectedHashtag.isNotEmpty()){
                            DropdownMenuItem(
                                onClick = {
                                    addPostViewModel.onHashtagAdded(selectedHashtag)
                                    selectedHashtag = ""
                                    expandedHashtags = false
                                }
                            ) {
                                Text(text = "Add new hashtag $selectedHashtag")
                            }
                        }
                        if(filterOpts.isNotEmpty()){
                            filterOpts.forEach { hashtag ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedHashtag = ""
                                        addPostViewModel.onHashtagAdded(hashtag.name)
                                        expandedHashtags = false
                                    }
                                ) {
                                    Text(text = hashtag.name)
                                }
                            }
                        }
                    }
                }
                if(addPostState.hashtags.isNotEmpty()){
                    val listState = rememberLazyListState()
    
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                        state = listState,
                        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(addPostState.hashtags.toList()){ hashtag ->
                            Chip(
                                icon = Icons.Outlined.Close,
                                tintColor = Color.White,
                                isSelected = false,
                                text = hashtag,
                                onChecked = {addPostViewModel.onHashtagRemoved(hashtag)},
                                layoutDirection = LayoutDirection.Rtl
                            )
                        }
                    }
                }
            }
            val maxChar = 125
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding(),
                value = addPostState.description,
                onValueChange = {
                    if (it.length <= maxChar) addPostViewModel.onDescriptionChanged(it)
                },
                label = { Text(text = stringResource(R.string.description))},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down)}
                ),
                isError = !addPostState.isDescriptionValid,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.ShortText,
                        contentDescription = stringResource(R.string.description)
                    )
                }
            )
            Text(
                text = "${addPostState.description.length} / $maxChar",
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
            )
            ExposedDropdownMenuBox(expanded = expandedSaveImageAs, onExpandedChange = {expandedSaveImageAs = !expandedSaveImageAs}) {
                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding(),
                    isError = !addPostState.isSaveImageValid,
                    value = addPostState.saveImageAs,
                    onValueChange = {},
                    label = { Text(text = stringResource(R.string.save_image_as))},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSaveImageAs) },
                )
                ExposedDropdownMenu(
                    expanded = expandedSaveImageAs,
                    onDismissRequest = { expandedSaveImageAs = false }
                ) {
                    saveImageAsOptions.forEach { item ->
                        DropdownMenuItem(
                            content = { Text(text = item) },
                            onClick = {
                                addPostViewModel.onSaveImageAs(item)
                                expandedSaveImageAs = false
                            }
                        )
                    }
                }
            }
            Button(
                modifier = modifier
                    .fillMaxWidth(),
                onClick = { addPostViewModel.addPost(userId!!) },
                shape = RoundedCornerShape(15.dp),
                enabled = addPostState.isDescriptionValid && addPostState.isSaveImageValid && addPostState.isBase64ImageValid && addPostState.isHashtagsValid
            ) {
                Text(text = stringResource(R.string.post))
            }
        }else{
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
                Text(text = stringResource(R.string.uploading_post))
            }
        }
    }
    stateHashtags.let { state ->
        when(state){
            RequestState.LOADING -> {
                loadingStateHashtag = true
            }
            is RequestState.FAILURE -> {
                loadingStateHashtag = false
                val message = state.message
                failureOccurred = !state.isApiError
                LaunchedEffect(key1 = message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
            RequestState.SUCCESS -> {
                loadingStateHashtag = false
                failureOccurred = false
            }
            else -> {}
        }
    }

    addingPostState.let { state ->
        when(state){
            is RequestState.LOADING -> {
                loadingStateAddingPost = true
            }
            is RequestState.FAILURE -> {
                val message = state.message
                loadingStateAddingPost = false
                LaunchedEffect(key1 = message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
            is RequestState.SUCCESS -> {
                loadingStateAddingPost = false
                LaunchedEffect(Unit) {
                    navigateToPostsOnSuccess()
                }
            }
            else -> {}
        }
    }
}
