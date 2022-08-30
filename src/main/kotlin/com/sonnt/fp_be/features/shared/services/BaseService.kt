package com.sonnt.fp_be.features.shared.services

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.security.core.context.SecurityContextHolder

open class BaseService {
    val modelMapper = ModelMapper().apply {
        configuration.matchingStrategy = MatchingStrategies.STRICT
    }

    val username: String
        get() = SecurityContextHolder.getContext().authentication.principal as String

    val userId: Long
        get() = SecurityContextHolder.getContext().authentication.details as Long
}