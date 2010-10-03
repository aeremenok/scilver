package org.scilver.db.dao

import org.hibernate.Session
import java.io.Serializable
import org.scilver.db.{HibernateConnector => JPA}
import org.scilver.log

/**
 * @author eav
 * Date: 20.09.2010
 * Time: 22:57:48
 */

trait DAO[E] {
  protected def entityClass: Class[E]

  def getById(id: Serializable): Option[E] = query {
    session =>
      session.get(entityClass, id) match {
        case e: E => Some[E](e)
        case _ => None
      }
  }

  def save(entity: E): E = modify {
    session => {
      session.persist(entity)
      entity
    }
  }

  def delete(entity: E) = modify {
    session => session.delete(entity)
  }

  protected def query[T](f: Session => T): T = {
    val session = JPA.openSession
    try {f(session)}
    finally JPA.close(session)
  }

  protected def modify[T](f: Session => T): T = {
    val session = JPA.openSession
    try {
      session.beginTransaction
      val result = f(session)
      session.getTransaction.commit

      result
    }
    catch {
      case t: Throwable => {
        log.error(t, t)
        session.getTransaction.rollback
        throw new Error(t)
      }
    }
    finally JPA.close(session)
  }
}
