package rip.deadcode.aoba3.util

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import rip.deadcode.aoba3.model.PageSetting
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

class FileReadableTest {

    var file: Path? = null
    var fileReadable: FileReadable? = null

    @Before
    fun setUp() {
        fileReadable = object : FileReadable {}

        val fs = Jimfs.newFileSystem(Configuration.unix())
        file = fs.getPath("/test")
    }

    private fun write(data: String) {
        Files.write(file, data.toByteArray(StandardCharsets.UTF_8))
    }

    @Test
    fun testRead() {
        val data = "testing"
        write(data)

        val res = fileReadable!!.read(file!!)

        assertThat(res, `is`(data))
    }

    @Test
    fun testReadMarkdown() {
        val data = """
# test
*ITALIC*
**BOLD**
"""
        write(data)

        val res = fileReadable!!.readMarkdown(file!!)

        assertThat(res, `is`("<h1>test</h1><p><em>ITALIC</em><strong>BOLD</strong></p>"))
    }

    @Test
    fun testReadSetting() {
        val data = """{"title":"foo"}"""
        write(data)

        val res = fileReadable!!.readSetting(file!!, PageSetting::class.java)

        assertThat(res.title, `is`("foo"))
    }

}