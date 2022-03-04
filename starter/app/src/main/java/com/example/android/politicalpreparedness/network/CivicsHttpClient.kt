package com.example.android.politicalpreparedness.network

import okhttp3.OkHttpClient

class CivicsHttpClient: OkHttpClient() {

    companion object {

        const val API_KEY = "AIzaSyDHyR7bsE5AuJtNP21zB2UiKiY47nqiIqA" //TODO: Place your API Key Here

        //AIzaSyDE8vRYm-Gbac0pFQqLhvoBCNv2-ftICNw
        //AIzaSyDHyR7bsE5AuJtNP21zB2UiKiY47nqiIqA
        fun getClient(): OkHttpClient {
            return Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val url = original
                                .url()
                                .newBuilder()
                                .addQueryParameter("key", API_KEY)
                                .build()
                        val request = original
                                .newBuilder()
                                .url(url)
                                .build()
                        chain.proceed(request)
                    }
                    .build()
        }

    }

}