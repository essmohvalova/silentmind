package com.example.coursework_app.data.mappers.users

import com.example.coursework_app.data.db.entity.user.CharacterTypeDb
import com.example.coursework_app.data.db.entity.user.UserEntityDb
import com.example.coursework_app.domain.model.user.CharacterType
import com.example.coursework_app.domain.model.user.User
import javax.inject.Inject

class UserDomainToDbMapper @Inject constructor() : (User) -> UserEntityDb {

    override fun invoke(userDomain: User): UserEntityDb {
        return UserEntityDb(
            id = userDomain.id,
            name = userDomain.name,
            email = userDomain.email,
            characterType = userDomain.selectedCharacter.toDb(),
        )
    }

    private fun CharacterType.toDb(): CharacterTypeDb {
        return when(this) {
            CharacterType.CAT -> CharacterTypeDb.CAT
            CharacterType.DOG -> CharacterTypeDb.DOG
            CharacterType.EMOJI -> CharacterTypeDb.EMOJI
        }
    }
}
