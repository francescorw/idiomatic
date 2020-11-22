package ai.frw.idiomatic.utils

object Metaphone {
  val metaphone = new org.apache.commons.codec.language.Metaphone
  metaphone setMaxCodeLen 6

  def encode(str:String) : String = {
    metaphone encode str
  }
}

