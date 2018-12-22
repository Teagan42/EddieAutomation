package state.things

import state.Thing
import state.data.Platform
import state.machine.StateMachine
import state.machine.base.DeviceEvent
import state.machine.base.DeviceState
import state.machine.base.SideEffect

data class NumericState<NumberType>(override val value: NumberType) :
    DeviceState<NumberType> where NumberType : Number, NumberType : Comparable<NumberType>

data class SetNumericValue<NumberType>(val value: NumberType) :
    DeviceEvent where NumberType : Number, NumberType : Comparable<NumberType>

class NumericThing<NumberType>(
        id: String,
        name: String,
        platform: Platform,
        initialGraph: StateMachine.Graph<NumericState<NumberType>, SetNumericValue<NumberType>, SideEffect, NumberType>? = null,
        initialState: NumericState<NumberType>,
        sideEffect: SideEffect,
        children: MutableList<Thing<*, *, *, *>> = mutableListOf()
) : Thing<NumericState<NumberType>, SetNumericValue<NumberType>, SideEffect, NumberType>(
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
                onTransition(sideEffect)
            }
            .build(),
        initialState,
        children
) where NumberType : Number, NumberType : Comparable<NumberType>