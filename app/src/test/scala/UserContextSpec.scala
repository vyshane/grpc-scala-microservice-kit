package mu.node.echod

import mu.node.echod.models.UserContext
import mu.node.echod.util.KeyUtils
import pdi.jwt.Jwt

class UserContextSpec extends BaseSpec with KeyUtils {
  val jwtSigningKey = loadPrivateKey(pathForTestResourcePath(config.getString("jwt.signing-key")))

  "The UserContext companion object" when {

    val userId = "8d5921be-8f85-11e6-ae22-56b6b6499611"
    val claim  = s"""{ sub: "$userId" }"""

    "asked to create a UserContext from a valid JWT" should {
      "return the UserContext" in {
        val validJwt = Jwt.encode(claim, jwtSigningKey, jwtDsa)
        UserContext.fromJwt(validJwt, jwtVerificationKey) shouldEqual UserContext(userId)
      }
    }

    "asked to create UserContext from an invalid JWT" should {
      "return None" in {
        val unsignedJwt = Jwt.encode(claim)
        UserContext.fromJwt(unsignedJwt, jwtVerificationKey) shouldEqual None
      }
    }
  }
}
