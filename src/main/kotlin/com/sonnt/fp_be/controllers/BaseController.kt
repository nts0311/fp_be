package com.sonnt.fp_be.controllers

import com.sonnt.fp_be.services.UserService
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder

open class BaseController {
    @Autowired
    lateinit var userService: UserService


    val modelMapper = ModelMapper().apply {
        configuration.matchingStrategy = MatchingStrategies.STRICT
    }

    val username: String
        get() = SecurityContextHolder.getContext().authentication.principal as String

    val userId: Long
        get() = SecurityContextHolder.getContext().authentication.details as Long
}