package com.example.app.repository.impl

import com.example.app.dto.User
import com.example.app.dto.auth.RegistrationRequest
import com.example.app.model.Users
import com.example.app.model.Users.fromResultRow
import com.example.app.model.enum.Role
import com.example.app.repository.UserRepository
import com.example.plugins.extension.db.dbQuery
import com.example.plugins.extension.db.map
import org.jetbrains.exposed.sql.insertReturning
import org.jetbrains.exposed.sql.selectAll
import org.mindrot.jbcrypt.BCrypt

class UserRepositoryImpl : UserRepository {

    override fun findAll(): List<User> = dbQuery {
        Users.selectAll()
            .map { fromResultRow(it) }
    }

    override fun findByUsername(username: String) = dbQuery {
        Users.selectAll()
            .where { Users.username eq username }
            .firstOrNull()
            ?.map { fromResultRow(it) }
    }

    override fun findById(id: Long): User? {
        return Users.selectAll()
            .where { Users.id eq id }
            .firstOrNull()
            ?.map { fromResultRow(it) }
    }

    override fun existsByUsername(username: String) = dbQuery {
        Users.selectAll()
            .where { Users.username eq username }
            .any()
    }

    override fun save(registrationRequest: RegistrationRequest)= dbQuery {
        Users.insertReturning {
            it[username] = registrationRequest.username
            it[password] = BCrypt.hashpw(registrationRequest.password, BCrypt.gensalt())
            it[role] = Role.USER
        }.first()
            .map { fromResultRow(it) }
    }
}
