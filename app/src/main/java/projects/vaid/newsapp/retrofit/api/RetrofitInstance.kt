package projects.vaid.newsapp.retrofit.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import projects.vaid.newsapp.retrofit.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object{

        private val retrofitInstance by lazy{

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy {
            retrofitInstance.create(NewsAPI::class.java)
        }

    }
}