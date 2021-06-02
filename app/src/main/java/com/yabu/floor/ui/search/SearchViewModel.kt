package com.yabu.floor.ui.search

import androidx.lifecycle.ViewModel
import com.yabu.floor.data.model.search.SearchHistoryItem
import com.yabu.floor.data.repositories.IEXCloudRepository
import com.yabu.floor.data.repositories.SearchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val iexRepository: IEXCloudRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {
    suspend fun getSearch(search: String) = iexRepository.querySearch(search)

    suspend fun getSectorPerformance() = iexRepository.getSectorPerformance()

    suspend fun getSearcHistory() = searchHistoryRepository.getAll()

    suspend fun insertSearcHistory(item: SearchHistoryItem) = searchHistoryRepository.insert(item)
}