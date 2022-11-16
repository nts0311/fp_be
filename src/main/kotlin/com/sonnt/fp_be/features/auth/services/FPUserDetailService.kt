package com.sonnt.fp_be.features.auth.services

import com.sonnt.fp_be.model.entities.Account
import com.sonnt.fp_be.repos.AccountRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class FPUserDetailService: UserDetailsService {

    @Autowired lateinit var accountRepo: AccountRepo

    fun getUserByUsername(username: String): Account? = accountRepo.findByUsername(username)

    override fun loadUserByUsername(username: String?): UserDetails {
        if(username != null){
            val user = getUserByUsername(username) ?: throw UsernameNotFoundException("Username not found")
            return User(user.username,user.password, listOf(SimpleGrantedAuthority(user.role)))
        }
        throw UsernameNotFoundException("username null")
    }
}