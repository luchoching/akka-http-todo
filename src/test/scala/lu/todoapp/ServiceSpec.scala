package lu.todoapp

import org.scalatest.{WordSpec, MustMatchers}

class ServiceSpec extends WordSpec with MustMatchers {
  "A set" when {
    "empty" must {
      "has size 0" in {
        Set.empty.size mustBe 0
      }
    }
  }
}