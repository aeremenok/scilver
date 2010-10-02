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
  def login =
    Dialog.showInput(message = i18n.tr("Please enter your screen name"), initial = "")
    match {
      case screenName: Some[String] => Some[Authentication](authByName(screenName.get))
      case _ => None
    }


  private def authByName(screenName: String): Authentication = {
    val twitter = new TwitterFactory().getInstance
    twitter.setOAuthConsumer("IESXizMmIy26f1pMVPYlhg", "5k31Fkf9re8koYf4MLGlt7Osrz89dUUa2b3ot99ZM")

    credentialsDAO.findByName(screenName) match {
      case credentials: Some[Credentials] => {
        twitter.setOAuthAccessToken(credentials.get.toAccessToken)
        val user = twitter.verifyCredentials
        Authentication(credentials.get, twitter, user)
      }
      case _ => confirmPinOnSite(twitter)
    }
  }

  private def confirmPinOnSite(twitter: Twitter): Authentication = {
    val request = twitter.getOAuthRequestToken
    Desktop.getDesktop.browse(new URI(request.getAuthorizationURL))

    Dialog.showInput(message = i18n.tr("Please enter pin"), initial = "")
    match {
      case pin: Some[String] =>
        try {
          val accessToken = twitter.getOAuthAccessToken(request, pin.get)
          Authentication(
            credentialsDAO.save(Credentials(accessToken)),
            twitter,
            twitter.verifyCredentials
            )
        }
        catch {
          case e: TwitterException => {
            e.printStackTrace
            confirmPinOnSite(twitter)
          }
        }
      case None => confirmPinOnSite(twitter)
    }
  }
}









