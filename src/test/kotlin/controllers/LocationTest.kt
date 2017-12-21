package controllers
import org.junit.Assert.assertEquals
import org.junit.Test
import models.Location
class LocationTest {
  @Test
  fun testCreateLocation() {  //dummy test
    val location1 = Location(5.1, 10.1)
    val location2 = Location(5.1, 10.1)
    assertEquals(location1, location2)
  }
}