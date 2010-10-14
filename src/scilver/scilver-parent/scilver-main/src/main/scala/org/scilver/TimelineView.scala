package org.scilver

import java.awt.Color
import scala.swing._
import javax.swing.{SwingConstants, ImageIcon}
import org.jdesktop.swingx.JXLabel
import org.jdesktop.swingx.painter._
import twitter4j.{Paging, Status}
import java.util.{List => JList}
import org.scilver.view.RollingTableModel

/**
 * @author eav
 * Date: 05.10.2010
 * Time: 21:56:10
 */
object TimelineView extends Table {
  model = new TimelineModel
  override def model = super.model.asInstanceOf[TimelineModel]

  override protected def rendererComponent(isSelected: Boolean, focused: Boolean, row: Int, column: Int) =
    new StatusRenderer(apply(row, column).asInstanceOf[Status])

  peer.setTableHeader(null)

  rowHeight = 100
}

class TimelineModel extends RollingTableModel[Status](Array[Object]("Main")) {
  private var currentPage: Int = 0

  protected def loadPortion = {
    currentPage += 1
    App.twitter.getFriendsTimeline(new Paging(currentPage))
  }
}

case class StatusRenderer(status: Status) extends BorderPanel {
  import BorderPanel.Position._

  add(Component.wrap(new TweetLabel(status)), Center)
  val userIcon = new IconLabel(status)

  if (App.user == status.getUser)
    add(userIcon, East) else
    add(userIcon, West)
}

class TweetLabel(status: Status) extends JXLabel {
  setText(status.getText)
  setHorizontalTextPosition(SwingConstants.CENTER)
  setHorizontalAlignment(SwingConstants.CENTER)

  setBackgroundPainter(createPainter)
  def createPainter = {
    val rp1 = new RectanglePainter(5, 5, 5, 5, 20, 20);
    rp1.setFillPaint(Color.LIGHT_GRAY);
    rp1.setBorderPaint(Color.LIGHT_GRAY.darker);
    rp1.setStyle(AbstractAreaPainter.Style.BOTH);
    rp1.setBorderWidth(3);
    rp1.setAntialiasing(true);
    new CompoundPainter(rp1, new GlossPainter());
  }
}

class IconLabel(status: Status) extends Label {
  icon = new ImageIcon(status.getUser.getProfileImageURL)
}
