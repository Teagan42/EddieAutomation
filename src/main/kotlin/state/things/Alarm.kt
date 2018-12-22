package state.things

import state.Thing
import data.platform.Platform
import state.machine.StateMachine
import state.machine.base.DeviceEvent
import state.machine.base.DeviceState
import state.machine.base.TransitionHandler

enum class Alarm {
    DISARMED,
    ARMED_HOME,
    ARMED_AWAY,
    TRIGGERED
}

sealed class AlarmState(override val value: Alarm) : DeviceState<Alarm> {
    object Disarmed : AlarmState(Alarm.DISARMED)
    object ArmedHome : AlarmState(Alarm.ARMED_HOME)
    object ArmedAway : AlarmState(Alarm.ARMED_AWAY)
    object Triggered : AlarmState(Alarm.TRIGGERED)
}

sealed class AlarmEvent : DeviceEvent {
    object Disarm : AlarmEvent()
    data class Arm(val homeMode: Boolean) : AlarmEvent()
    object Trigger : AlarmEvent()
}

class AlarmThing(
        id: String,
        name: String,
        platform: Platform,
        initialGraph: StateMachine.Graph<AlarmState, AlarmEvent, TransitionHandler, Alarm>? = null,
        initialState: AlarmState = AlarmState.Disarmed,
        transitionHandler: TransitionHandler,
        children: MutableList<Thing<*, *, *, *>> = mutableListOf()
) : Thing<AlarmState, AlarmEvent, TransitionHandler, Alarm>(
        id,
        name,
        platform,
        GraphBuilder(initialGraph)
            .apply {
                state<AlarmState.Disarmed> {
                    on<AlarmEvent.Arm> {
                        transitionTo(
                                when (it.homeMode) {
                                    true -> AlarmState.ArmedHome
                                    false -> AlarmState.ArmedAway
                                }
                        )
                    }
                }
                state<AlarmState.Triggered> {
                    on<AlarmEvent.Disarm> {
                        transitionTo(AlarmState.Disarmed)
                    }
                }
                state<AlarmState.ArmedHome> {
                    on<AlarmEvent.Trigger> {
                        transitionTo(AlarmState.Triggered)
                    }
                    on<AlarmEvent.Disarm> {
                        transitionTo(AlarmState.Disarmed)
                    }
                    on<AlarmEvent.Arm> {
                        transitionTo(when (it.homeMode) {
                                         true -> AlarmState.ArmedHome
                                         false -> AlarmState.ArmedAway
                                     }
                        )
                    }
                }
                state<AlarmState.ArmedAway> {
                    on<AlarmEvent.Trigger> {
                        transitionTo(AlarmState.Triggered)
                    }
                    on<AlarmEvent.Disarm> {
                        transitionTo(AlarmState.Disarmed)
                    }
                    on<AlarmEvent.Arm> {
                        transitionTo(when (it.homeMode) {
                                         true -> AlarmState.ArmedHome
                                         false -> AlarmState.ArmedAway
                                     }
                        )
                    }
                }
                onTransition(transitionHandler)
            }.build(),
        initialState,
        children
)