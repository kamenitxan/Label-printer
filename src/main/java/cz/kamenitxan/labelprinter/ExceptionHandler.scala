package cz.kamenitxan.labelprinter

import org.apache.log4j.Logger

/**
  * Created by tomaspavel on 13.3.17.
  */
class ExceptionHandler extends Thread.UncaughtExceptionHandler {
	private final val logger = Logger.getLogger(classOf[ExceptionHandler])

	override def uncaughtException(t: Thread, e: Throwable): Unit = {
		logger.fatal("Uncaught exception in thread: " + t.toString, e)
		e.printStackTrace()
	}
}
