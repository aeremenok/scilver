package org.scilver.db.dao

import org.hibernate.Session
import java.io.Serializable
import org.scilver.db.{HibernateConnector => JPA}

/**
 * @author eav
 * Date: 20.09.2010
 * Time: 22:57:48
 */

trait DAO[E] {
  def getById[E](id: Serializable)(implicit m: Manifest[E]): E = query {
    session => session.get(m.erasure, id).asInstanceOf[E]
  }

  def save[E](entity: E): E = modify {
    session => {
      session.persist(entity)
      entity
    }
  }

  def delete[E](entity: E) = modify {
    session => session.delete(entity)
  }

  def query[E](f: Session => E): E = {
    val session = JPA.openSession
    try {f(session)}
    finally JPA.close(session)
  }

  def modify[E](f: Session => E): E = {
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
