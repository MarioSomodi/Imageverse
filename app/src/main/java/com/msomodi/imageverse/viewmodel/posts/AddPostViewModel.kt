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
import com.msomodi.imageverse.model.packages.response.PackageResponse
import com.msomodi.imageverse.model.post.request.CreatePostRequest
import com.msomodi.imageverse.repository.HashtagRepository
import com.msomodi.imageverse.repository.PostRepository
import com.msomodi.imageverse.view.main.AddPostState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalPagingApi::class)
class AddPostViewModel
@Inject constructor(
    private val _postRepository: PostRepository,
    private val _hashtagRepository: HashtagRepository
) : ViewModel(){

    private val _hashtags : MutableLiveData<List<HashtagResponse>?> = MutableLiveData()
    val hashtags: LiveData<List<HashtagResponse>?>
        get() = _hashtags

    private var _addPostState = mutableStateOf(
        AddPostState()
    )
    val addPostState: State<AddPostState>
        get() = _addPostState

    private val _addPostRequestState = MutableStateFlow<RequestState>(RequestState.START)

    private val _addPostHashtagsRequestState = MutableStateFlow<RequestState>(RequestState.START)

    val addPostRequestState : Flow<RequestState>
        get() = _addPostRequestState

    val addPostHashtagsRequestState : Flow<RequestState>
        get() = _addPostHashtagsRequestState


    fun onSaveImageAs(saveImageAs: String){
        _addPostState.value = _addPostState.value.copy(
            saveImageAs = saveImageAs,
            isSaveImageValid = listOf("png", "jpeg", "bmp").contains(saveImageAs)
        )
    }

    fun onDescriptionChanged(description: String){
        _addPostState.value = _addPostState.value.copy(
            description = description,
            isDescriptionValid = description.isNotEmpty()
        )
    }

    fun onBase64ImageChanged(base64Image: String){
        _addPostState.value = _addPostState.value.copy(
            base64Image = base64Image,
            isBase64ImageValid = base64Image.isNotEmpty()
        )
    }

    fun onHashtagAdded(hashtag: String){
        val hashtags = _addPostState.value.hashtags.plusElement(hashtag)
        _addPostState.value = _addPostState.value.copy(
            hashtags = hashtags,
            isHashtagsValid = hashtags.isNotEmpty()
        )
    }

    fun onHashtagRemoved(hashtag: String){
        val hashtags = _addPostState.value.hashtags.minusElement(hashtag)
        _addPostState.value = _addPostState.value.copy(
            hashtags = hashtags,
            isHashtagsValid = hashtags.isNotEmpty()
        )
    }

    private val _errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("ADD_POST_VIEW_MODEL", e.toString(), e)
    }

    fun addPost(id : String){
        viewModelScope.launch (_errorHandler){
            _addPostRequestState.emit(RequestState.LOADING)
            _postRepository.createPost(
                CreatePostRequest(
                    id,
                    addPostState.value.description,
                    addPostState.value.base64Image,
                    addPostState.value.hashtags,
                    addPostState.value.saveImageAs
                )
            ).onSuccess {
                _addPostRequestState.emit(RequestState.SUCCESS)
                _addPostState = mutableStateOf(
                    AddPostState()
                )
            }.onFailure {
                if(it is HttpException){
                    val error : ApiException = ErrorUtils().parseError(it)
                    _addPostRequestState.emit(RequestState.FAILURE(error.title))
                }
                else
                    _addPostRequestState.emit(RequestState.FAILURE(it.localizedMessage))
            }
        }
    }

    fun getHashtags() = viewModelScope.launch(_errorHandler){
        _addPostHashtagsRequestState.emit(RequestState.LOADING)
        _hashtagRepository.getHashtags().onSuccess {
            _hashtags.postValue(it)
            _addPostHashtagsRequestState.emit(RequestState.SUCCESS)
        }.onFailure {
            if(it is HttpException){
                val error : ApiException = ErrorUtils().parseError(it)
                _addPostHashtagsRequestState.emit(RequestState.FAILURE(error.title, isApiError = true))
            }
            else
                _addPostHashtagsRequestState.emit(RequestState.FAILURE(it.localizedMessage))
        }
    }
    init {
        getHashtags()
    }
}