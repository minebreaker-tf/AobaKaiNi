package rip.deadcode.aoba3.model

/**
 * Configuration for website itself.
 */
data class Setting(val header: Header)

/**
 * Header component.
 */
data class Header(
        /**
         * Title shown. Used for HTML title tag.
         */
        val title: String
)
