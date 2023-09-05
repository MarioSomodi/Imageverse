package com.msomodi.imageverse.view.main

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.model.hashtag.response.HashtagResponse
import com.msomodi.imageverse.model.post.response.PostResponse
import com.msomodi.imageverse.view.common.Chip
import com.msomodi.imageverse.viewmodel.posts.EditPostViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun EditPostScreen(
    modifier: Modifier = Modifier,
    postId: String?,
    editPostViewModel: EditPostViewModel = hiltViewModel(),
    navigateToPostsOnSuccess: () -> Unit
) {
    val context = LocalContext.current

    val post : PostResponse? = editPostViewModel.post.observeAsState(initial = null).value

    val hashtags : List<HashtagResponse>? = editPostViewModel.hashtags.observeAsState(initial = null).value


    var loadingStateGetPost by remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    var expanded by remember { mutableStateOf(false) }

    var loadingStateEditPost by remember {
        mutableStateOf(false)
    }

    var failureOccurred by remember {
        mutableStateOf(false)
    }

    val editPostState = editPostViewModel.editPostState.value

    val getPostRequestState by editPostViewModel.getPostRequestState.collectAsState(null)

    val editPostRequestState by editPostViewModel.editPostRequestState.collectAsState(null)

    var selectedHashtag by remember { mutableStateOf("") }

    if(postId != null){
        LaunchedEffect(Unit){
            editPostViewModel.getPostAndHashtags(postId)
        }
        if(loadingStateGetPost){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
                Text(text = stringResource(R.string.loading_post))
            }
        }else if(failureOccurred){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxSize()
            ) {
                Text(text = stringResource(R.string.internet_connection_error_or_server_connection_error_occurred), textAlign = TextAlign.Center)
                Button(onClick = {editPostViewModel.getPostAndHashtags(postId)}) {
                    Text(text = stringResource(R.string.try_again))
                }
            }
        }
        else if(post != null && hashtags != null){
            LaunchedEffect(Unit){
                editPostViewModel.setInitValues(post.description, post.hashtags.map{it.name})
            }

            val updateAllowed =
                editPostState.isDescriptionValid &&
                editPostState.isHashtagsValid

            if(loadingStateEditPost){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                    Text(text = stringResource(R.string.updating_post))
                }
            }else{
                Column(modifier = modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                        OutlinedTextField(
                            modifier = modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .imePadding(),
                            value = selectedHashtag,
                            onValueChange = {
                                selectedHashtag = it
                                expanded = true
                            },
                            label = { Text(text = stringResource(R.string.hashtags))},
                            isError = editPostState.isHashtagsValid,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )
                        val filterOpts = hashtags.filter { it.name.contains(selectedHashtag, ignoreCase = true) && !editPostState.hashtags.contains(it.name) }
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            if(selectedHashtag.isNotEmpty()){
                                DropdownMenuItem(
                                    onClick = {
                                        editPostViewModel.onHashtagAdded(selectedHashtag)
                                        selectedHashtag = ""
                                        expanded = false
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
                                            editPostViewModel.onHashtagAdded(hashtag.name)
                                            expanded = false
                                        }
                                    ) {
                                        Text(text = hashtag.name)
                                    }
                                }
                            }
                        }
                    }
                    if(editPostState.hashtags.isNotEmpty()){
                        val listState = rememberLazyListState()

                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            state = listState,
                            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            items(editPostState.hashtags.toList()){ hashtag ->
                                Chip(
                                    icon = Icons.Outlined.Close,
                                    tintColor = Color.White,
                                    isSelected = false,
                                    text = hashtag,
                                    onChecked = {editPostViewModel.onHashtagRemoved(hashtag)},
                                    layoutDirection = LayoutDirection.Rtl
                                )
                            }
                        }
                    }
                    val maxChar = 125
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .imePadding(),
                        value = editPostState.description,
                        onValueChange = {
                            if (it.length <= maxChar) editPostViewModel.onDescriptionChanged(it)
                        },
                        label = { Text(text = stringResource(R.string.description))},
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down)}
                        ),
                        isError = !editPostState.isDescriptionValid,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.ShortText,
                                contentDescription = stringResource(R.string.description)
                            )
                        }
                    )
                    Text(
                        text = "${editPostState.description.length} / $maxChar",
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp)
                    )
                    Button(
                        modifier = modifier
                            .fillMaxWidth(),
                        onClick = { editPostViewModel.editPost(postId) },
                        shape = RoundedCornerShape(15.dp),
                        enabled = updateAllowed
                    ) {
                        Text(text = stringResource(R.string.update_post))
                    }
                }
            }
        }
    }

    editPostRequestState.let { state ->
        when(state){
            RequestState.LOADING -> {
                loadingStateEditPost = true
            }
            is RequestState.FAILURE -> {
                loadingStateEditPost = false
                val message = state.message
                LaunchedEffect(key1 = message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
            RequestState.SUCCESS -> {
                loadingStateEditPost = false
                LaunchedEffect(Unit) {
                    navigateToPostsOnSuccess()
                }
            }
            else -> {}
        }
    }

    getPostRequestState.let { state ->
        when(state){
            RequestState.LOADING -> {
                loadingStateGetPost = true
            }
            is RequestState.FAILURE -> {
                loadingStateGetPost = false
                val message = state.message
                failureOccurred = !state.isApiError
                LaunchedEffect(key1 = message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
            RequestState.SUCCESS -> {
                loadingStateGetPost = false
                failureOccurred = false
            }
            else -> {}
        }
    }
}