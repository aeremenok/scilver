package org.scilver

import annotation.elidable
import elidable.INFO
import org.apache.log4j.{Logger, PropertyConfigurator}

/**
 * @author eav
 * Date: 03.10.2010
 * Time: 11:01:32
 */

object log {
  private lazy val logger = Logger.getLogger(getClass)

  def init {
    val url = getClass.getClassLoader.getResource("log4j.properties")
    assert(url != null)
    PropertyConfigurator.configure(url)
    debug("logger initialized")
  }

//  @elidable(INFO)
  def debug(message: Any) = logger.debug(String.valueOf(message))

  def error(message: Any, e: Throwable) = logger.error(String.valueOf(message), e)
}
