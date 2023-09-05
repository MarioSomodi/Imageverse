package com.msomodi.imageverse.viewmodel.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.msomodi.imageverse.model.post.response.PostResponse
import com.msomodi.imageverse.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class PostsViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel(){
    val posts = repository.getPosts()

    fun delete(id : String, dbId : Int) {
        viewModelScope.launch {
            repository.deletePost(id, dbId)
        }
    }
}