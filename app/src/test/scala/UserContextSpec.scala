package mu.node.echod

import java.util.Calendar

import mu.node.echod.models.UserContext
import mu.node.echod.util.KeyUtils
import pdi.jwt.Jwt

import scala.concurrent.duration._

class UserContextSpec extends BaseSpec with KeyUtils {
  val jwtSigningKey = loadPkcs8PrivateKey(
    pathForTestResourcePath(config.getString("jwt.signing-key")))

  "The UserContext companion object" when {

    val userId = "8d5921be-8f85-11e6-ae22-56b6b6499611"
    val futureExpiry = Calendar.getInstance().getTimeInMillis + Duration(5, MINUTES).toMillis
    val validClaim =
      s"""|{
          |  "sub": "$userId",
          |  "exp": $futureExpiry
          |}""".stripMargin

    "asked to create a UserContext from a valid, signed JWT" should {
      "return the UserContext" in {
        val validJwt = Jwt.encode(validClaim, jwtSigningKey, jwtDsa)
        UserContext.fromJwt(validJwt, jwtVerificationKey) shouldEqual Some(UserContext(userId))
      }
    }

    "asked to create UserContext from an unsigned JWT" should {
      "return None" in {
        val unsignedJwt = Jwt.encode(validClaim)
        UserContext.fromJwt(unsignedJwt, jwtVerificationKey) shouldEqual None
      }
    }

    "asked to create UserContext from a JWT with an invalid claim" should {
      "return None" in {
        val invalidClaim = s"""{ "unknownField": "value" }"""
        val invalidJwt = Jwt.encode(invalidClaim, jwtSigningKey, jwtDsa)
        UserContext.fromJwt(invalidJwt, jwtVerificationKey) shouldEqual None
      }
    }

    "asked to create UserContext from a JWT with an invalid payload" should {
      "return None" in {
        val invalidPayload = "malformed JSON"
        val invalidJwt = Jwt.encode(invalidPayload, jwtSigningKey, jwtDsa)
        UserContext.fromJwt(invalidJwt, jwtVerificationKey) shouldEqual None
      }
    }
  }
}
