package com.mehmetalican.fridgechef.data.manager

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.mehmetalican.fridgechef.domain.manager.TranslationManager
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleTranslationManager @Inject constructor() : TranslationManager {

    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.TURKISH)
        .build()

    private val translator = Translation.getClient(options)

    override suspend fun translate(text: String): String {
        return try {
            // First, ensure the model is downloaded
            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
            
            // We use downloadModelIfNeeded. It's a no-op if already downloaded.
            translator.downloadModelIfNeeded(conditions).await()

            // Translate
            translator.translate(text).await()
        } catch (e: Exception) {
            // Fallback to original text if translation fails (e.g. no internet for first download)
            e.printStackTrace()
            text
        }
    }
}
