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

/**
 * Singleton object that provides configured Retrofit API services
 */
object ApiClient {
    
    private lateinit var tokenManager: TokenManager
    private lateinit var retrofit: Retrofit
    
    /**
     * Initialize the API client with application context
     * Must be called before accessing any services
     */
    fun initialize(context: Context) {
        tokenManager = TokenManager(context.applicationContext)
        retrofit = createRetrofit()
    }
    
    /**
     * Create and configure Retrofit instance
     */
    private fun createRetrofit(): Retrofit {
        // Logging interceptor for debugging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
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
    fun getTokenManager(): TokenManager {
        checkInitialized()
        return tokenManager
    }
    
    /**
     * Authentication API service
     */
    val authService: AuthApiService by lazy {
        checkInitialized()
        retrofit.create(AuthApiService::class.java)
    }
    
    /**
     * Question Sets API service
     */
    val questionSetsService: QuestionSetsApiService by lazy {
        checkInitialized()
        retrofit.create(QuestionSetsApiService::class.java)
    }
    
    /**
     * Questions API service
     */
    val questionsService: QuestionsApiService by lazy {
        checkInitialized()
        retrofit.create(QuestionsApiService::class.java)
    }
    
    /**
     * Submissions API service
     */
    val submissionsService: SubmissionsApiService by lazy {
        checkInitialized()
        retrofit.create(SubmissionsApiService::class.java)
    }
    
    /**
     * Answers/Performance API service
     */
    val answersService: AnswersApiService by lazy {
        checkInitialized()
        retrofit.create(AnswersApiService::class.java)
    }
    
    /**
     * Check if ApiClient has been initialized
     */
    private fun checkInitialized() {
        if (!::tokenManager.isInitialized || !::retrofit.isInitialized) {
            throw IllegalStateException(
                "ApiClient must be initialized with initialize(context) before use"
            )
        }
    }
}

// Made with Bob
