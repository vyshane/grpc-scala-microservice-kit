package mu.node.echod

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}

class BaseSpec extends WordSpec with EchodTestModule with Matchers with ScalaFutures with BeforeAndAfterAll {
  implicit override val patienceConfig = PatienceConfig(timeout = Span(5, Seconds), interval = Span(50, Millis))
}

