package com.example.agrichain.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository {

    private val auth: FirebaseAuth =
        FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun createAccount(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val result =
                auth.createUserWithEmailAndPassword(
                    email,
                    password
                ).await()

            val user =
                result.user
                    ?: return Result.failure(
                        IllegalStateException(
                            "Account was created but no user was returned."
                        )
                    )

            Result.success(user)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun sendEmailVerification(): Result<Unit> {
        return try {
            val user =
                auth.currentUser
                    ?: return Result.failure(
                        IllegalStateException(
                            "No signed-in user."
                        )
                    )

            user.sendEmailVerification().await()

            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun reloadCurrentUser(): Result<FirebaseUser> {
        return try {
            val user =
                auth.currentUser
                    ?: return Result.failure(
                        IllegalStateException(
                            "No signed-in user."
                        )
                    )

            user.reload().await()

            Result.success(user)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun signIn(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val result =
                auth.signInWithEmailAndPassword(
                    email,
                    password
                ).await()

            val user =
                result.user
                    ?: return Result.failure(
                        IllegalStateException(
                            "Login succeeded but no user was returned."
                        )
                    )

            user.reload().await()

            if (!user.isEmailVerified) {
                auth.signOut()

                return Result.failure(
                    IllegalStateException(
                        "Please verify your email address before signing in."
                    )
                )
            }

            Result.success(user)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun sendPasswordResetEmail(
        email: String
    ): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}