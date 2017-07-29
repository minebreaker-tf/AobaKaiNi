package rip.deadcode.aoba3.util

import com.google.common.collect.ImmutableList
import com.google.gson.Gson
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
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
        return html
    }

    fun <T> readJson(path: Path, clazz: Class<T>): T {
        val json = read(path)
        return gson.fromJson(json, clazz)
    }

    companion object {
        private val gson = Gson()
        private val option = MutableDataSet()
                .set(Parser.EXTENSIONS, ImmutableList.of(TablesExtension.create()))
        private val markdownParser = Parser.builder(option).build()
        private val htmlRenderer = HtmlRenderer.builder(option).build()
    }

}