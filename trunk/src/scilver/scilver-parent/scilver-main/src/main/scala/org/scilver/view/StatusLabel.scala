package org.scilver.view

import org.jdesktop.swingx.JXLabel
import java.awt.Color
import org.jdesktop.swingx.painter.{CompoundPainter, RectanglePainter, AbstractAreaPainter, GlossPainter}
import java.awt.event.MouseEvent
import swing.Dialog
import org.jdesktop.swingx.icon.EmptyIcon
import org.scilver.html

/**
 * @author eav
 * Date: 07.11.2010
 * Time: 13:02:09
 */

case class StatusLabel(text: String) extends JXLabel(html(text)) with Clickable {
  setIcon(new EmptyIcon)
  setIconTextGap(10)

  setLineWrap(true)

  setBackgroundPainter({
    val rp1 = new RectanglePainter(5, 5, 5, 5, 20, 20);
    rp1.setFillPaint(Color.LIGHT_GRAY);
    rp1.setBorderPaint(Color.LIGHT_GRAY.darker);
    rp1.setStyle(AbstractAreaPainter.Style.BOTH);
    rp1.setBorderWidth(3);
    rp1.setAntialiasing(true);
    new CompoundPainter(rp1, new GlossPainter);
  })

  def onClick(e: MouseEvent) = Dialog.showMessage(parent = null, message = "Show conversation")
}
