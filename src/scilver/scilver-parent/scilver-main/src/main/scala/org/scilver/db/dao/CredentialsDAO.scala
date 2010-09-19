package org.scilver.db.dao

import org.scilver.db.{Credentials, HibernateConnector => JPA}
import org.hibernate.Session

/**
 * @author eav
 * Date: 19.09.2010
 * Time: 22:45:52
 */

object CredentialsDAO {
  def findByName(name: String): Credentials = query {
    session =>
      val criteria = session.createCriteria(classOf[Credentials])
      criteria.uniqueResult.asInstanceOf[Credentials]
  }

  def save(credentials: Credentials) = modify {
    session => session.persist(credentials)
  }

  def delete(credentials: Credentials) = modify {
    session => session.delete(credentials)
  }


  def query[B](f: Session => B): B = {
    val session = JPA.openSession
    try {f(session)}
    finally JPA.close(session)
  }

  def modify[B](f: Session => B): B = {
    val session = JPA.openSession
    try {
      session.beginTransaction
      val result = f(session)
      session.getTransaction.commit

      result
    }
    catch {
      case t: Throwable => {
        t.printStackTrace
        session.getTransaction.rollback
        throw new Error(t)
      }
    }
    finally JPA.close(session)
  }
}
