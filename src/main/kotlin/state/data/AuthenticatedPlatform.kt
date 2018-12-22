package state.data

import state.data.models.AuthenticationResult
import state.data.models.Credentials

interface AuthenticatedPlatform : Platform {
    fun authenticate(credentials: Credentials): AuthenticationResult
}