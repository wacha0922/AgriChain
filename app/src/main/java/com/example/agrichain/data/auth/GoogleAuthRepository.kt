package com.example.agrichain.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.agrichain.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthRepository(
    private val context: Context
) {

    private val auth: FirebaseAuth =
        FirebaseAuth.getInstance()

    private val credentialManager: CredentialManager =
        CredentialManager.create(context)

    suspend fun signInWithGoogle(): Result<FirebaseUser> {
        return try {

            // -------------------------------------------------
            // Google ID request
            // -------------------------------------------------

            val googleIdOption =
                GetGoogleIdOption.Builder()
                    .setServerClientId(
                        context.getString(
                            R.string.default_web_client_id
                        ).trim()
                    )
                    .setFilterByAuthorizedAccounts(false)
                    .build()

            // -------------------------------------------------
            // Credential Manager request
            // -------------------------------------------------

            val request =
                GetCredentialRequest.Builder()
                    .addCredentialOption(
                        googleIdOption
                    )
                    .build()

            // -------------------------------------------------
            // Launch Google account picker
            // -------------------------------------------------

            val result =
                credentialManager.getCredential(
                    context = context,
                    request = request
                )

            // -------------------------------------------------
            // Read returned credential
            // -------------------------------------------------

            val credential =
                result.credential

            if (
                credential is CustomCredential &&
                credential.type ==
                GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {

                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(
                        credential.data
                    )

                val idToken =
                    googleIdTokenCredential.idToken

                if (idToken.isBlank()) {
                    return Result.failure(
                        IllegalStateException(
                            "Google returned an empty ID token."
                        )
                    )
                }

                // -------------------------------------------------
                // Firebase Google credential
                // -------------------------------------------------

                val firebaseCredential =
                    GoogleAuthProvider.getCredential(
                        idToken,
                        null
                    )

                // -------------------------------------------------
                // Firebase authentication
                // -------------------------------------------------

                val authResult =
                    auth.signInWithCredential(
                        firebaseCredential
                    ).await()

                val user =
                    authResult.user
                        ?: return Result.failure(
                            IllegalStateException(
                                "Google sign-in succeeded but no Firebase user was returned."
                            )
                        )

                return Result.success(user)
            }

            Result.failure(
                IllegalStateException(
                    "The selected credential was not a Google ID credential."
                )
            )

        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut() {
        auth.signOut()
    }
}