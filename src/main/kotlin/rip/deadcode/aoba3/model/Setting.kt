package rip.deadcode.aoba3.model

/**
 * Configuration for website itself.
 */
data class Setting(

        /**
         * Site title shown. Used for HTML title tag.
         */
        val site: String,

        /**
         * Notification header.
         */
        val notification: String,

        /**
         * Breadcrumb setting.
         */
        val breadcrumb: Breadcrumb
)

/**
 * Configuration for Breadcrumb.
 */
data class Breadcrumb(

        /**
         * Splitter String of each path element.
         */
        val splitter: String
)
