package state.things

import state.Thing
import data.platform.Platform
import state.machine.StateMachine
import state.machine.base.ThingEvent
import state.machine.base.ThingState
import state.machine.base.TransitionHandler

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
    ThingState<StateValue> {
    class Playing(value: StateValue) : MediaState(value)
    class Paused(value: StateValue) : MediaState(value)
    object Stopped : MediaState(StateValue(type = MediaType.UNKNOWN))
}

sealed class MediaEvent(val value: StateValue) : ThingEvent {
    class Play(value: StateValue) : MediaEvent(value)
    class Pause(value: StateValue) : MediaEvent(value)
    object Stop : MediaEvent(StateValue(type = MediaType.UNKNOWN))
}

class MediaThing(
        id: String,
        name: String,
        platform: Platform,
        initialGraph: StateMachine.Graph<MediaState, MediaEvent, TransitionHandler, StateValue>? = null,
        initialState: MediaState = MediaState.Stopped,
        transitionHandler: TransitionHandler,
        children: MutableList<Thing<*, *, *, *>> = mutableListOf()
) : Thing<MediaState, MediaEvent, TransitionHandler, StateValue>(
        id,
        name,
        platform,
        GraphBuilder(initialGraph)
            .apply {
                state<MediaState.Playing> {
                    on<MediaEvent.Play> {
                        transitionTo(MediaState.Playing(it.value))
                    }
                    on<MediaEvent.Pause> {
                        transitionTo(MediaState.Paused(it.value))
                    }
                    on<MediaEvent.Stop> {
                        transitionTo(MediaState.Stopped)
                    }
                }
                state<MediaState.Paused> {
                    on<MediaEvent.Play> {
                        transitionTo(MediaState.Playing(it.value))
                    }
                    on<MediaEvent.Pause> {
                        transitionTo(MediaState.Paused(it.value))
                    }
                    on<MediaEvent.Stop> {
                        transitionTo(MediaState.Stopped)
                    }
                }
                state<MediaState.Stopped> {
                    on<MediaEvent.Play> {
                        transitionTo(MediaState.Playing(it.value))
                    }
                }
                onTransition(transitionHandler)
            }
            .build(),
        initialState,
        children
)