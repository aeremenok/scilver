package test.env

import org.mockito.Mockito
/**
 * @author eav
 * Date: 05.09.2010
 * Time: 12:57:36
 */
object mockery {
  def mock[T](implicit m: Manifest[T]) : T = Mockito.mock(m.erasure).asInstanceOf[T]
}

