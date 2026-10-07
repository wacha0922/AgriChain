package com.example.agrichain.data.profile

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserProfileRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            firestore
                .collection("users")
                .document(profile.uid)
                .set(profile)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(uid: String): Result<UserProfile?> {
        return try {
            val document = firestore
                .collection("users")
                .document(uid)
                .get()
                .await()

            if (document.exists()) {
                Result.success(document.toObject(UserProfile::class.java))
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}