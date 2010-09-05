package test.cases

import test.env.TestApp
import org.mockito.Mockito
import twitter4j.{User, Twitter}
import Mockito._
import org.fest.swing.fixture.FrameFixture
import org.testng.annotations.{Test, BeforeClass}
import org.scilver.mainFrame
import java.net.URL

/**
 * @author eav
 * Date: 05.09.2010
 * Time: 12:57:36
 */
class MainFrameTest {
  @BeforeClass
  def setUp {
    val twitter = mock(classOf[Twitter])
    val user = mock(classOf[User])

    when(twitter.verifyCredentials) thenReturn user
    when(user.getScreenName) thenReturn "eav"
    when(user.getProfileImageURL) thenReturn new URL("http://www.gstatic.com/codesite/ph/images/defaultlogo.png")

    TestApp.startup(twitter, user)
  }

  @Test
  def verifyContents {
    val frame = new FrameFixture(mainFrame.peer)

    frame button "eav" requireText "eav"
    frame button "Tweet" requireText "Tweet"
  }
}
