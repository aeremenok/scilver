package org.scilver.db

import reflect.BeanProperty
import javax.persistence.{Id, Entity}
import twitter4j.http.AccessToken

/**
 * @author eav
 * Date: 19.09.2010
 * Time: 21:58:24
 */
@Entity
class Credentials {
  @Id
  @BeanProperty
  var userId: Int = 0

  @BeanProperty
  var screenName: String = ""

  @BeanProperty
  var token: String = ""

  @BeanProperty
  var tokenSecret: String = ""

  def toAccessToken = new AccessToken(token, tokenSecret)
}

object Credentials {
  def apply(accessToken: AccessToken): Credentials = {
    val c = new Credentials
    c.userId = accessToken.getUserId
    c.screenName = accessToken.getScreenName
    c.token = accessToken.getToken
    c.tokenSecret = accessToken.getTokenSecret
    c
  }
}
