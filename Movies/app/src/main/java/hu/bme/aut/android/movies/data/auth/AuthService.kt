package hu.bme.aut.android.movies.data.auth

import hu.bme.aut.android.movies.domain.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface AuthService {
    val currentUserId: String?

    val hasUser: Boolean

    val currentUser: Flow<User?>

    suspend fun signUp(
        email: String, password: String,
    )

    suspend fun authenticate(
        email: String,
        password: String
    )

    suspend fun sendRecoveryEmail(email: String)

    suspend fun deleteAccount()

    suspend fun signOut()
}