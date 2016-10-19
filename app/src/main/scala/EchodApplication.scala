package mu.node.echod

import org.slf4j.LoggerFactory

/*
 * Main entry point for the application
 */
object EchodApplication extends EchodModule {
  def main(args: Array[String]): Unit = {
    LoggerFactory.getLogger(this.getClass).info("Starting gRPC server")
    echoServer.start().awaitTermination()
  }
}
