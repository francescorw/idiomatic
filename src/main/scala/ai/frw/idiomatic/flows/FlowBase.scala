package ai.frw.idiomatic.core.flows

import com.typesafe.scalalogging.Logger

class FlowBase[T](val context: FlowContext[T]) {
    private var activities: Seq[ActivityBase[T]] = Seq[ActivityBase[T]]()
    var nextStepIndex = 0

    def setActivities(activities: Seq[ActivityBase[T]]) = {
        this.activities = activities
    }

    def nextStep: Boolean = {
        var moveForward = true;
        while (moveForward && hasMoreActivities) {
            val currentActivity = activities(nextStepIndex)
            val success = currentActivity.execute()

            if (success) {
                nextStepIndex += 1
            }
            else {
                moveForward = false
            }

            if (currentActivity.isInstanceOf[PauseFlowActivity[T]]) {
                moveForward = false
            }
        }

        hasMoreActivities
    }

    private def hasMoreActivities: Boolean = {
        nextStepIndex < activities.length
    }
}
