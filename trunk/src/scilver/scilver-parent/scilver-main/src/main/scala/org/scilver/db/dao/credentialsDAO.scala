package org.scilver.db.dao

import org.scilver.db.Credentials
import org.hibernate.criterion.Restrictions

/**
 * @author eav
 * Date: 19.09.2010
 * Time: 22:45:52
 */

object credentialsDAO extends DAO[Credentials] {
  protected def entityClass = classOf[Credentials]

  def findByName(name: String): Option[Credentials] = query {
    session =>
      val criteria = session.createCriteria(entityClass)
      criteria.add(Restrictions.eq("screenName", name))
      criteria.uniqueResult match {
        case credentials: Credentials => Some[Credentials](credentials)
        case _ => None
      }
  }
}
