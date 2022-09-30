package com.sonnt.fp_be.features.shared.services

import com.google.maps.GeoApiContext
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

val mapApiContext = GeoApiContext.Builder()
    .apiKey("AIzaSyDiMTlswUq0ViADeQUUwIn8EPaZd4rBUo8")
    .build()