package com.catpedigree.capstone.catpedigreebase.data.local.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.PostRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems
import com.catpedigree.capstone.catpedigreebase.data.network.item.RemoteItems
import com.catpedigree.capstone.catpedigreebase.utils.resource.wrapEspressoIdlingResource
import retrofit2.HttpException
import java.io.IOException

//private const val INITIAL_PAGE_INDEX = 1
//
//@OptIn(ExperimentalPagingApi::class)
//class PostRemoteMediator(
//    private val token: String,
//    private val postRemoteDataSource: PostRemoteDataSource,
//    private val catDatabase: CatDatabase,
//) : RemoteMediator<Int, PostItems>() {
//
//    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
//    }
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, PostItems>,
//    ): MediatorResult {
//        wrapEspressoIdlingResource {
//            val page = when (loadType) {
//                LoadType.REFRESH -> {
//                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                    remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
//                }
//                LoadType.PREPEND -> {
//                    val remoteKeys = getRemoteKeyForFirstItem(state)
//                    val prevKey = remoteKeys?.prevKey
//                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                    prevKey
//                }
//                LoadType.APPEND -> {
//                    val remoteKeys = getRemoteKeyForLastItem(state)
//                    val nextKey = remoteKeys?.nextKey
//                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                    nextKey
//                }
//            }
//
//            val stories = mutableListOf<PostItems>()
//            try {
//                val responses = postRemoteDataSource.getPost(
//                    token,
//                    page,
//                    state.config.pageSize
//                )
//
//                val endOfPaginationReached = responses.body()?.data.isNullOrEmpty()
//                responses.body()?.data?.forEach {
//                    stories.add(
//                        PostItems(
//                            id = it.id,
//                            name = it.user?.name,
//                            profile_photo_path = it.user?.profile_photo_path,
//                            photo = it.photo,
//                            description = it.description,
//                            created_at = it.created_at,
//                            loves_count = it.loves_count,
//                            comments_count = it.comments_count,
//                            isBookmarked = false
//                        )
//                    )
//                }
//
//                catDatabase.withTransaction {
//                    if (loadType == LoadType.REFRESH) {
//                        catDatabase.remoteItemsDao().deleteRemoteKeys()
//                        catDatabase.postDao().deleteAllPosts()
//                    }
//                    val prevKey = if (page == 1) null else page - 1
//                    val nextKey = if (endOfPaginationReached) null else page + 1
//                    val keys = stories.map {
//                        RemoteItems(id = it.id, prevKey = prevKey, nextKey = nextKey)
//                    }
//                    catDatabase.remoteItemsDao().insertAll(keys)
//                    catDatabase.postDao().insertStory(stories)
//                }
//                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
//            } catch (e: IOException) {
//                return MediatorResult.Error(e)
//            } catch (e: HttpException) {
//                return MediatorResult.Error(e)
//            }
//        }
//    }
//
//    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PostItems>): RemoteItems? {
//        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
//            catDatabase.remoteItemsDao().getRemoteKeysById(data.id)
//        }
//    }
//
//    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PostItems>): RemoteItems? {
//        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
//            catDatabase.remoteItemsDao().getRemoteKeysById(data.id)
//        }
//    }
//
//    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PostItems>): RemoteItems? {
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?.id?.let { id ->
//                catDatabase.remoteItemsDao().getRemoteKeysById(id)
//            }
//        }
//    }
//}