package hu.bme.aut.android.movies

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.bme.aut.android.movies.data.auth.AuthService
import hu.bme.aut.android.movies.data.auth.FirebaseAuthService
import hu.bme.aut.android.movies.data.movies.MovieService
import hu.bme.aut.android.movies.data.movies.firebase.FirebaseMovieService

class MovieApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        authService = FirebaseAuthService(FirebaseAuth.getInstance())
        movieService = FirebaseMovieService(FirebaseFirestore.getInstance(), authService)
    }

    companion object{
        lateinit var authService: AuthService
        lateinit var movieService: MovieService
    }
}