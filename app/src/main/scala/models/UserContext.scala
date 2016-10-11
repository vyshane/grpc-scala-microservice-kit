package mu.node.echod.models

import java.security.PublicKey

import mu.node.echod.util.KeyUtils
import pdi.jwt.Jwt
import play.api.libs.json.Json

case class UserContext(userId: String)

object UserContext extends KeyUtils {
  def fromJwt(jwt: String, jwtVerificationKey: PublicKey): Option[UserContext] = {
    Jwt.decode(jwt, jwtVerificationKey, Seq(jwtDsa))
      .toOption
      .flatMap { payload =>
        val json = Json.parse(payload)
        (json \ "sub").asOpt[String].map(UserContext(_))
      }
  }
}

