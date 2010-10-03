package org.scilver.db

import org.h2.jdbcx.JdbcConnectionPool
import java.sql.Connection
import org.hibernate.connection.ConnectionProvider
import java.util.Properties

/**
 * @author eav
 * Date: 19.09.2010
 * Time: 20:53:08
 */

class H2Connector extends ConnectionProvider {
  private var pool: JdbcConnectionPool = _

  def supportsAggressiveRelease = false

  def close = if (pool != null) pool.dispose

  def closeConnection(c: Connection) = c.close

  def getConnection = pool.getConnection

  def configure(ingored: Properties) = {
    close

    val home = System.getProperty("user.home")
    val scilverDir = home + "/.scilver/db/main"
    val params = "TRACE_LEVEL_FILE=1;TRACE_LEVEL_SYSTEM_OUT=0;TRACE_MAX_FILE_SIZE=1;AUTO_SERVER=TRUE"
    val url = "jdbc:h2:nio:" + scilverDir + ";" + params;

    pool = JdbcConnectionPool.create(url, "sa", "")
    pool.setMaxConnections(5)
  }
}
