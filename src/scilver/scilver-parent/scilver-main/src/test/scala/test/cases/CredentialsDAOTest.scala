package test.cases

import org.scilver.db.dao.credentialsDAO
import org.testng.annotations.{BeforeClass, Test}
import org.scilver.db.Credentials
import test.env.dbEnv

/**
 * @author eav
 * Date: 20.09.2010
 * Time: 23:18:41
 */

class CredentialsDAOTest {
  @BeforeClass
  def setUp = dbEnv.setUp(this)

  var credentials: Credentials = _

  @Test
  def loadFirst =
    assert(credentialsDAO.getById(1) == None)

  @Test(dependsOnMethods = Array("loadFirst"))
  def saveOne {
    credentials = Credentials(1, "eav", "token", "secret")
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
