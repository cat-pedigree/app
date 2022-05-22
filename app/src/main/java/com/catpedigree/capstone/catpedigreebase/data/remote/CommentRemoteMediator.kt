package com.catpedigree.capstone.catpedigreebase.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.catpedigree.capstone.catpedigreebase.data.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.item.CommentItems
import com.catpedigree.capstone.catpedigreebase.data.item.RemoteCommentItems
import com.catpedigree.capstone.catpedigreebase.utils.wrapEspressoIdlingResource
import retrofit2.HttpException
import java.io.IOException

private const val INITIAL_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class CommentRemoteMediator(
    private val token: String,
    private val post_id: Int,
    private val commentRemoteDataSource: CommentRemoteDataSource,
    private val catDatabase: CatDatabase,
) : RemoteMediator<Int, CommentItems>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CommentItems>,
    ): MediatorResult {
        wrapEspressoIdlingResource {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            val comments = mutableListOf<CommentItems>()
            try {
                val responses = commentRemoteDataSource.getComment(
                    token,
                    post_id,
                    page,
                    state.config.pageSize
                )

                val endOfPaginationReached = responses.body()?.data.isNullOrEmpty()
                responses.body()?.data?.forEach {
                    comments.add(
                        CommentItems(
                            id = it.id,
                            post_id = it.post_id,
                            user_id = it.user_id,
                            description = it.description
                        )
                    )
                }

                catDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        catDatabase.remoteCommentItemsDao().deleteRemoteKeys()
                        catDatabase.commentDao().deleteAllComments()
                    }
                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = comments.map {
                        RemoteCommentItems(id = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    catDatabase.remoteCommentItemsDao().insertAll(keys)
                    catDatabase.commentDao().insertComment(comments)
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } catch (e: IOException) {
                return MediatorResult.Error(e)
            } catch (e: HttpException) {
                return MediatorResult.Error(e)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CommentItems>): RemoteCommentItems? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            catDatabase.remoteCommentItemsDao().getRemoteKeysById(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, CommentItems>): RemoteCommentItems? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            catDatabase.remoteCommentItemsDao().getRemoteKeysById(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CommentItems>): RemoteCommentItems? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                catDatabase.remoteCommentItemsDao().getRemoteKeysById(id)
            }
        }
    }
}