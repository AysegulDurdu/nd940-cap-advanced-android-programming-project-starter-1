package com.example.android.politicalpreparedness.network

import okhttp3.OkHttpClient

class CivicsHttpClient: OkHttpClient() {

    companion object {

        const val API_KEY = "AIzaSyBf646_NJzPd5EIsIQpLYOzV_SRC5uVUt8" //TODO: Place your API Key Here

        //AIzaSyBf646_NJzPd5EIsIQpLYOzV_SRC5uVUt8
        //AIzaSyDzKzHQIdLiYHSSNCs2adnxhqKnNzFQl_w
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