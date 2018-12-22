package platform

import platform.models.AuthenticationResult
import platform.models.Credentials

interface AuthenticatedPlatform<CREDENTIALS : Credentials> : Platform {
    suspend fun authenticate(credentials: CREDENTIALS): AuthenticationResult
}