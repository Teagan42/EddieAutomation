package state.devices

import state.Device
import state.data.Platform
import state.machine.StateMachine
import state.machine.base.DeviceEvent
import state.machine.base.DeviceState
import state.machine.base.SideEffect

enum class MediaType {
    UNKNOWN,
    MOVIE,
    TV,
    MUSIC
}

data class StateValue(
        val title: String? = null,
        val imageUrl: String? = null,
        val type: MediaType
)

sealed class MediaState(override val value: StateValue) :
    DeviceState<StateValue> {
    class Playing(value: StateValue) : MediaState(value)
    class Paused(value: StateValue) : MediaState(value)
    object Stopped : MediaState(StateValue(type = MediaType.UNKNOWN))
}

sealed class MediaEvent(val value: StateValue) : DeviceEvent {
    class Play(value: StateValue) : MediaEvent(value)
    class Pause(value: StateValue) : MediaEvent(value)
    object Stop : MediaEvent(StateValue(type = MediaType.UNKNOWN))
}

class MediaDevice(
        id: String,
        name: String,
        platform: Platform,
        initialGraph: StateMachine.Graph<MediaState, MediaEvent, SideEffect, StateValue>? = null,
        initialState: MediaState = MediaState.Stopped,
        sideEffect: SideEffect
) : Device<MediaState, MediaEvent, SideEffect, StateValue>(
        id,
        name,
        platform,
        GraphBuilder(initialGraph)
            .apply {
                state<MediaState.Playing> {
                    on<MediaEvent.Play> {
                        transitionTo(MediaState.Playing(it.value),
                                     sideEffect
                        )
                    }
                    on<MediaEvent.Pause> {
                        transitionTo(MediaState.Paused(it.value),
                                     sideEffect
                        )
                    }
                    on<MediaEvent.Stop> {
                        transitionTo(MediaState.Stopped,
                                     sideEffect
                        )
                    }
                }
                state<MediaState.Paused> {
                    on<MediaEvent.Play> {
                        transitionTo(MediaState.Playing(it.value),
                                     sideEffect
                        )
                    }
                    on<MediaEvent.Pause> {
                        transitionTo(MediaState.Paused(it.value),
                                     sideEffect
                        )
                    }
                    on<MediaEvent.Stop> {
                        transitionTo(MediaState.Stopped,
                                     sideEffect
                        )
                    }
                }
                state<MediaState.Stopped> {
                    on<MediaEvent.Play> {
                        transitionTo(MediaState.Playing(it.value),
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