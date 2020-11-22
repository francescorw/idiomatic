package ai.frw.idiomatic.test.core.flows.activities

import ai.frw.idiomatic.core.flows.FlowBase
import ai.frw.idiomatic.core.flows.ActivityBase
import ai.frw.idiomatic.core.flows.activities.WriteTextActivity
import ai.frw.idiomatic.core.flows.PauseFlowActivity
import ai.frw.idiomatic.core.flows.FlowContext

class FlowSpec extends ActivitySpecBase {
    object Strings {
        val firstString = "first string"
        val secondString = "second string"
        val thirdString = "third string"
    }

    class FlowTest(override val context: FlowContext[Unit]) extends FlowBase(context) {
        setActivities(Seq[ActivityBase[Unit]](
            new WriteTextActivity(context, () => Strings.firstString),
            new PauseFlowActivity(context),
            new WriteTextActivity(context, () => Strings.secondString)
        ))
    }

    class FlowTest2(override val context: FlowContext[Unit]) extends FlowBase(context) {
        setActivities(Seq[ActivityBase[Unit]](
            new WriteTextActivity(context, () => Strings.firstString),
            new WriteTextActivity(context, () => Strings.secondString),
            new PauseFlowActivity(context),
            new WriteTextActivity(context, () => Strings.thirdString)
        ))
    }

    test("it should read firstString") {
        var flowTest = new FlowTest(context)

        flowTest.nextStep

        assert(readFromStdOutput() == Strings.firstString)
    }

    test("it should read firstString and then secondString") {
        var flowTest = new FlowTest(context)

        flowTest.nextStep

        assert(readFromStdOutput() == Strings.firstString)

        flowTest.nextStep

        assert(readFromStdOutput() == Strings.secondString)
    }

    test("it should read secondString and then thirdString") {
        var flowTest = new FlowTest2(context)

        flowTest.nextStep

        assert(readFromStdOutput() == Strings.secondString)

        flowTest.nextStep

        assert(readFromStdOutput() == Strings.thirdString)
    }
}
