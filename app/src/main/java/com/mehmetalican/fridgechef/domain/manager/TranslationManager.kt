package com.mehmetalican.fridgechef.domain.manager

interface TranslationManager {
    suspend fun translate(text: String): String
}
