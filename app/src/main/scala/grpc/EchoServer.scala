package mu.node.echod.grpc

import java.io.File

import com.typesafe.config.Config
import io.grpc.ServerInterceptors
import io.grpc.internal.ServerImpl
import io.grpc.netty.{GrpcSslContexts, NettyServerBuilder}
import io.netty.handler.ssl.ClientAuth
import mu.node.echo.EchoServiceGrpc
import mu.node.echo.EchoServiceGrpc.EchoService
import mu.node.echod.util.FileUtils

import scala.concurrent.ExecutionContext

object EchoServer extends FileUtils {

  def build(config: Config,
            echoService: EchoService,
            userContextServerInterceptor: UserContextServerInterceptor,
            fileForConfiguredPath: (String) => File = fileForAbsolutePath): ServerImpl = {

    val sslContext = GrpcSslContexts
      .forServer(fileForConfiguredPath(config.getString("ssl.server-certificate")),
                 fileForConfiguredPath(config.getString("ssl.server-private-key")))
      .trustManager(fileForConfiguredPath(config.getString("ssl.client-ca-certificate")))
      .clientAuth(ClientAuth.REQUIRE)
      .build()

    val echoGrpcService = EchoServiceGrpc.bindService(echoService, ExecutionContext.global)

    NettyServerBuilder
      .forPort(config.getInt("server-port"))
      .sslContext(sslContext)
      .addService(ServerInterceptors.intercept(echoGrpcService, userContextServerInterceptor))
      .build()
  }
}
