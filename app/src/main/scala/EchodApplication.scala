package mu.node.echod

/*
 * Main entry point for the application
 */
object EchodApplication extends EchodModule {
  def main(args: Array[String]): Unit = {
    echoServer.start().awaitTermination()
  }
}

