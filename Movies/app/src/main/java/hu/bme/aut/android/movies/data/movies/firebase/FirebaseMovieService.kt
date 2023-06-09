package hu.bme.aut.android.movies.data.movies.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.firestore.ktx.toObject
import hu.bme.aut.android.movies.data.auth.AuthService
import hu.bme.aut.android.movies.data.movies.MovieService
import hu.bme.aut.android.movies.domain.model.Movie
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class FirebaseMovieService(
    private val fireStore: FirebaseFirestore,
    private val authService: AuthService
) : MovieService {

    override val movies: Flow<List<Movie>> = authService.currentUser.flatMapLatest { user ->
        if (user == null) flow { emit(emptyList()) }
        else currentCollection(user.id)
            .snapshots()
            .map { snapshot ->
                snapshot
                    .toObjects<FirebaseMovie>()
                    .map {
                        it.asMovie()
                    }
            }
    }

    override suspend fun getMovie(id: String): Movie? =
        authService.currentUserId?.let {
            currentCollection(it).document(id).get().await().toObject<FirebaseMovie>()?.asMovie()
        }

    override suspend fun saveMovie(movie: Movie) {
        authService.currentUserId?.let {
            currentCollection(it).add(movie.asFireBaseMovie()).await()
        }
    }

    override suspend fun updateMovie(movie: Movie) {
        authService.currentUserId?.let {
            currentCollection(it).document(movie.id).set(movie.asFireBaseMovie()).await()
        }
    }

    override suspend fun deleteMovie(id: String) {
        authService.currentUserId?.let {
            currentCollection(it).document(id).delete().await()
        }
    }

    private fun currentCollection(userId: String) =
        fireStore.collection(USER_COLLECTION).document(userId).collection(MOVIE_COLLECTION)

    companion object {
        private const val USER_COLLECTION = "users"
        private const val MOVIE_COLLECTION = "movies"
    }
}