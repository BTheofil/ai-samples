/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.android.ai.catalog.ui.domain

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.android.ai.catalog.R
import com.android.ai.samples.geminichatbot.GeminiChatbotScreen
import com.android.ai.samples.geminimultimodal.GeminiMultimodalScreen
import com.android.ai.samples.geminivideosummary.VideoSummarizationScreen
import com.android.ai.samples.genai_image_description.GenAIImageDescriptionScreen
import com.android.ai.samples.genai_summarization.GenAISummarizationScreen
import com.android.ai.samples.genai_writing_assistance.GenAIWritingAssistanceScreen
import com.android.ai.samples.imagen.ImagenScreen
import com.android.ai.samples.magicselfie.MagicSelfieScreen
import hu.tb.barcode.BarcodeScreen
import hu.tb.track_object.ObjectDetectionScreen

class SampleCatalog(
    context: Context
) {
    val list = listOf(
        SampleCatalogItem(
            title = context.getString(R.string.gemini_multimodal_sample_title),
            description = context.getString(R.string.gemini_multimodal_sample_description),
            route = "GeminiMultimodalScreen",
            sampleEntryScreen = { GeminiMultimodalScreen() },
            tags = listOf(SampleTags.GEMINI_2_0_FLASH, SampleTags.FIREBASE),
            needsFirebase = true
        ),
        SampleCatalogItem(
            title = context.getString(R.string.gemini_chatbot_sample_title),
            description = context.getString(R.string.gemini_chatbot_sample_description),
            route = "GeminiChitchatScreen",
            sampleEntryScreen = { GeminiChatbotScreen() },
            tags = listOf(SampleTags.GEMINI_2_0_FLASH, SampleTags.FIREBASE),
            needsFirebase = true
        ),
        SampleCatalogItem(
            title = context.getString(R.string.genai_summarization_sample_title),
            description = context.getString(R.string.genai_summarization_sample_description),
            route = "GenAISummarizationScreen",
            sampleEntryScreen = { GenAISummarizationScreen() },
            tags = listOf(SampleTags.GEMINI_NANO, SampleTags.ML_KIT)
        ),
        SampleCatalogItem(
            title = context.getString(R.string.genai_image_description_sample_title),
            description = context.getString(R.string.genai_image_description_sample_description),
            route = "GenAIImageDescriptionScreen",
            sampleEntryScreen = { GenAIImageDescriptionScreen() },
            tags = listOf(SampleTags.GEMINI_NANO, SampleTags.ML_KIT)
        ),
        SampleCatalogItem(
            title = context.getString(R.string.genai_writing_assistance_sample_title),
            description = context.getString(R.string.genai_writing_assistance_sample_description),
            route = "GenAIWritingAssistanceScreen",
            sampleEntryScreen = { GenAIWritingAssistanceScreen() },
            tags = listOf(SampleTags.GEMINI_NANO, SampleTags.ML_KIT)
        ),
        SampleCatalogItem(
            title = context.getString(R.string.imagen_sample_title),
            description = context.getString(R.string.imagen_sample_description),
            route = "ImagenImageGenerationScreen",
            sampleEntryScreen = { ImagenScreen() },
            tags = listOf(SampleTags.IMAGEN, SampleTags.FIREBASE),
            needsFirebase = true
        ),
        SampleCatalogItem(
            title = context.getString(R.string.magic_selfie_sample_title),
            description = context.getString(R.string.magic_selfie_sample_description),
            route = "MagicSelfieScreen",
            sampleEntryScreen = { MagicSelfieScreen() },
            tags = listOf(SampleTags.IMAGEN, SampleTags.FIREBASE, SampleTags.ML_KIT),
            needsFirebase = true
        ),
        SampleCatalogItem(
            title = context.getString(R.string.gemini_video_summarization_sample_title),
            description = context.getString(R.string.gemini_video_summarization_sample_description),
            route = "VideoSummarizationScreen",
            sampleEntryScreen = { VideoSummarizationScreen() },
            tags = listOf(SampleTags.GEMINI_2_0_FLASH, SampleTags.FIREBASE, SampleTags.MEDIA3),
            needsFirebase = true
        ),
        SampleCatalogItem(
            title = "Barcode",
            description = "barcode scanner",
            route = "BarcodeScreen",
            sampleEntryScreen = { BarcodeScreen() },
            tags = listOf(SampleTags.ML_KIT)
        ),
        SampleCatalogItem(
            title = "Object detection",
            description = "object detection",
            route = "ObjectDetectionScreen",
            sampleEntryScreen = { ObjectDetectionScreen() },
            tags = listOf(SampleTags.ML_KIT)
        )
    )
}

data class SampleCatalogItem(
    val title: String,
    val description: String,
    val route: String,
    val sampleEntryScreen: @Composable () -> Unit,
    val tags: List<SampleTags> = emptyList(),
    val needsFirebase: Boolean = false
)

enum class SampleTags(
    val label: String,
    val backgroundColor: Color,
    val textColor: Color
) {
    FIREBASE("Firebase", Color(0xFFFF9100), Color.White),
    GEMINI_2_0_FLASH("Gemini 2.0 Flash", Color(0xFF4285F4), Color.White),
    GEMINI_NANO("Gemini Nano", Color(0xFF7abafe), Color.White),
    IMAGEN("Imagen", Color(0xFF7CB342), Color.White),
    MEDIA3("Media3", Color(0xFF7CB584), Color.White),
    ML_KIT("ML Kit", Color.White, Color(0xFF4285F4))
}

