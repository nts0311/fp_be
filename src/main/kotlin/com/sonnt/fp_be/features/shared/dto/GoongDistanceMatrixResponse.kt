package com.sonnt.fp_be.features.shared.dto

import com.google.gson.annotations.SerializedName

data class GoongDistanceMatrixResponse(
    @SerializedName("rows") var rows: ArrayList<Rows> = arrayListOf()
): java.io.Serializable

data class Rows(
    @SerializedName("elements") var elements: ArrayList<Elements> = arrayListOf()
): java.io.Serializable

data class Elements(
    @SerializedName("status") var status: String? = null,
    @SerializedName("duration") var duration: Duration? = Duration(),
    @SerializedName("distance") var distance: Distance? = Distance()
): java.io.Serializable

data class Duration(
    @SerializedName("text") var text: String? = null,
    @SerializedName("value") var value: Int? = null
): java.io.Serializable

data class Distance(
    @SerializedName("text") var text: String? = null,
    @SerializedName("value") var value: Int? = null
): java.io.Serializable