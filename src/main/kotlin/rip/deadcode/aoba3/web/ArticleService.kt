package rip.deadcode.aoba3.web

import com.google.common.cache.CacheBuilder
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import rip.deadcode.aoba3.config.Config
import rip.deadcode.aoba3.model.PageSetting
import rip.deadcode.aoba3.util.FileReadable
import rip.deadcode.aoba3.util.MediaTypes
import rip.deadcode.aoba3.util.Strings2
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

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

}

@Service
class ArticleServiceImpl(
        private val config: Config
) : ArticleService, FileReadable {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val base = Paths.get(config.content).toAbsolutePath()
    private val withExt = Regex("^.*\\.(?<ext>[a-zA-Z0-9]+)$")

    // TODO move to configuration
    private val settingCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build<Path, PageSetting>()
    private val contentCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build<Path, String>()

    override fun serve(pathParam: String): Article {

        logger.debug("PathParam: {}", pathParam)

        val path = Strings2.dropFirst(pathParam, "/")
        if (withExt.matches(path)) {
            val resourceFile = base.resolve(path)
            if (Files.exists(resourceFile)) {
                val ext = withExt.matchEntire(path)!!.groups["ext"]!!.value
                logger.debug("Ext: {}", ext)
                return Article("", "", FileSystemResource(resourceFile.toFile()), MediaTypes.extToContentType(ext))
            } else {
                return Article("", "")
            }
        }

        val settingPath = base.resolve(path + ".json")
        val contentPath = base.resolve(path + ".md")
        checkSafePath(contentPath)
        logger.debug("Requested content: {}", contentPath.toAbsolutePath().toString())
        if (!Files.exists(settingPath) || !Files.exists(contentPath)) {
            return Article("", "")
        }

        val setting = settingCache.get(settingPath, {
            logger.info("read!")
            readJson(settingPath, PageSetting::class.java)
        })
        val content = contentCache.get(contentPath, { readMarkdown(contentPath) })

        logger.debug("Setting: {} Content: {}", setting, content)

        return Article(content, setting.title)
    }

    private fun checkSafePath(path: Path) {
        val isSafe = path.toAbsolutePath().normalize().toString().contains(base.toString(), true)
        if (!isSafe) {
            throw RuntimeException("Unsafe path: " + path)
        }
    }

}

data class Article(val content: String, val title: String, val resource: Resource? = null, val contentType: MediaType? = null)
