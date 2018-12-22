package state.data

import state.data.models.AuthenticationResult
import state.data.models.Credentials

interface AuthenticatedPlatform<CREDENTIALS : Credentials> : Platform {
    suspend fun authenticate(credentials: CREDENTIALS): AuthenticationResult
}