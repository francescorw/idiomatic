package ai.frw.idiomatic.core.flows

class PauseFlowActivity[T](override val context: FlowContext[T]) extends ActivityBase(context) {
    def execute(): Boolean = true
}
