package test.cases

import org.scilver.db.dao.credentialsDAO
import org.testng.annotations.{BeforeClass, Test}
import org.scilver.db.{HibernateConnector, Credentials}
import org.scilver.log

/**
 * @author eav
 * Date: 20.09.2010
 * Time: 23:18:41
 */

class CredentialsDAOTest {
  @BeforeClass
  def setUp {
    log.init

    val c = HibernateConnector.openSession.connection
    try {c.prepareStatement("delete from Credentials").executeUpdate}
    finally c.close
  }

  @Test
  def loadFirst {
    val c = credentialsDAO.getById(1)
    assert(c == None)
  }

  var credentials: Credentials = _

  @Test(dependsOnMethods = Array("loadFirst"))
  def saveOne {
    credentials = new Credentials(
      userId = 1,
      screenName = "eav",
      token = "token",
      tokenSecret = "secret"
      )
    credentialsDAO.save(credentials)
  }

  @Test(dependsOnMethods = Array("saveOne"))
  def loadSaved {
    val c1 = credentialsDAO.getById(1)
    assert(c1.get == credentials)

    val c2 = credentialsDAO.findByName("eav")
    assert(c2.get == credentials)

    assert(!(c1.get eq c2.get))
  }
}
