package com.example.app.repository.impl

import com.example.app.dto.auth.RegistrationRequest
import com.example.app.model.User
import com.example.app.model.UserDao
import com.example.app.model.Users
import com.example.app.model.enum.Role
import com.example.app.repository.UserRepository
import com.example.plugins.extension.db.dbQuery
import org.mindrot.jbcrypt.BCrypt

class UserRepositoryImpl : UserRepository {

    override fun findAll(): List<User> = dbQuery {
        UserDao.all()
            .map { it.toSerializable() }
    }

    override fun findByUsername(username: String) = dbQuery {
        UserDao.find { Users.username eq username }
            .firstOrNull()
            ?.toSerializable()
    }

    override fun findById(id: Long) = dbQuery {
        UserDao.findById(id)
            ?.toSerializable()
    }

    override fun existsByUsername(username: String) = dbQuery {
        UserDao.find { Users.username eq username }
            .any()
    }

    override fun save(registrationRequest: RegistrationRequest)= dbQuery {
        UserDao.new {
            firstName = registrationRequest.firstName
            lastName = registrationRequest.lastName
            username = registrationRequest.username
            password = BCrypt.hashpw(registrationRequest.password, BCrypt.gensalt())
            role = Role.USER
        }.toSerializable()
    }
}
