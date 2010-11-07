package org.scilver.view

import java.awt.event.{MouseMotionAdapter, MouseAdapter, MouseEvent}
import java.awt.{Cursor, Component => AWTComponent}

/**
 * @author eav
 * Date: 06.11.2010
 * Time: 20:27:26
 */


trait Clickable extends AWTComponent {
  def onClick(e: MouseEvent): Unit

  addMouseListener(new MouseAdapter {
    override def mouseClicked(e: MouseEvent) = onClick(e)
  })

  addMouseMotionListener(new MouseMotionAdapter {
    override def mouseMoved(e: MouseEvent) = setCursor(new Cursor(Cursor.HAND_CURSOR))
  })
}
