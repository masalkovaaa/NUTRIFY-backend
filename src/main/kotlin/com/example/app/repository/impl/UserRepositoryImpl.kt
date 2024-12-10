package com.example.app.repository.impl

import com.example.app.dto.auth.RegistrationRequest
import com.example.app.dto.user.UserDto
import com.example.app.dto.user.UserUpdateDto
import com.example.app.model.PersonalData
import com.example.app.model.PersonalDataDao
import com.example.app.model.UserDao
import com.example.app.model.Users
import com.example.app.model.enum.Role
import com.example.app.repository.UserRepository
import com.example.plugins.exception.NotFoundException
import com.example.plugins.extension.db.dbQuery
import org.jetbrains.exposed.sql.ResultRow
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

    override fun findUserProfileById(id: Long) = dbQuery {
        (Users innerJoin PersonalData)
            .select(Users.columns + PersonalData.columns)
            .where { Users.id eq id }
            .firstOrNull()
            ?.let { toUserDto(it) }
            ?: throw NotFoundException("User with id $id not found")
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
        UserDao.findByIdAndUpdate(userId) { dao ->
            userUpdateDto.email?.let { dao.email = userUpdateDto.email }
            userUpdateDto.name?.let { dao.name = userUpdateDto.name }
            userUpdateDto.password?.let { dao.password = BCrypt.hashpw(userUpdateDto.password, BCrypt.gensalt()) }
        }?.let { UserUpdateDto(it.name, it.email, null) }
            ?: throw NotFoundException("User with id $userId does not exist")
    }

    private fun toUserDto(row: ResultRow) = UserDto(
        id =  row[Users.id].value,
        email = row[Users.email],
        name = row[Users.name],
        personalData = PersonalDataDao.wrapRow(row).toSerializable()
    )
}
