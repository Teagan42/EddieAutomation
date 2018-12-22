package state.sideeffects

import state.machine.StateMachine
import state.machine.base.SideEffect

class LogSideEffect : SideEffect {
    override fun invoke(thing: StateMachine<*, *, *, *>,
                        transition: StateMachine.Transition<*, *, *, *>
    ) {

    }
}