package rip.deadcode.aoba3.util

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class Strings2Test {

    @Test
    fun testDropFirst() {
        val res = Strings2.dropFirst("////abc/def", "/")

        assertThat(res, `is`("abc/def"))
    }

}