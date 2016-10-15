package mu.node.echod.models

import java.security.{PrivateKey, PublicKey}
import java.util.Calendar

import mu.node.echod.util.KeyUtils
import pdi.jwt.Jwt
import play.api.libs.json.Json

import scala.util.Try

case class UserContext(userId: String) extends KeyUtils {

  def toJwt(expiryMillis: Long, jwtSigningKey: PrivateKey): String = {
    val json =
      s"""{
         |  "sub": "$userId",
         |  "exp": $expiryMillis
         |}
         |""".stripMargin
    Jwt.encode(json, jwtSigningKey, jwtDsa)
  }
}

object UserContext extends KeyUtils {
  def fromJwt(jwt: String, jwtVerificationKey: PublicKey): Option[UserContext] = {
    Jwt
      .decode(jwt, jwtVerificationKey, Seq(jwtDsa))
      .flatMap(payload => Try(Json.parse(payload)))
      .toOption
      .filter(json => (json \ "exp").asOpt[Long].exists(notExpired))
      .flatMap(json => (json \ "sub").asOpt[String].map(UserContext(_)))
  }

  private def notExpired(expiryMillis: Long): Boolean =
    expiryMillis > Calendar.getInstance().getTimeInMillis
}
