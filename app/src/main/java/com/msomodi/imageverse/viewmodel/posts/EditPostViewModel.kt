package com.msomodi.imageverse.viewmodel.posts

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.msomodi.imageverse.exception.ErrorUtils
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.model.exception.ApiException
import com.msomodi.imageverse.model.hashtag.response.HashtagResponse
import com.msomodi.imageverse.model.post.request.CreatePostRequest
import com.msomodi.imageverse.model.post.request.EditPostRequest
import com.msomodi.imageverse.model.post.response.PostResponse
import com.msomodi.imageverse.repository.HashtagRepository
import com.msomodi.imageverse.repository.PostRepository
import com.msomodi.imageverse.view.main.AddPostState
import com.msomodi.imageverse.view.main.EditPostState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @OptIn(ExperimentalPagingApi::class)
@Inject constructor(
    private val _postRepository: PostRepository,
    private val _hashtagRepository: HashtagRepository,
) : ViewModel(){

    private var _editPostState = mutableStateOf(
        EditPostState()
    )
    val editPostState: State<EditPostState>
        get() = _editPostState

    private val _post : MutableLiveData<PostResponse?> = MutableLiveData()
    val post: LiveData<PostResponse?>
        get() = _post

    private val _hashtags : MutableLiveData<List<HashtagResponse>?> = MutableLiveData()
    val hashtags: LiveData<List<HashtagResponse>?>
        get() = _hashtags

    private val _getPostRequestState = MutableStateFlow<RequestState>(RequestState.START)

    private val _editPostRequestState = MutableStateFlow<RequestState>(RequestState.START)

    val getPostRequestState : Flow<RequestState>
        get() = _getPostRequestState

    val editPostRequestState : Flow<RequestState>
        get() = _editPostRequestState

    private val _errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("EDIT_POST_VIEW_MODEL", e.toString(), e)
    }

    fun setInitValues(description: String, hashtags : Collection<String>){
        _editPostState.value = _editPostState.value.copy(
            description = description,
            hashtags = hashtags,
        )
    }

    fun onDescriptionChanged(description: String){
        _editPostState.value = _editPostState.value.copy(
            description = description,
            isDescriptionValid = description.isNotEmpty()
        )
    }

    fun onHashtagAdded(hashtag: String){
        val hashtags = _editPostState.value.hashtags.plusElement(hashtag)
        _editPostState.value = _editPostState.value.copy(
            hashtags = hashtags,
            isHashtagsValid = hashtags.isNotEmpty()
        )
    }

    fun onHashtagRemoved(hashtag: String){
        val hashtags = _editPostState.value.hashtags.minusElement(hashtag)
        _editPostState.value = _editPostState.value.copy(
            hashtags = hashtags,
            isHashtagsValid = hashtags.isNotEmpty()
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    fun editPost(id : String){
        viewModelScope.launch (_errorHandler){
            _editPostRequestState.emit(RequestState.LOADING)
            _postRepository.updatePost(
                EditPostRequest(
                    id,
                    editPostState.value.description,
                    editPostState.value.hashtags,
                )
            ).onSuccess {
                _editPostRequestState.emit(RequestState.SUCCESS)
                _editPostState = mutableStateOf(
                    EditPostState()
                )
            }.onFailure {
                if(it is HttpException){
                    val error : ApiException = ErrorUtils().parseError(it)
                    _editPostRequestState.emit(RequestState.FAILURE(error.title))
                }
                else
                    _editPostRequestState.emit(RequestState.FAILURE(it.localizedMessage))
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getPostAndHashtags(id : String) = viewModelScope.launch(_errorHandler){
        _getPostRequestState.emit(RequestState.LOADING)
        _postRepository.getPost(id).onSuccess { post ->
            _post.postValue(post)
            _hashtagRepository.getHashtags().onSuccess { hashtags ->
                _hashtags.postValue(hashtags)
                _getPostRequestState.emit(RequestState.SUCCESS)
            }.onFailure {
                if(it is HttpException){
                    val error : ApiException = ErrorUtils().parseError(it)
                    _getPostRequestState.emit(RequestState.FAILURE(error.title, isApiError = true))
                }
                else
                    _getPostRequestState.emit(RequestState.FAILURE(it.localizedMessage))
            }
        }.onFailure {
            if(it is HttpException){
                val error : ApiException = ErrorUtils().parseError(it)
                _getPostRequestState.emit(RequestState.FAILURE(error.title, isApiError = true))
            }
            else
                _getPostRequestState.emit(RequestState.FAILURE(it.localizedMessage))
        }
    }
}