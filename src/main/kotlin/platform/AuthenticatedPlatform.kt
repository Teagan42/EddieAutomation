package platform

import platform.models.AuthenticationResult
import platform.models.Credentials

interface AuthenticatedPlatform<CREDENTIALS : Credentials> : Platform {
    val credentials: CREDENTIALS
    suspend fun authenticate(): AuthenticationResult
}