package mu.node.echod.util

import java.nio.file.{Files, Paths}
import java.security.{KeyFactory, PrivateKey, PublicKey}
import java.security.spec.X509EncodedKeySpec

import pdi.jwt.JwtAlgorithm.RS256

trait KeyUtils {

  val jwtDsa = RS256

  def loadPrivateKey(path: String): PrivateKey = {
    val keyBytes = Files.readAllBytes(Paths.get(path))
    val spec     = new X509EncodedKeySpec(keyBytes)
    KeyFactory.getInstance("RSA").generatePrivate(spec)
  }

  def loadPublicKey(path: String): PublicKey = {
    val keyBytes = Files.readAllBytes(Paths.get(path))
    val spec     = new X509EncodedKeySpec(keyBytes)
    KeyFactory.getInstance("RSA").generatePublic(spec)
  }
}
