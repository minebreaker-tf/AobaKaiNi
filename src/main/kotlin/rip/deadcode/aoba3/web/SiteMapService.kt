package rip.deadcode.aoba3.web

import org.springframework.stereotype.Service
import rip.deadcode.aoba3.config.Config
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors.joining

interface SiteMapService {

    /**
     * Generates sitemap xml.
     */
    fun generate(): String
}

@Service
class SiteMapServiceImpl(private val config: Config) : SiteMapService {

    private val base = Paths.get(config.content).toAbsolutePath()
    private val root = if (config.url.endsWith("/")) config.url.dropLast(1) else config.url
    private val reservedFiles = listOf("404.md", "default.md")  // TODO 掃除

    private val sitemap: String

    // TODO 定期的に読み直すようにする
    init {
        sitemap = Files.walk(Paths.get(config.content))
                .filter { it.toString().endsWith(".md") }
                .filter { file -> !reservedFiles.any { file.endsWith(it) } }
                .map { it.toAbsolutePath().toString().substring(base.toString().length) }
                .map { it.replace("\\", "/") }  // Replace "\" to "/" for windows.
                .map { it.dropLast(3) }  // Drop ".md"
                .map { root + it }
                .collect(joining("\n"))
    }

    override fun generate(): String {
        return sitemap
    }

}
