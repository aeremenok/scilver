package test.cases

import twitter4j.{User, Twitter}
import org.fest.swing.fixture.FrameFixture
import java.net.URL
import twitter4j.http.AccessToken
import test.env.mockery._
import org.mockito.Mockito.when
import org.scilver.db.Credentials
import org.scilver._
import org.testng.annotations.{AfterClass, Test, BeforeClass}
import test.env.env

/**
 * @author eav
 * Date: 05.09.2010
 * Time: 12:57:36
 */
class MainFrameTest {
  @BeforeClass
  def setUp {
    authentication.authService = FakeAuthService
    App.startup(Array.empty)
  }

  @Test
  def verifyContents {
    val frame = new FrameFixture(mainFrame.peer)

    frame button "eav" requireText "eav"
    frame button "Tweet" requireText "Tweet"
  }

  @AfterClass
  def tearDown() = App.shutdown
}

object FakeAuthService extends AuthService {
  def login = {
    val twitter = mock[Twitter]
    val user = mock[User]
    val accessToken = mock[AccessToken]

    when(twitter.verifyCredentials) thenReturn user
    when(user.getScreenName) thenReturn "eav"
    when(user.getProfileImageURL) thenReturn getClass.getClassLoader.getResource("im-user.png")

    val c = Credentials(0, "eav", "token", "tokenSecret")

    Some(Authentication(c, twitter, user))
  }
}
