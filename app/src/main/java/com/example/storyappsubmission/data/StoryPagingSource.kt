package com.example.storyappsubmission.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyappsubmission.data.remote.response.ListStoryItem
import com.example.storyappsubmission.data.remote.retrofit.StoryService
import retrofit2.HttpException
import java.io.IOException

class StoryPagingSource(private val storyService: StoryService) : PagingSource<Int, ListStoryItem>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        val position = params.key ?: INITIAL_PAGE_INDEX

        return try {
            val response = storyService.getAllPagedStories(position, params.loadSize)

            if (!response.listStory.isNullOrEmpty()) {
                LoadResult.Page(
                    data = response.listStory,
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = if (response.listStory.isEmpty()) null else position + 1
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = null
                )
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}