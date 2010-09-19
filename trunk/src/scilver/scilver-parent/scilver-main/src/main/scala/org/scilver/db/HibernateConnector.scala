package org.scilver.db

import org.hibernate.cfg.AnnotationConfiguration
import org.hibernate.Session

/**
 * @author eav
 * Date: 19.09.2010
 * Time: 22:17:42
 */

object HibernateConnector {
  private lazy val sessionFactory = init

  private def init = {
    val cfg = new AnnotationConfiguration
    cfg.configure.buildSessionFactory
  }

  def openSession = sessionFactory.openSession

  def close(session: Session) = session.close
}

