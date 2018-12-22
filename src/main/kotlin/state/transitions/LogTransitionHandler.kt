package state.transitions

import state.machine.StateMachine
import state.machine.base.TransitionHandler

class LogTransitionHandler : TransitionHandler {
    override fun invoke(thing: StateMachine<*, *, *, *>,
                        transition: StateMachine.Transition<*, *, *, *>
    ) {

    }
}