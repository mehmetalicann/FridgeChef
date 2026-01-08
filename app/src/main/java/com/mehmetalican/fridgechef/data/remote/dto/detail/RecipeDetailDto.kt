package com.mehmetalican.fridgechef.data.remote.dto.detail

import com.google.gson.annotations.SerializedName

data class RecipeDetailDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("instructions")
    val instructions: String?,
    @SerializedName("analyzedInstructions")
    val analyzedInstructions: List<AnalyzedInstructionDto>
)

data class AnalyzedInstructionDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("steps")
    val steps: List<StepDto>
)

data class StepDto(
    @SerializedName("number")
    val number: Int,
    @SerializedName("step")
    val step: String
)
