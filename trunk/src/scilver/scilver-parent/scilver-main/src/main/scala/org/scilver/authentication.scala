package org.scilver

import db.Credentials
import db.dao.CredentialsDAO
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
  def login = {
    val twitter = new TwitterFactory().getInstance();
    twitter.setOAuthConsumer("IESXizMmIy26f1pMVPYlhg", "5k31Fkf9re8koYf4MLGlt7Osrz89dUUa2b3ot99ZM")
    enterPin(twitter)
  }

  private def enterPin(twitter: Twitter): Authentication = {
    val request = twitter.getOAuthRequestToken
    Desktop.getDesktop.browse(new URI(request.getAuthorizationURL))

    Dialog.showInput(message = i18n.tr("Enter pin"), initial = "") match {
      case None => enterPin(twitter)
      case pin: Some[String] =>
        try {
          val accessToken = twitter.getOAuthAccessToken(request, pin.get)
          val user = twitter.verifyCredentials
          val credentials = Credentials(accessToken)
          CredentialsDAO.save(credentials)
          Authentication(credentials, twitter, user)
        }
        catch {
          case e: TwitterException => {
            println(e.getMessage)
            enterPin(twitter)
          }
        }
    }
  }
}









