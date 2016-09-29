package mu.node.echod

import org.scalatest.BeforeAndAfterAll

class EchoServerSpec extends BaseSpec with BeforeAndAfterAll {

  override def beforeAll(): Unit = {
    echoServer.start()
  }

  override def afterAll(): Unit = {
    echoServer.shutdown()
  }

  // TODO
}

