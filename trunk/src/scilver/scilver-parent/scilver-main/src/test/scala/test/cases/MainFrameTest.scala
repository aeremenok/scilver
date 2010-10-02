package test.cases

import twitter4j.{User, Twitter}
import org.fest.swing.fixture.FrameFixture
import org.testng.annotations.{Test, BeforeClass}
import java.net.URL
import twitter4j.http.AccessToken
import org.scilver.mainFrame
import test.env.mockery._
import org.mockito.Mockito.when

/**
 * @author eav
 * Date: 05.09.2010
 * Time: 12:57:36
 */
class MainFrameTest {
  @BeforeClass
  def setUp {
    val twitter = mock[Twitter]
    val user = mock[User]
    val accessToken = mock[AccessToken]

    when(twitter.verifyCredentials) thenReturn user
    when(user.getScreenName) thenReturn "eav"
    when(user.getProfileImageURL) thenReturn new URL("http://www.gstatic.com/codesite/ph/images/defaultlogo.png")

    //    new BasicApp {
    //      override def login = Authentication(Credentials(accessToken), twitter, user)
    //    }.startup(null)
  }

  @Test
  def verifyContents {
    val frame = new FrameFixture(mainFrame.peer)

    frame button "eav" requireText "eav"
    frame button "Tweet" requireText "Tweet"
  }
}


