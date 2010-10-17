package org.scilver

import db.Credentials
import db.dao.credentialsDAO
import java.awt.Desktop
import java.net.URI
import twitter4j.{User, TwitterException, TwitterFactory, Twitter}
import view.{pinDialog, loginDialog}

/**
 * @author eav
 * Date: 28.08.2010
 * Time: 12:54:04
 */
case class Authentication(credentials: Credentials, twitter: Twitter, user: User) {
  @throws(classOf[TwitterException])
  def this(credentials: Credentials, twitter: Twitter) =
    this (credentials, twitter, twitter.verifyCredentials)
}

object authentication {
  var authService: AuthService = AuthServiceImpl

  def login = authService.login
}

trait AuthService {
  def login: Option[Authentication]
}

object AuthServiceImpl extends AuthService with Loggable {
  private def createTwitter = {
    val twitter = new TwitterFactory().getInstance
    twitter.setOAuthConsumer("IESXizMmIy26f1pMVPYlhg", "5k31Fkf9re8koYf4MLGlt7Osrz89dUUa2b3ot99ZM")
    twitter
  }

  private lazy val twitter = createTwitter
  private lazy val request = twitter.getOAuthRequestToken

  def login = loginDialog(authByName) match {
    case None => showPinDialog
    case x => x
  }

  private def authByName(screenName: String) = credentialsDAO.findByName(screenName) match {
    case Some(credentials) =>
      twitter.setOAuthAccessToken(credentials.toAccessToken)
      Some(new Authentication(credentials, twitter))

    case _ => {
      Desktop.getDesktop.browse(new URI(request.getAuthorizationURL))
      None
    }
  }

  private def showPinDialog = pinDialog(processPin)

  private def processPin(pin: String) = try {
    val accessToken = twitter.getOAuthAccessToken(request, pin)
    val credentials = credentialsDAO.save(Credentials(accessToken))
    Some(new Authentication(credentials, twitter))
  }
  catch {
    case e: TwitterException => error(e, e)
  }
}
