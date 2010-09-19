package test.env

import org.scilver._

/**
 * @author eav
 * Date: 05.09.2010
 * Time: 12:09:58
 */

class TestApp(fakeCredentials: Authentication) extends BasicApp
{
  override def login = fakeCredentials
}
