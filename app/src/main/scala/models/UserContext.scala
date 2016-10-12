package mu.node.echod.models

import java.security.{PrivateKey, PublicKey}

import mu.node.echod.util.KeyUtils
import pdi.jwt.Jwt
import play.api.libs.json.Json

import scala.util.Try

case class UserContext(userId: String) extends KeyUtils {
  def toJwt(jwtSigningKey: PrivateKey): String = {
    Jwt.encode(s"""{ "sub": "$userId" }""", jwtSigningKey, jwtDsa)
  }
}

object UserContext extends KeyUtils {
  def fromJwt(jwt: String, jwtVerificationKey: PublicKey): Option[UserContext] = {
    Jwt.decode(jwt, jwtVerificationKey, Seq(jwtDsa))
      .flatMap(payload => Try(Json.parse(payload)))
      .toOption
      .flatMap(json => (json \ "sub").asOpt[String].map(UserContext(_)))
  }
}

