package com.anirudh.chapterai

data class GeminiDataModels(
    val prompt: String,
    val context: String,
    val temperature: Double = 0.7,
    val maxTokens: Int = 1024
)

data class GeminiResponse(
    val text: String
)