package com.msomodi.imageverse.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.msomodi.imageverse.api.ImageverseApi
import com.msomodi.imageverse.db.ImageverseDatabase
import com.msomodi.imageverse.model.post.request.CreatePostRequest
import com.msomodi.imageverse.model.post.request.EditPostRequest
import com.msomodi.imageverse.model.post.response.PostResponse
import com.msomodi.imageverse.paging.PostsRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalPagingApi
class PostRepository @Inject constructor(
    private val _imageverseApi: ImageverseApi,
    private val _imageverseDatabase: ImageverseDatabase
) {
    fun getPosts(): Flow<PagingData<PostResponse>> {
        val pagingSource = { _imageverseDatabase.postsDao().getPosts() }

        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = PostsRemoteMediator(
                _imageverseApi,
                _imageverseDatabase
            ),
            pagingSourceFactory = pagingSource
        ).flow
    }

    suspend fun createPost(
        createPostRequest: CreatePostRequest
    ) : Result<PostResponse> {
        val result =  withContext(Dispatchers.IO){
            _imageverseApi.createPost(createPostRequest)
        }
        result.onSuccess {
            addToDb(it)
        }
        return result;
    }

    suspend fun getPost(
        id : String
    ) : Result<PostResponse> {
        val result =  withContext(Dispatchers.IO){
            _imageverseApi.getPost(id)
        }
        return result;
    }

    suspend fun updatePost(
        editPostRequest: EditPostRequest
    ) : Result<PostResponse> {
        val result =  withContext(Dispatchers.IO){
            _imageverseApi.putPost(editPostRequest)
        }
        result.onSuccess {
            updateInDb(it)
        }
        return result;
    }

    suspend fun deletePost(
        id : String,
        dbId : Int
    ) : Unit {
        val result = withContext(Dispatchers.IO){
            _imageverseApi.deletePost(id)
        }
        result.onSuccess {
            deleteInDb(dbId)
        }
    }

    suspend fun addToDb(post: PostResponse) = _imageverseDatabase.postsDao().addPost(post)

    suspend fun updateInDb(post: PostResponse) = _imageverseDatabase.postsDao().update(post)

    suspend fun deleteInDb(id : Int) = _imageverseDatabase.postsDao().deleteByPostId(id)
}