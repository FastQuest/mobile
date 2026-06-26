package com.example.fastquest.data.model.request

import com.example.fastquest.data.model.response.Answer
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Request model for creating a new submission
 */
@JsonClass(generateAdapter = true)
data class CreateSubmissionRequest(
    @Json(name = "question_set_id")
    val questionSetId: Int,
    
    @Json(name = "answers")
    val answers: List<Answer>
)

// Made with Bob
