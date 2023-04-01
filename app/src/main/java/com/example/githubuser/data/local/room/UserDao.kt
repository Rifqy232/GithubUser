package com.example.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY username ASC")
    fun getUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users where isFavorite = 1")
    fun getFavoriteUsers(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUsers(user: UserEntity)

    @Update
    fun updateUser(user: UserEntity)

    @Query("DELETE FROM users WHERE isFavorite = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM users WHERE username = :username AND isFavorite = 1)")
    fun isUserFavorite(username: String): Boolean

    @Query("SELECT * FROM users WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<UserEntity>

}