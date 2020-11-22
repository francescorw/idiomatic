package ai.frw.idiomatic.core.flows

abstract class ActivityBase[T](val context: FlowContext[T]) {
  def execute(): Boolean
}