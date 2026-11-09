package com.example.fastquest

import android.app.Application
import com.example.fastquest.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FastQuestApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            container.tokenManager.migrateLegacyTokens()
            container.tokenManager.getAccessToken().collect(container.tokenManager::cacheAccessToken)
        }
    }
}