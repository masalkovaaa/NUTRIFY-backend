package com.example.app.service.impl

import com.example.app.repository.impl.AdminRepository

interface AdminService {
    fun findTableByName(name: String): List<Map<String, Any?>>
}

class AdminServiceImpl(
    private val adminRepository: AdminRepository
) : AdminService {

    override fun findTableByName(name: String) = adminRepository.findTableByName(name)
}