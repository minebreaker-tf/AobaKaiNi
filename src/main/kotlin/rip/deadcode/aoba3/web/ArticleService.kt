package rip.deadcode.aoba3.web

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import rip.deadcode.aoba3.config.Config
import rip.deadcode.aoba3.model.PageSetting
import rip.deadcode.aoba3.util.FileReadable
import rip.deadcode.aoba3.util.Strings2
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Find and serve article data.
 */
interface ArticleService {
    /**
     * Find and serve article data.
     *
     * @param pathParam Path parameter to find the article from file system.
     */
    fun serve(pathParam: String): Article

    fun serveRaw(pathParam: String): String
}

@Service
class ArticleServiceImpl(
        private val config: Config
) : ArticleService, FileReadable {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val base = Paths.get(config.content).toAbsolutePath()

    override fun serve(pathParam: String): Article {

        logger.info("PathParam: {}", pathParam)

        val path = Strings2.dropFirst(pathParam, "/")

        val settingPath = base.resolve(path + ".json")
        val contentPath = base.resolve(path + ".md")
        checkSafePath(contentPath)
        logger.info("Read content: {}", contentPath.toAbsolutePath().toString())
        if (!Files.exists(settingPath) || !Files.exists(contentPath)) {
            return Article("", "")
        }

        // TODO Use cache
        val setting = readJson(settingPath, PageSetting::class.java)
        val content = readMarkdown(contentPath)

        logger.debug("Setting: {} Content: {}", setting, content)

        return Article(content, setting.title)
    }

    override fun serveRaw(pathParam: String): String {
        val path = Strings2.dropFirst(pathParam, "/")
        val contentPath = base.resolve(path)
        logger.info("Read content: {}", contentPath.toAbsolutePath().toString())
        return read(contentPath)
    }

    private fun checkSafePath(path: Path) {
        val isSafe = path.toAbsolutePath().toString().contains(base.toString(), true)
        if (!isSafe) {
            throw RuntimeException("Unsafe path: " + path)
        }
    }

}

data class Article(val content: String, val title: String)
