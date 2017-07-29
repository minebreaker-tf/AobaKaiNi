package rip.deadcode.aoba3.web

import org.springframework.stereotype.Service
import rip.deadcode.aoba3.config.Config
import rip.deadcode.aoba3.model.Setting
import rip.deadcode.aoba3.util.FileReadable
import java.nio.file.Paths

/**
 * Handles global site configuration.
 */
interface SiteConfigurationService {
    fun getSetting(): Setting
}

@Service
class SiteConfigurationServiceImpl(
        private val config: Config
) : SiteConfigurationService, FileReadable {

    private val setting = readSetting()

    override fun getSetting(): Setting {
        return setting
    }

    private fun readSetting(): Setting {
        val path = Paths.get(config.content, "setting.json")
        val json = readJson(path, Setting::class.java)
        return json
    }

}
