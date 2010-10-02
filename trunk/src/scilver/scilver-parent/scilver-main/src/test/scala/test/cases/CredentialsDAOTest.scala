package test.cases

import org.scilver.db.dao.credentialsDAO
import org.testng.annotations.{BeforeClass, Test}
import org.scilver.db.{HibernateConnector, Credentials}
import org.apache.log4j.PropertyConfigurator

/**
 * @author eav
 * Date: 20.09.2010
 * Time: 23:18:41
 */

class CredentialsDAOTest {
  @BeforeClass
  def setUp {
    val url = getClass.getClassLoader.getResource("log4j.properties")
    assert(url != null)
    PropertyConfigurator.configure(url)

    val c = HibernateConnector.openSession.connection
    try {c.prepareStatement("delete from Credentials").executeUpdate}
    finally c.close
  }

  @Test
  def loadFirst {
    val c = credentialsDAO.getById[Credentials](id = 1)
    assert(c == null)
  }

  var credentials: Credentials = null

  @Test(dependsOnMethods = Array[String]("loadFirst"))
  def saveOne {
    credentials = new Credentials(
      userId = 1,
      screenName = "eav",
      token = "token",
      tokenSecret = "secret"
      )
    credentialsDAO.save(credentials)
  }

  @Test(dependsOnMethods = Array[String]("saveOne"))
  def loadSaved {
    val c = credentialsDAO.getById[Credentials](1)
    assert(c == credentials)
  }
}
