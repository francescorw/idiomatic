package ai.frw.idiomatic.core.flows

class ActivityGroup[T](override val context: FlowContext[T], val activities: Seq[ActivityBase[T]]) extends ActivityBase(context) {
    val flowBase: FlowBase[T] = new FlowBase[T](context)
    flowBase.setActivities(activities)

    def execute(): Boolean = {
        flowBase.nextStep
    }
}