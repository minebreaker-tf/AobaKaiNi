package rip.deadcode.aoba3.model

/**
 * Configuration for website itself.
 */
data class Setting(

        /**
         * Title shown. Used for HTML title tag.
         */
        val site: String,

        /**
         * Notification header.
         */
        val notification: String
)
