package mu.node.echod.grpc

import java.security.PublicKey

import com.google.common.collect.Iterables
import io.grpc._
import mu.node.echod.models.UserContext
import pdi.jwt.Jwt

import scala.util.{Failure, Success}

/*
 * Obtain the user context by reading from the JSON Web Token that is sent as an OAuth bearer
 * token with the HTTP request header.
 */
class UserContextServerInterceptor(jwtVerificationKey: PublicKey) extends ServerInterceptor {

  override def interceptCall[ReqT, RespT](call: ServerCall[ReqT, RespT],
                                          headers: Metadata,
                                          next: ServerCallHandler[ReqT, RespT]): ServerCall.Listener[ReqT] = {
    val bearerToken = readBearerToken(headers)
    if (bearerToken.isDefined) {
      Jwt.decode(bearerToken.get, jwtVerificationKey) match {
        case Success(jwtPayload) => {
          val withUserContext =
            Context.current().withValue(UserContextServerInterceptor.userContextKey, UserContext.fromJwt(jwtPayload))
          Contexts.interceptCall(withUserContext, call, headers, next)
        }
        case Failure(e) => call.close(Status.UNAUTHENTICATED, headers)
      }
    }
    next.startCall(call, headers)
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
  val userContextKey: Context.Key[Option[UserContext]] = Context.key("user_context")
}

