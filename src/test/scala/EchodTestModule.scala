package mu.node.echod

import com.typesafe.config.ConfigFactory
import mu.node.echod.grpc.{EchoClient, EchoServer}
import mu.node.echod.util.FileUtils

/*
 * Test dependencies
 */
trait EchodTestModule extends EchodModule with FileUtils {
  override lazy val config = ConfigFactory.load("test")
  override lazy val jwtVerificationKey = loadPublicKey(
    pathForTestResourcePath(config.getString("jwt.signature-verification-key")))
  override lazy val echoServer =
    EchoServer.build(config, echoService, userContextServerInterceptor, fileForTestResourcePath)
  lazy val echoServiceStub = EchoClient.buildServiceStub(config, fileForTestResourcePath)
}
