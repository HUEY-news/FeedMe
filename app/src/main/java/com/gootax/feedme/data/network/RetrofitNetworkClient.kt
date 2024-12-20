package com.gootax.feedme.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.gootax.feedme.data.dto.Response
import com.gootax.feedme.data.dto.SearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RetrofitNetworkClient @Inject constructor(
    private val context: Context,
    private val service: ApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) return Response().apply { resultCode = -1 }
        if (dto !is SearchRequest) return Response().apply { resultCode = 400 }

        return withContext(Dispatchers.IO) {
            try {
                Log.i("TEST", "Отправка запроса: $dto")
                val response = service.search(dto)
                Log.i("TEST", "Ответ от сервера: ${response}")
                response.apply { resultCode = 200 }
            } catch (exception: Throwable) {
                Log.e("TEST", "Ошибка при запросе: ${exception.message}", exception)
                Response().apply { resultCode = 500 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}
