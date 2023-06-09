package hu.bme.aut.android.movies.data.auth

import hu.bme.aut.android.movies.domain.model.User
import kotlinx.coroutines.flow.*

class MemoryAuthService : AuthService {
    override val currentUserId: String? get() = currentUserMutableFlow.value?.id
    override val hasUser: Boolean get() = currentUserMutableFlow.value != null
    override val currentUser: Flow<User?> get() = currentUserMutableFlow.asStateFlow()

    private val currentUserMutableFlow = MutableStateFlow<User?>(null)

    override suspend fun signUp(email: String, password: String) {
        currentUserMutableFlow.value = User(email.hashCode().toString())
    }

    override suspend fun authenticate(email: String, password: String) {
        currentUserMutableFlow.value = User(email.hashCode().toString())
    }

    override suspend fun sendRecoveryEmail(email: String) = Unit

    override suspend fun deleteAccount() {
        currentUserMutableFlow.value = null
    }

    override suspend fun signOut() {
        currentUserMutableFlow.value = null
    }
}