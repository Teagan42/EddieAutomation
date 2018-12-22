package state.machine.base

import state.machine.StateMachine

interface TransitionHandler : (StateMachine<*, *, *, *>, StateMachine.Transition<*, *, *, *>) -> Unit