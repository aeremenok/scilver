package org.scilver.view

import scala.swing._
import javax.swing.{Box, ImageIcon}
import org.scilver.{App, tr}

/**
 * @author eav
 * Date: 28.08.2010
 * Time: 12:54:04
 */
object toolBar extends BoxPanel(Orientation.Horizontal) {
  def +=(a: Action) =
    contents += new Button(a) {
      name = a.title
      tooltip = a.title
      peer.setHideActionText(true)
    }

  this += profileAction
  this += tweetAction
  this += followersAction
  this += followingAction

  peer.add(Box.createHorizontalGlue)

  contents += Component.wrap(tasks)
}

object fromFile {
  def apply(fileName: String) =
    new ImageIcon(getClass.getClassLoader.getResource(fileName))
}

object tweetAction extends Action(tr("Tweet")) {
  icon = fromFile("tweet.png")
  def apply = Dialog.showMessage(toolBar, "Tweet") // todo
}

object profileAction extends Action(App.user.getScreenName) {
  icon = new ImageIcon(App.user.getProfileImageURL)
  def apply = Dialog.showMessage(toolBar, "User profile") // todo
}

object followersAction extends Action(tr("Followers")) {
  icon = fromFile("followers.png")
  def apply = Dialog.showMessage(toolBar, "Followers") // todo
}

object followingAction extends Action(tr("Following")) {
  icon = fromFile("following.png")
  def apply = Dialog.showMessage(toolBar, "Following") // todo
}
