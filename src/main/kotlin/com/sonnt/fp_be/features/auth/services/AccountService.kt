package com.sonnt.fp_be.features.auth.services

import com.sonnt.fp_be.model.entities.Account
import com.sonnt.fp_be.repos.AccountRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AccountService: UserDetailsService {
    @Autowired
    lateinit var accountRepo: AccountRepo

    override fun loadUserByUsername(username: String?): UserDetails {
        if(username != null){
            val user = getUserByUsername(username) ?: throw UsernameNotFoundException("Username not found")
            return User(user.username,user.password, listOf(SimpleGrantedAuthority(user.role)))
        }
        throw UsernameNotFoundException("username null")
    }

    fun getUserByUsername(username: String): Account? = accountRepo.findByUsername(username)

    fun getAllUser(): List<Account> = accountRepo.findAll()

    fun getFcmToken(userId: Long): String {
        return accountRepo.getById(userId).fcmToken
    }

    fun isUserExist(username: String): Boolean = getUserByUsername(username) != null
    fun isUserExist(userId: Long): Boolean = !accountRepo.findById(userId).isEmpty

    fun updateFcmToken(userId: Long, fcmToken: String) {
        val user = accountRepo.getById(userId)
        user.fcmToken = fcmToken
        accountRepo.save(user)
    }
}