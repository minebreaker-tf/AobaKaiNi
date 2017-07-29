package rip.deadcode.aoba3.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletRequest

@Controller
class Controller(
        private val articleService: ArticleService,
        private val siteConfigurationService: SiteConfigurationService) {

    @RequestMapping("index.css")
    @ResponseBody
    fun css(): String {
        return articleService.serveRaw("index.css")
    }

    @RequestMapping("index.js")
    @ResponseBody
    fun js(): String {
        return articleService.serveRaw("index.js")
    }

    @RequestMapping("/**")
    fun content(request: HttpServletRequest, model: Model): String {

        val url = request.requestURI
        val article = articleService.serve(if (url.isEmpty() || url == "/") "/index" else url)

        val siteTitle = siteConfigurationService.getSetting().header.title

        model.addAttribute("title", article.title)
        model.addAttribute("site", siteTitle)
        model.addAttribute("content", article.content)
        model.addAttribute("url", request.requestURI)
        return "content"
    }

}
