package platform

import common.models.AuthenticationResult
import common.models.Credentials

interface AuthenticatedPlatform<CREDENTIALS : Credentials> : Platform {
    val credentials: CREDENTIALS
    suspend fun authenticate(): AuthenticationResult
}