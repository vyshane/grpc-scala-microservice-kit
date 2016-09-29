package mu.node.echod.util

import java.io.File

trait FileUtils {
  def fileForAbsolutePath(path: String): File = new File(path)
  def fileForTestResourcePath(path: String): File = new File(pathForTestResourcePath(path))
  def pathForTestResourcePath(path: String): String = getClass.getResource(path).getPath
}

