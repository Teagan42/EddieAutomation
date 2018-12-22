package state.things

import state.Thing
import state.data.Platform
import state.machine.StateMachine
import state.machine.base.DeviceEvent
import state.machine.base.DeviceState
import state.machine.base.SideEffect

data class Location(
        val name: String? = null,
        val latitude: Float,
        val longitude: Float
)

data class GPSState(override val value: Location) :
    DeviceState<Location>

data class GPSUpdated(val value: Location) : DeviceEvent

class GPSThing(
        id: String,
        name: String,
        platform: Platform,
        initialGraph: StateMachine.Graph<GPSState, GPSUpdated, SideEffect, Location>? = null,
        initialState: GPSState = GPSState(
                Location(null,
                         0.0f,
                         0.0f
                )
        ),
        sideEffect: SideEffect
) : Thing<GPSState, GPSUpdated, SideEffect, Location>(
        id,
        name,
        platform,
        GraphBuilder(initialGraph)
            .apply {
                state<GPSState> {
                    on<GPSUpdated> {
                        transitionTo(GPSState(it.value),
                                     sideEffect
                        )
                    }
                }
                onTransition {
                    TODO("Handle Transition")
                }
            }
            .build(),
        initialState
)