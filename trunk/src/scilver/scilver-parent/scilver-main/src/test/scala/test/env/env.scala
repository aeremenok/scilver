package test.env

import org.scilver.log
import java.io.File
import org.scilver.db.dbConfig
import org.mockito.Mockito

/**
 * @author eav
 * Date: 03.10.2010
 * Time: 17:33:04
 */

object env {
  def setUp(test: Any) {
    log.init
    log.debug("set up test " + test)
  }

  def tearDown(test: Any) {
    log.debug("tear down test " + test)
  }
}

object mockery {
  def mock[T](implicit m: Manifest[T]): T = Mockito.mock(m.erasure).asInstanceOf[T]
}

object dbEnv {
  def setUp(test: Any) {
    env.setUp(test)

    dbConfig.dbDir = System.getProperty("java.io.tmpdir")
    val f = new File(dbConfig.scilverDbDir)
    assert(!f.exists || f.delete)

    log.debug("temp db dir is clean")
  }

  def tearDown(test: Any) = env.tearDown(test)
}
