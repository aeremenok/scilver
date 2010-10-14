package org.scilver

import db.Credentials
import db.dao.credentialsDAO
import java.awt.Desktop
import java.net.URI
import swing.Dialog
import twitter4j.{User, TwitterException, TwitterFactory, Twitter}
/**
 * @author eav
 * Date: 28.08.2010
 * Time: 12:54:04
 */
case class Authentication(credentials: Credentials, twitter: Twitter, user: User){
  @throws(classOf[TwitterException])
  def this(credentials: Credentials, twitter: Twitter) =
    this(credentials, twitter, twitter.verifyCredentials)
}

object authentication {
  var authService: AuthService = AuthServiceImpl

  def login = authService.login
}

trait AuthService {
  def login: Option[Authentication]
}

object AuthServiceImpl extends AuthService with Loggable {
  def login =
    Dialog.showInput(message = i18n.tr("Please enter your user name"), initial = "") match {
      case Some(screenName) => authByName(screenName)
      case _ => None
    }

  private def authByName(screenName: String): Option[Authentication] = {
    val twitter = new TwitterFactory().getInstance
    twitter.setOAuthConsumer("IESXizMmIy26f1pMVPYlhg", "5k31Fkf9re8koYf4MLGlt7Osrz89dUUa2b3ot99ZM")

    credentialsDAO.findByName(screenName) match {
      case Some(credentials) =>
        twitter.setOAuthAccessToken(credentials.toAccessToken)
        Some(new Authentication(credentials, twitter))

      case _ => confirmPinOnSite(twitter)
    }
  }

  private def confirmPinOnSite(twitter: Twitter): Option[Authentication] = {
    val request = twitter.getOAuthRequestToken
    Desktop.getDesktop.browse(new URI(request.getAuthorizationURL))

    Dialog.showInput(message = i18n.tr("Please enter pin"), initial = "") match {
      case Some(pin) =>
        try {
          val accessToken = twitter.getOAuthAccessToken(request, pin)
          val credentials = credentialsDAO.save(Credentials(accessToken))
          Some(new Authentication(credentials, twitter))
        }
        catch {
          case e: TwitterException => {
            error(e, e)
            confirmPinOnSite(twitter)
          }
        }

      case _ => None
    }
  }
}
