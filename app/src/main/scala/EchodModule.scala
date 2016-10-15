package mu.node.echod

import com.typesafe.config.ConfigFactory
import mu.node.echod.util.KeyUtils
import mu.node.echod.grpc.{EchoServer, UserContextServerInterceptor}
import mu.node.echod.services.EchoService

/*
 * Application dependencies
 */
trait EchodModule extends KeyUtils {
  lazy val config = ConfigFactory.load()
  lazy val echoService = new EchoService
  lazy val jwtVerificationKey = loadX509PublicKey(
    config.getString("jwt.signature-verification-key"))
  lazy val userContextServerInterceptor = new UserContextServerInterceptor(jwtVerificationKey)
  lazy val echoServer = EchoServer.build(config, echoService, userContextServerInterceptor)
}
