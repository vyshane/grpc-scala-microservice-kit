package mu.node.echod.grpc

import java.security.PublicKey

import com.google.common.collect.Iterables
import io.grpc._
import mu.node.echod.models.UserContext

/*
 * Obtain the user context by reading from the JSON Web Token that is sent as an OAuth bearer
 * token with the HTTP request header.
 */
class UserContextServerInterceptor(jwtVerificationKey: PublicKey) extends ServerInterceptor {

  override def interceptCall[ReqT, RespT](
      call: ServerCall[ReqT, RespT],
      headers: Metadata,
      next: ServerCallHandler[ReqT, RespT]): ServerCall.Listener[ReqT] = {
    readBearerToken(headers) flatMap { token =>
      UserContext.fromJwt(token, jwtVerificationKey)
    } map { userContext =>
      val withUserContext = Context
        .current()
        .withValue[UserContext](UserContextServerInterceptor.userContextKey, userContext)
      Contexts.interceptCall(withUserContext, call, headers, next)
    } getOrElse {
      next.startCall(call, headers)
    }
  }

  private def readBearerToken(headers: Metadata): Option[String] = {
    val authorizationHeaderKey = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)
    try {
      Iterables
        .toArray(headers.getAll(authorizationHeaderKey), classOf[String])
        .find(header => header.startsWith("Bearer "))
        .map(header => header.replaceFirst("Bearer ", ""))
    } catch {
      case _: Exception => Option.empty
    }
  }
}

object UserContextServerInterceptor {
  val userContextKey: Context.Key[UserContext] = Context.key("user_context")
}
