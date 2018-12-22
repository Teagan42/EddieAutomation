package state.things

import state.Thing
import data.platform.Platform
import state.machine.StateMachine
import state.machine.base.ThingEvent
import state.machine.base.ThingState
import state.machine.base.TransitionHandler

sealed class BinaryState(override val value: Boolean) : ThingState<Boolean> {
    object On : BinaryState(true)
    object Off : BinaryState(false)
}

sealed class BinaryEvent : ThingEvent {
    object TurnOn : BinaryEvent()
    object TurnOff : BinaryEvent()
    object Toggle : BinaryEvent()
}

class BinaryThing(
        id: String,
        name: String,
        platform: Platform,
        initialGraph: StateMachine.Graph<BinaryState, BinaryEvent, TransitionHandler, Boolean>? = null,
        initialState: BinaryState = BinaryState.Off,
        transitionHandler: TransitionHandler,
        children: MutableList<Thing<*, *, *, *>> = mutableListOf()
) : Thing<BinaryState, BinaryEvent, TransitionHandler, Boolean>(
        id,
        name,
        platform,
        GraphBuilder(initialGraph)
            .apply {
                state<BinaryState.Off> {
                    on<BinaryEvent.TurnOn> {
                        transitionTo(BinaryState.On)
                    }
                    on<BinaryEvent.Toggle> {
                        transitionTo(BinaryState.On)
                    }
                }
                state<BinaryState.On> {
                    on<BinaryEvent.TurnOff> {
                        transitionTo(BinaryState.Off)
                    }
                    on<BinaryEvent.Toggle> {
                        transitionTo(BinaryState.Off)
                    }
                }
                onTransition(transitionHandler)
            }
            .build(),
        initialState,
        children
)