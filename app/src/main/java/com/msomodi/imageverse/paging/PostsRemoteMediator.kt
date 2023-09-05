package com.msomodi.imageverse.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.msomodi.imageverse.api.ImageverseApi
import com.msomodi.imageverse.db.ImageverseDatabase
import com.msomodi.imageverse.model.post.PostRemoteKeys
import com.msomodi.imageverse.model.post.response.PostResponse

@ExperimentalPagingApi
class PostsRemoteMediator(
    private val imageverseApi: ImageverseApi,
    private val imageverseDatabase: ImageverseDatabase
) : RemoteMediator<Int, PostResponse>() {
    private val postsDao = imageverseDatabase.postsDao()
    private val postsRemoteKeysDao = imageverseDatabase.postRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostResponse>
    ): MediatorResult {
        Log.d("MEDIATOR", loadType.toString())
        return try {
            val currentPage = when(loadType) {
                LoadType.REFRESH -> {
                    val postRemoteKeys: PostRemoteKeys? = getPostRemoteKeysClosestToCurrentPosition(state)
                    postRemoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val postRemoteKeys: PostRemoteKeys? = getPostRemoteKeysForFirstItem(state)
                    val prevPage = postRemoteKeys?.prevPage
                        ?: return MediatorResult.Success(postRemoteKeys != null)
                    prevPage
                }
                LoadType.APPEND -> {
                    val postRemoteKeys: PostRemoteKeys? = getPostRemoteKeysForLastItem(state)
                    val nextPage = postRemoteKeys?.nextPage
                        ?: return MediatorResult.Success(postRemoteKeys != null)
                    nextPage
                }
            }

            val response = imageverseApi.getPosts(page = currentPage).sortedByDescending { it.updatedAtDateTime }
            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            imageverseDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    postsDao.deletePosts()
                    postsRemoteKeysDao.deletePostRemoteKeys()
                }
                val postRemoteKeys = response.map { post ->
                    PostRemoteKeys(
                        id = post.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }

                postsRemoteKeysDao.addPostRemoteKeys(postRemoteKeys = postRemoteKeys)
                postsDao.addPosts(posts = response)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getPostRemoteKeysForFirstItem(state: PagingState<Int, PostResponse>): PostRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { post ->
            postsRemoteKeysDao.getPostRemoteKeys(id = post.id)
        }
    }

    private suspend fun getPostRemoteKeysForLastItem(state: PagingState<Int, PostResponse>): PostRemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { post ->
            postsRemoteKeysDao.getPostRemoteKeys(id = post.id)
        }
    }

    private suspend fun getPostRemoteKeysClosestToCurrentPosition(state: PagingState<Int, PostResponse>): PostRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                postsRemoteKeysDao.getPostRemoteKeys(id = id)
            }
        }
    }
}