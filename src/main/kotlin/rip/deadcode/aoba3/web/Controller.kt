package rip.deadcode.aoba3.web

import org.springframework.boot.context.config.ResourceNotFoundException
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import rip.deadcode.aoba3.config.Config
import javax.servlet.http.HttpServletRequest

@Controller
class Controller(
        private val articleService: ArticleService,
        private val siteConfigurationService: SiteConfigurationService,
        private val siteMapService: SiteMapService,
        private val config: Config) {

    private val site = siteConfigurationService.getSetting().site

    /**
     * Internal url that returns static file assets.
     */
    @RequestMapping("/internal")
    fun internal(request: HttpServletRequest): ResponseEntity<Resource> {
        val resource = request.getAttribute("resource") as Resource
        val responseHeaders = HttpHeaders()
        responseHeaders.contentType = request.getAttribute("contentType") as MediaType
        return ResponseEntity(resource, responseHeaders, HttpStatus.OK)
    }

    /**
     * General handler.
     */
    @RequestMapping("/**")
    fun content(request: HttpServletRequest, model: Model): String {

        val url = request.requestURI
        val article = articleService.serve(if (url.isEmpty() || url == "/") "/index" else url)
        if (article.content.isEmpty()) {
            if (article.resource == null) throw ResourceNotFoundException(url, null)

            request.setAttribute("resource", article.resource)
            request.setAttribute("contentType", article.contentType)
            return "forward:/internal"
        }

        model.addAttribute("title", article.title)
        model.addAttribute("site", site)
        model.addAttribute("content", article.content)
        model.addAttribute("breadcrumb", article.breadcrumb)
        model.addAttribute("url", request.requestURI)
        model.addAttribute("production", config.production)
        return "content"
    }

    /**
     * Returns sitemap.
     */
    @RequestMapping("/sitemap.txt")
    fun sitemap(): ResponseEntity<String> {
        return ResponseEntity(siteMapService.generate(), HttpStatus.OK)
    }

    /**
     * Handles 404.
     */
    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun notFound(model: Model): String {

        val article = articleService.serve("/404")

        if (article.content.isEmpty()) {
            return "404"  // Fall back to default 404 page.
        }

        model.addAttribute("title", article.title)
        model.addAttribute("site", site)
        model.addAttribute("content", article.content)
        model.addAttribute("production", config.production)
        return "content"
    }

}
