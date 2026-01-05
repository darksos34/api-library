package dev.jda.api.library.swagger;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerRedirectController {

    @GetMapping("/")
    public String redirectToSwagger() {
        return "redirect:/api/swagger-ui/index.html";
    }
}
