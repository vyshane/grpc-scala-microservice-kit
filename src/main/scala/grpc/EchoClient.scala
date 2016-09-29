package mu.node.echod.grpc

import java.io.File

import com.typesafe.config.Config
import io.grpc.netty.{GrpcSslContexts, NettyChannelBuilder}
import mu.node.echo.EchoServiceGrpc
import mu.node.echod.util.FileUtils

object EchoClient extends FileUtils {

  def buildServiceStub(
      config: Config,
      fileForConfiguredPath: (String) => File = fileForAbsolutePath): EchoServiceGrpc.EchoServiceStub = {

    val sslContext = GrpcSslContexts
      .forClient()
      .keyManager(fileForConfiguredPath(config.getString("ssl.client-certificate-path")),
                  fileForConfiguredPath(config.getString("ssl.client-private-key-path")))
      .trustManager(fileForConfiguredPath(config.getString("ssl.server-ca-certificate-path")))
      .build()

    val channel =
      NettyChannelBuilder.forAddress("localhost", config.getInt("server-port")).sslContext(sslContext).build()

    EchoServiceGrpc.stub(channel)
  }
}
