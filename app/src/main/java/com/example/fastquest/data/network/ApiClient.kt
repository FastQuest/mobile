package com.example.fastquest.data.network

import android.content.Context
import com.example.fastquest.BuildConfig
import com.example.fastquest.data.local.TokenManager
import com.example.fastquest.data.network.service.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/** Provides configured Retrofit API services for the application container. */
class ApiClient(context: Context) {
    val tokenManager = TokenManager(context.applicationContext)

    private val retrofit = createRetrofit()

    /**
     * Create and configure Retrofit instance
     */
    private fun createRetrofit(): Retrofit {
        // Logging interceptor for debugging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            redactHeader("Authorization")
        }
        
        // OkHttp client with interceptors
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        // Moshi for JSON parsing
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        
        // Retrofit instance
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
    
    /**
     * Get TokenManager instance
     */
    val authService: AuthApiService = retrofit.create(AuthApiService::class.java)
    val questionSetsService: QuestionSetsApiService = retrofit.create(QuestionSetsApiService::class.java)
    val questionsService: QuestionsApiService = retrofit.create(QuestionsApiService::class.java)
    val submissionsService: SubmissionsApiService = retrofit.create(SubmissionsApiService::class.java)
    val answersService: AnswersApiService = retrofit.create(AnswersApiService::class.java)
}