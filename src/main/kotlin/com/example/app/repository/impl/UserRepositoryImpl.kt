package com.example.app.repository.impl

import com.example.app.dto.auth.RegistrationRequest
import com.example.app.dto.user.UserUpdateDto
import com.example.app.model.User
import com.example.app.model.UserDao
import com.example.app.model.Users
import com.example.app.model.enum.Role
import com.example.app.repository.UserRepository
import com.example.plugins.exception.NotFoundException
import com.example.plugins.extension.db.dbQuery
import org.mindrot.jbcrypt.BCrypt

class UserRepositoryImpl : UserRepository {

    override fun findByEmail(email: String) = dbQuery {
        UserDao.find { Users.email eq email }
            .firstOrNull()
            ?.toSerializable()
    }

    override fun findById(id: Long) = dbQuery {
        UserDao.findById(id)
            ?.toSerializable()
    }

    override fun existsByEmail(email: String) = dbQuery {
        UserDao.find { Users.email eq email }
            .any()
    }

    override fun save(registrationRequest: RegistrationRequest)= dbQuery {
        UserDao.new {
            name = registrationRequest.name
            email = registrationRequest.email
            password = BCrypt.hashpw(registrationRequest.password, BCrypt.gensalt())
            role = Role.USER
        }.toSerializable()
    }

    override fun update(userUpdateDto: UserUpdateDto, userId: Long) = dbQuery {
        UserDao.findByIdAndUpdate(userId) {
            it.name = userUpdateDto.email
            it.email = userUpdateDto.email
            it.password = BCrypt.hashpw(userUpdateDto.password, BCrypt.gensalt())
        }?.toSerializable()
            ?: throw NotFoundException("User with id $userId does not exist")
    }
}
