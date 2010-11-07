package test.cases

import test.env.ComponentEnv
import org.fest.swing.fixture.FrameFixture
import org.testng.annotations.{AfterClass, Test, BeforeClass}
import org.scilver.view.StatusLabel
import java.util.concurrent.TimeUnit

/**
 * @author eav
 * Date: 07.11.2010
 * Time: 13:04:40
 */

class StatusTextTest extends ComponentEnv {
  var fixture: FrameFixture = _

  @BeforeClass
  override def setUp = super.setUp()

  @Test
  def display {
    fixture = frameWith(StatusLabel(
      "The standard text component commands for cut, copy, and paste used enhanced selection methods. The commands will only be active "
      ), 300, 75)
    fixture.show
    TimeUnit.SECONDS.sleep(30)
  }

  @AfterClass
  override def tearDown = {
    fixture.cleanUp
    super.tearDown
  }
}
