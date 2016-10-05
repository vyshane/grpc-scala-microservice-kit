package mu.node.echod.models

import play.api.libs.json.Json

case class UserContext(userId: String)

object UserContext {
  def fromJwt(jwtPayload: String): Option[UserContext] = {
    val json = Json.parse(jwtPayload)
    (json \ "sub").asOpt[String].map(UserContext(_))
  }
}

