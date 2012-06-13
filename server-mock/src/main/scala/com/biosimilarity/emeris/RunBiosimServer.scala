package com.biosimilarity.emeris

import net.model3.servlet.runner.JettyRunner
import net.model3.logging.LoggingConfigurator
import net.model3.logging.Level
import net.model3.newfile.File
import m3.predef._
import net.model3.logging.SimpleLoggingConfigurator

object RunBiosimServer extends App {

  // so we can see any bootstrapping errors
  new SimpleLoggingConfigurator().addConsoleAppender
  
  // so we can log stuff to disk
  val lc = inject[LoggingConfigurator]
  lc.addStandardHouseKeepingAppenders
//  slc.addEclipseConsoleAppender()
  
  JettyRunner.main(args)
  
}
