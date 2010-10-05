package test.cases

import org.fest.swing.fixture.FrameFixture
import twitter4j.http.AccessToken
import test.env.mockery._
import org.mockito.Mockito.when
import org.scilver.db.Credentials
import org.scilver._
import org.testng.annotations.{AfterClass, Test, BeforeClass}
import twitter4j.{Status, ResponseList, User, Twitter}
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
    val url = getClass.getClassLoader.getResource("im-user.png")

    val twitter = mock[Twitter]
    val accessToken = mock[AccessToken]

    val eav = mock[User]
    val friend = mock[User]

    when(eav getId) thenReturn 0
    when(friend getId) thenReturn 1

    when(twitter.verifyCredentials) thenReturn eav
    when(eav.getScreenName) thenReturn "eav"
    when(eav.getProfileImageURL) thenReturn url
    when(friend getProfileImageURL) thenReturn url

    val timeline = mock[ResponseList[Status]]
    val s1 = mock[Status]
    val s2 = mock[Status]

    when(timeline.size) thenReturn 2
    when(timeline get 0) thenReturn s1
    when(timeline get 1) thenReturn s2

    when(twitter getFriendsTimeline) thenReturn timeline

    when(s1 getUser) thenReturn eav
    when(s2 getUser) thenReturn friend

    when(s1 getText) thenReturn "It's me!"
    when(s2 getText) thenReturn "Hi!"

    val c = Credentials(0, "eav", "token", "tokenSecret")

    Some(Authentication(c, twitter, eav))
  }
}
