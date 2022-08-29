package com.sonnt.fp_be.utils

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies

val sharedModelMapper = ModelMapper().apply {
    configuration.matchingStrategy = MatchingStrategies.STRICT
}