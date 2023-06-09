package hu.bme.aut.android.movies.domain.usecases

class PasswordsMatchUseCase {

    operator fun invoke(password: String, confirmPassword: String): Boolean =
        password == confirmPassword
}