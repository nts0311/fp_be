package com.sonnt.fp_be.features.base.services

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies

open class BaseService {
    val modelMapper = ModelMapper().apply {
        configuration.matchingStrategy = MatchingStrategies.STRICT
    }
}