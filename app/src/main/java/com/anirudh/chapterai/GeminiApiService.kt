package com.anirudh.chapterai

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.TextPart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiApiService {
    private const val API_KEY = ""

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = API_KEY
    )

    suspend fun sendGeminiRequest(prompt: String, context: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val inputContent = Content(
                    parts = listOf(TextPart("Context: $context\nPrompt: $prompt"))
                )
                val response = generativeModel.generateContent(inputContent)
                response.text ?: "No response"
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }
    }
}