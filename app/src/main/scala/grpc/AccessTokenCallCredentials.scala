package mu.node.echod.grpc

import java.util.concurrent.Executor

import io.grpc.{Attributes, CallCredentials, Metadata, MethodDescriptor}

class AccessTokenCallCredentials(accessToken: String) extends CallCredentials {

  override def applyRequestMetadata(method: MethodDescriptor[_, _],
                                    attributes: Attributes,
                                    appExecutor: Executor,
                                    applier: CallCredentials.MetadataApplier): Unit = {
    appExecutor.execute(new Runnable {
      override def run(): Unit = {
        val headers                = new Metadata()
        val authorizationHeaderKey = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)
        headers.put(authorizationHeaderKey, "Bearer " + accessToken)
        applier.apply(headers)
      }
    })
  }
}
