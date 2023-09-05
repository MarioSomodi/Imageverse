package com.msomodi.imageverse.view.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.view.common.PostCard
import com.msomodi.imageverse.viewmodel.posts.PostsViewModel

@ExperimentalPagingApi
@Composable
fun PostsScreen(
    modifier: Modifier = Modifier,
    navigateToEditPost : (String) -> Unit,
    authResult : AuthenticationResponse? = null,
    postsViewModel: PostsViewModel = hiltViewModel(),
){
    val user = authResult?.user
    val posts = postsViewModel.posts.collectAsLazyPagingItems()
    val listState = rememberLazyListState(0)
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(
            items = posts,
            key = { post -> post.id }
        ) { post ->
            post?.let {
                PostCard(
                    user = user,
                    post = post,
                    navigateToEditPost = navigateToEditPost,
                    onPostDelete = {id, dbId ->
                        postsViewModel.delete(id, dbId)
                    },
                    onRefresh = {posts.refresh()}
                )
            }
        }
    }
}