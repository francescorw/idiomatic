package ai.frw.idiomatic.test.core.flows.activities

import ai.frw.idiomatic.test.UnitSpec
import ai.frw.idiomatic.core.flows.activities.WriteTextActivity

class WriteTextActivitySpec extends ActivitySpecBase {
    test("it should write a message") {
        val textToWrite = "sent text"
        val activity = new WriteTextActivity(context, () => textToWrite);
        activity.execute()

        assert(readFromStdOutput() == textToWrite)
    }
}