package rip.deadcode.aoba3.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Static configurations.
 */
@Component
@ConfigurationProperties(prefix = "aoba3")
open class Config(var content: String = "")
