package ai.frw.idiomatic.test.utils

import ai.frw.idiomatic.test.UnitSpec
import ai.frw.idiomatic.utils.Metaphone

class MetaphoneSpec extends UnitSpec {
    test("should encode the word banana") {
        assert(Metaphone.encode("banana") == "BNN")
    }
}
