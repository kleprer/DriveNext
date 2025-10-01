import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val birthDate: String,
    val gender: String,

    val driverLicense: String? = null,
    val licenseIssueDate: String? = null,
    val profileImagePath: String? = null,
    val licenseImagePath: String? = null,
    val passportImagePath: String? = null,

    val accessToken: String? = null,
    val refreshToken: String? = null,
    val tokenExpiry: Long? = null,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)