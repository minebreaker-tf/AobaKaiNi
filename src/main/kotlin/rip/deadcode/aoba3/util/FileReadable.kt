package rip.deadcode.aoba3.util

import com.google.gson.Gson
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import java.nio.charset.StandardCharsets
import java.nio.file.Path

interface FileReadable {

    fun read(path: Path): String {
        return String(java.nio.file.Files.readAllBytes(path), StandardCharsets.UTF_8)
    }

    fun readMarkdown(path: Path): String {
        val md = read(path)
        val nodes = markdownParser.parse(md)
        val html = htmlRenderer.render(nodes)
        return html.replace("\n", "").replace("\r", "")
    }

    fun <T> readSetting(path: Path, clazz: Class<T>): T {
        val json = read(path)
        return gson.fromJson(json, clazz)
    }

    companion object {
        val gson = Gson()
        val markdownParser = Parser.builder().build()
        val htmlRenderer = HtmlRenderer.builder().build()
    }

}