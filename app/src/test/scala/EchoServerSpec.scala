package mu.node.echod

import mu.node.echo.SendMessageRequest
import grpc.{AccessTokenCallCredentials, EchoClient}
import mu.node.echod.models.UserContext
import org.scalatest.BeforeAndAfterAll

class EchoServerSpec extends BaseSpec with BeforeAndAfterAll {

  val jwtSigningKey   = loadPkcs8PrivateKey(pathForTestResourcePath(config.getString("jwt.signing-key")))
  val echoServiceStub = EchoClient.buildServiceStub(config, fileForTestResourcePath)

  override def beforeAll(): Unit = {
    echoServer.start()
  }

  override def afterAll(): Unit = {
    echoServer.shutdown()
  }

  "The echod gRPC server" when {

    "sent a valid, authenticated SendMessageRequest" should {
      "reply back with the Message" in {
        val userId = "8d5921be-8f85-11e6-ae22-56b6b6499611"
        val jwt    = UserContext(userId).toJwt(jwtSigningKey)
        val sendMessage =
          echoServiceStub.withCallCredentials(new AccessTokenCallCredentials(jwt)).send(SendMessageRequest("hello"))
        whenReady(sendMessage) { reply =>
          reply.messageId.nonEmpty shouldBe true
          reply.senderId shouldEqual userId
          reply.content shouldEqual "hello"
        }
      }
    }

    "sent an unauthenticated SendMessageRequest" should {
      "return an exception indicating that the call was unauthenticated" in {
        val sendMessage = echoServiceStub.send(SendMessageRequest("test"))
        whenReady(sendMessage.failed) { ex =>
          ex shouldBe a[Exception]
          ex.getMessage shouldEqual "UNAUTHENTICATED"
        }
      }
    }

    "sent an SendMessageRequest with an invalid access token" should {
      "return an exception indicating that the call was unauthenticated" in {
        val sendMessage = echoServiceStub
          .withCallCredentials(new AccessTokenCallCredentials("bad jwt"))
          .send(SendMessageRequest("test"))
        whenReady(sendMessage.failed) { ex =>
          ex shouldBe a[Exception]
          ex.getMessage shouldEqual "UNAUTHENTICATED"
        }
      }
    }
  }
}
