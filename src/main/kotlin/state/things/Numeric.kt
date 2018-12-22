package state.things

import state.Thing
import state.data.Platform
import state.machine.StateMachine
import state.machine.base.DeviceEvent
import state.machine.base.DeviceState
import state.machine.base.TransitionHandler

data class NumericState<NumberType>(override val value: NumberType) :
    DeviceState<NumberType> where NumberType : Number, NumberType : Comparable<NumberType>

data class SetNumericValue<NumberType>(val value: NumberType) :
    DeviceEvent where NumberType : Number, NumberType : Comparable<NumberType>

class NumericThing<NumberType>(
        id: String,
        name: String,
        platform: Platform,
        initialGraph: StateMachine.Graph<NumericState<NumberType>, SetNumericValue<NumberType>, TransitionHandler, NumberType>? = null,
        initialState: NumericState<NumberType>,
        transitionHandler: TransitionHandler,
        children: MutableList<Thing<*, *, *, *>> = mutableListOf()
) : Thing<NumericState<NumberType>, SetNumericValue<NumberType>, TransitionHandler, NumberType>(
        id,
        name,
        platform,
        GraphBuilder(initialGraph)
            .apply {
                state<NumericState<NumberType>> {
                    on<SetNumericValue<NumberType>> {
                        transitionTo(NumericState(it.value))
                    }
                }
                onTransition(transitionHandler)
            }
            .build(),
        initialState,
        children
) where NumberType : Number, NumberType : Comparable<NumberType>