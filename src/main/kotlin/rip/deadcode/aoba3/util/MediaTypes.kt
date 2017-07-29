package rip.deadcode.aoba3.util

import org.springframework.http.MediaType

object MediaTypes {

    fun extToContentType(ext: String): MediaType {
        return when (ext.toLowerCase()) {
            "json" -> MediaType.APPLICATION_JSON
            "htm", "html" -> MediaType.TEXT_HTML
            "xml" -> MediaType.TEXT_XML
            "md" -> MediaType.TEXT_MARKDOWN
            "css" -> MediaType("text", "css")
            "js" -> MediaType("application", "javascript")
            "pdf" -> MediaType.APPLICATION_PDF

            "gif" -> MediaType.IMAGE_GIF
            "jpg", "jpeg" -> MediaType.IMAGE_JPEG
            "png" -> MediaType.IMAGE_PNG
            "bmp" -> MediaType("image", "bmp")

            "wav" -> MediaType("audio", "x-wav")
            "mp3" -> MediaType("audio", "mpeg")

            "zip" -> MediaType("application", "zip")
            else -> MediaType.TEXT_PLAIN
        }
    }

}