@file:Suppress("BlockingMethodInNonBlockingContext")

package com.yabu.floor.data

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import retrofit2.Call
import java.util.*

/**
 * Defines loading from network a resource of type [K]
 * to map into an output [LiveData] resource of type [T].
 */
abstract class NetworkResource<T, K> {
    private val logtag = this::class.simpleName

    private val coroutine = CoroutineScope(Dispatchers.Main + Job())

    abstract fun loadFromDisk(): T

    abstract fun shouldFetch(disk: T?): Boolean

    abstract fun loadData(): Call<K>

    abstract fun processResponse(data: K): T

    abstract fun writeToDisk(data: T): Boolean

    suspend fun emit(): Resource<T> {
        val cache = withContext(Dispatchers.IO) {
            loadFromDisk()
        }

        if (!shouldFetch(cache)) {
            return Resource.success(cache)
        }

        try {
            Log.d(logtag, "Beginning network call...")

            val call = loadData()

            Log.d(
                logtag,
                "Calling ${call.request().method.toUpperCase(Locale.ROOT)} " +
                        "${call.request().url}"
            )
            val response = withContext(Dispatchers.Default) {
                call.execute()
            }

            if (!response.isSuccessful || response.body() == null) {
                return Resource.error(response.errorBody().toString(), cache)
            }

            val processedResponse = processResponse(response.body()!!)

            withContext(Dispatchers.IO) {
                writeToDisk(processedResponse)
            }

            val refreshedData = withContext(Dispatchers.IO) {
                loadFromDisk()
            }

            return Resource.success(refreshedData)
        } catch (exception: Exception) {
            Log.d(logtag,
                "Error caught in network call: ${exception.message}")

            return Resource.error(exception.message ?: "", cache)
        }
    }
}