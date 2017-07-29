package rip.deadcode.aoba3.web

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import rip.deadcode.aoba3.config.Config
import rip.deadcode.aoba3.model.PageSetting
import rip.deadcode.aoba3.util.FileReadable
import java.nio.file.Files
import java.nio.file.Paths

interface ContentService {
    fun serve(pathParam: String): Content
}

@Service
class ContentServiceImpl(
        val config: Config
) : ContentService, FileReadable {

    val logger = LoggerFactory.getLogger(this::class.java)
    val base = Paths.get(config.content)

    override fun serve(pathParam: String): Content {

        logger.info("PathParam: {}", pathParam)

        val path = if (pathParam.startsWith("/"))
            pathParam.substring(1, pathParam.length) else pathParam

        val settingPath = base.resolve(path + ".json")
        val contentPath = base.resolve(path + ".md")
        logger.info("Read content: {}", contentPath.toAbsolutePath().toString())
        if (!Files.exists(settingPath) || !Files.exists(contentPath)) {
            return Content("", "")
        }

        // TODO Use cache
        val setting = readSetting(settingPath, PageSetting::class.java)
        val content = readMarkdown(contentPath)

        logger.debug("Setting: {} Content: {}", setting, content)

        return Content(content, setting.title)
    }

}

data class Content(val content: String, val title: String)
