package com.example.myapplication

import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val time = DateTime.now()
        val plusTime = time.plusMonths(-1)
        assertEquals(31, plusTime.dayOfMonth().maximumValue)
    }
}
