package rip.deadcode.aoba3.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
class Controller(val contentService: ContentService) {

    @RequestMapping("/admin")
    fun greeting(model: Model): String {
        return "greeting"
    }

    @RequestMapping("/**")
    fun content(request: HttpServletRequest, model: Model): String {

        val url = request.requestURI
        val content = contentService.serve(if (url.isEmpty() || url == "/") "/index" else url)

        model.addAttribute("title", content.title)
        model.addAttribute("content", content.content)
        model.addAttribute("url", request.requestURI)
        return "content"
    }

}