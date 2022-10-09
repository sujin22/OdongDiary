package com.example.owndiary.model

import androidx.compose.runtime.collectAsState
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.owndiary.repository.DiaryRepository
import com.example.owndiary.ui.screen.Sort

class DiarySource(private val repository: DiaryRepository, private val sortState: Sort) : PagingSource<Int, Diary>(){
    private companion object {
        const val INIT_INDEX = 0
    }

    @ExperimentalPagingApi
    override fun getRefreshKey(state: PagingState<Int, Diary>): Int? {
        return state.anchorPosition?.let{achorPosition ->
            state.closestPageToPosition(achorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(achorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Diary> {
        val position = params.key ?: INIT_INDEX
        val loadData = repository.getDiaryPage(position, params.loadSize)
            .filter{
                if(sortState == Sort.FAVORITES)
                it.isFavorite
            else
                true
        }
            .sortedWith(
                Comparator<Diary>{ d1, d2 ->
                    if(sortState == Sort.ASCENDING){
                        d1.date.compareTo(d2.date)
                    }else{
                        d2.date.compareTo(d1.date)
                    }
                }
            )

        return LoadResult.Page(
            data = loadData,
            prevKey = if(position == INIT_INDEX) null else position - 1,
            nextKey = if(loadData.isNullOrEmpty()) null else position + 1
        )
    }
}