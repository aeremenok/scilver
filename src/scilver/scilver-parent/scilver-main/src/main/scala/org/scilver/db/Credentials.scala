package org.scilver.db

import twitter4j.http.AccessToken
import javax.persistence.Entity
import scalaJPA.Id

/**
 * @author eav
 * Date: 19.09.2010
 * Time: 21:58:24
 */
@Entity
case class Credentials(
        @Id
        userId: Int,
        screenName: String,
        token: String,
        tokenSecret: String
        ) {
  def this() = this (userId = -1, screenName = "", token = "", tokenSecret = "")

  def toAccessToken = new AccessToken(token, tokenSecret)
}

object Credentials {
  def apply(accessToken: AccessToken) =
    new Credentials(
      accessToken.getUserId,
      accessToken.getScreenName,
      accessToken.getToken,
      accessToken.getTokenSecret
      )
}
