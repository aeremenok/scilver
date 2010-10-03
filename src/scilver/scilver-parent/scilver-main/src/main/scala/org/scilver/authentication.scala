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
case class Authentication(credentials: Credentials, twitter: Twitter, user: User)

object authentication {
  var authService: AuthService = AuthServiceImpl

  def login = authService.login
}

trait AuthService {
  def login: Option[Authentication]
}

object AuthServiceImpl extends AuthService {
  def login =
    Dialog.showInput(message = i18n.tr("Please enter your screen name"), initial = "")
    match {
      case screenName: Some[String] =>
        val auth = authByName(screenName.get)
        log.debug("logged in successfully")
        Some[Authentication](auth)

      case _ => None
    }

  private def authByName(screenName: String): Authentication = {
    val twitter = new TwitterFactory().getInstance
    twitter.setOAuthConsumer("IESXizMmIy26f1pMVPYlhg", "5k31Fkf9re8koYf4MLGlt7Osrz89dUUa2b3ot99ZM")

    credentialsDAO.findByName(screenName) match {
      case credentials: Some[Credentials] =>
        log.debug("credentials loaded from db" + credentials)

        twitter.setOAuthAccessToken(credentials.get.toAccessToken)
        val user = twitter.verifyCredentials
        Authentication(credentials.get, twitter, user)

      case _ => confirmPinOnSite(twitter)
    }
  }

  private def confirmPinOnSite(twitter: Twitter): Authentication = {
    val request = twitter.getOAuthRequestToken
    Desktop.getDesktop.browse(new URI(request.getAuthorizationURL))

    Dialog.showInput(message = i18n.tr("Please enter pin"), initial = "") match {
      case pin: Some[String] =>
        try {
          val accessToken = twitter.getOAuthAccessToken(request, pin.get)
          val credentials = credentialsDAO.save(Credentials(accessToken))
          log.debug("acquired credentials " + credentials)
          val user = twitter.verifyCredentials
          Authentication(credentials, twitter, user)
        }
        catch {
          case e: TwitterException => {
            log.error(e, e)
            confirmPinOnSite(twitter)
          }
        }
      case None => confirmPinOnSite(twitter)
    }
  }
}
