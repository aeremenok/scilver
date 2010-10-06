package org.scilver

import org.jdesktop.swingx.JXBusyLabel
import javax.swing.SwingWorker
import java.util.concurrent.atomic.AtomicInteger
import java.awt.{Color, Dimension}

/**
 * @author eav
 * Date: 06.10.2010
 * Time: 23:10:39
 */
object tasks extends JXBusyLabel(new Dimension(50, 50)) {
  setBusy(false)
  setDelay(7)
  override def createBusyPainter(dim: Dimension) = {
    val p = super.createBusyPainter(dim)
    p.setPoints(50)
    p.setTrailLength(20)
    p.setAntialiasing(true)
    p.setHighlightColor(new Color(44, 61, 146).darker)
    p.setBaseColor(new Color(168, 204, 241).brighter)
    p
  }

  private val running = new AtomicInteger(0)

  def execute[V](background: => V, edt: V => _) {
    val w = new SwingWorker[V, Any] {
      def doInBackground = background

      override def done = {
        edt(get)
        setBusy(running.decrementAndGet > 0)
      }
    }

    setBusy(running.incrementAndGet > 0)
    w.execute
  }

  override def setBusy(busy: Boolean) {
    setVisible(busy)
    super.setBusy(busy)
  }
}

