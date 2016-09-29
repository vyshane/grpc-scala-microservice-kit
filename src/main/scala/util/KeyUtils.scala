package mu.node.echod.util

import java.nio.file.{Files, Paths}
import java.security.{KeyFactory, PublicKey}
import java.security.spec.X509EncodedKeySpec

trait KeyUtils {
  def loadPublicKey(path: String): PublicKey = {
    val byteArray  = Files.readAllBytes(Paths.get(path))
    val spec       = new X509EncodedKeySpec(byteArray)
    val keyFactory = KeyFactory.getInstance("RSA")
    keyFactory.generatePublic(spec)
  }
}

