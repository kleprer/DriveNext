@file:Suppress("DEPRECATION")

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

data class GoogleUserModel(
    val id: String,
    val email: String,
    val name: String,
    val givenName: String,
    val familyName: String,
    val photoUrl: String?,
    val idToken: String?
) {
    @Suppress("DEPRECATION")
    companion object {
        fun fromGoogleAccount(account: GoogleSignInAccount): GoogleUserModel {
            return GoogleUserModel(
                id = account.id ?: "",
                email = account.email ?: "",
                name = account.displayName ?: "",
                givenName = account.givenName ?: "",
                familyName = account.familyName ?: "",
                photoUrl = account.photoUrl?.toString(),
                idToken = account.idToken
            )
        }
        private fun generateFallbackId(account: GoogleSignInAccount): String {
            return "google_${account.email?.hashCode() ?: System.currentTimeMillis()}"
        }
    }
}