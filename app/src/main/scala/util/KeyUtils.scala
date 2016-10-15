package mu.node.echod.util

import java.nio.file.{Files, Paths}
import java.security.{KeyFactory, PrivateKey, PublicKey}
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}

import pdi.jwt.JwtAlgorithm.RS256

trait KeyUtils {

  val jwtDsa = RS256

  def loadPkcs8PrivateKey(path: String): PrivateKey = {
    val keyBytes = Files.readAllBytes(Paths.get(path))
    val spec = new PKCS8EncodedKeySpec(keyBytes)
    KeyFactory.getInstance("RSA").generatePrivate(spec)
  }

  def loadX509PublicKey(path: String): PublicKey = {
    val keyBytes = Files.readAllBytes(Paths.get(path))
    val spec = new X509EncodedKeySpec(keyBytes)
    KeyFactory.getInstance("RSA").generatePublic(spec)
  }
}
