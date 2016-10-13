package mu.node.echod.services

import java.util.UUID

import io.grpc.Status
import mu.node.echo.{EchoServiceGrpc, Message, SendMessageRequest}
import mu.node.echod.grpc.UserContextServerInterceptor

import scala.concurrent.Future

class EchoService extends EchoServiceGrpc.EchoService {

  override def send(request: SendMessageRequest): Future[Message] = {
    Option(UserContextServerInterceptor.userContextKey.get) match {
      case Some(userContext) =>
        Future.successful(Message(UUID.randomUUID().toString, userContext.userId, request.content))
      case None => Future.failed(Status.UNAUTHENTICATED.asException())
    }
  }
}
