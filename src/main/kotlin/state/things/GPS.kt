package state.things

import state.Thing
import platform.Platform
import state.machine.StateMachine
import state.machine.base.ThingEvent
import state.machine.base.ThingState
import state.machine.base.TransitionHandler

data class Location(
        val name: String? = null,
        val latitude: Float,
        val longitude: Float
)

data class GPSState(override val value: Location) :
    ThingState<Location>

data class GPSUpdated(val value: Location) : ThingEvent

class GPSThing(
        id: String,
        name: String,
        platform: Platform,
        initialGraph: StateMachine.Graph<GPSState, GPSUpdated, TransitionHandler, Location>? = null,
        initialState: GPSState = GPSState(
                Location(null,
                         0.0f,
                         0.0f
                )
        ),
        transitionHandler: TransitionHandler,
        children: MutableList<Thing<*, *, *, *>> = mutableListOf()
) : Thing<GPSState, GPSUpdated, TransitionHandler, Location>(
        id,
        name,
        platform,
        GraphBuilder(initialGraph)
            .apply {
                state<GPSState> {
                    on<GPSUpdated> {
                        transitionTo(GPSState(it.value))
                    }
                }
                onTransition(transitionHandler)
            }
            .build(),
        initialState,
        children
)