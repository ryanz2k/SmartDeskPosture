package com.eldroid.smartdeskposture.data

import com.google.firebase.database.FirebaseDatabase
import com.eldroid.smartdeskposture.model.User
import kotlinx.coroutines.tasks.await

object FirebaseDatabaseService {
    
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")
    
    suspend fun saveUser(user: User): Result<Unit> {
        return try {
            usersRef.child(user.id).setValue(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            when {
                e.message?.contains("Permission denied") == true -> {
                    Result.failure(Exception("Permission denied: Please check Firebase Database rules"))
                }
                e.message?.contains("Network error") == true -> {
                    Result.failure(Exception("Network error: Please check your internet connection"))
                }
                else -> {
                    Result.failure(Exception("Database error: ${e.message}"))
                }
            }
        }
    }
    
    suspend fun getUserById(userId: String): Result<User?> {
        return try {
            val snapshot = usersRef.child(userId).get().await()
            val user = snapshot.getValue(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            when {
                e.message?.contains("Permission denied") == true -> {
                    Result.failure(Exception("Permission denied: Please check Firebase Database rules"))
                }
                else -> Result.failure(e)
            }
        }
    }
    
    suspend fun getUserByEmail(email: String): Result<User?> {
        return try {
            val snapshot = usersRef.orderByChild("email").equalTo(email).get().await()
            val user = snapshot.children.firstOrNull()?.getValue(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            when {
                e.message?.contains("Permission denied") == true -> {
                    Result.failure(Exception("Permission denied: Please check Firebase Database rules"))
                }
                else -> Result.failure(e)
            }
        }
    }
    
    suspend fun updateUser(user: User): Result<Unit> {
        return try {
            usersRef.child(user.id).setValue(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            when {
                e.message?.contains("Permission denied") == true -> {
                    Result.failure(Exception("Permission denied: Please check Firebase Database rules"))
                }
                else -> Result.failure(e)
            }
        }
    }
    
    suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            usersRef.child(userId).removeValue().await()
            Result.success(Unit)
        } catch (e: Exception) {
            when {
                e.message?.contains("Permission denied") == true -> {
                    Result.failure(Exception("Permission denied: Please check Firebase Database rules"))
                }
                else -> Result.failure(e)
            }
        }
    }
}