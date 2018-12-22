package data.platform

import data.platform.models.AuthenticationResult
import data.platform.models.Credentials

interface AuthenticatedPlatform<CREDENTIALS : Credentials> : Platform {
    suspend fun authenticate(credentials: CREDENTIALS): AuthenticationResult
}