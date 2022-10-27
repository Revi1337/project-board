package com.fastcampus.projectboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// ArticleControllerTest 참조
// /articles   (o)
// /articles/{article-id} (0)
// /articles/search (0)
// /articles/search-hashtag (0)

@RequestMapping("/articles")
@Controller
public class ArticleController {

    @GetMapping
    public String atricles(ModelMap map) {
        map.addAttribute("articles", List.of()); // Model attr 추가

        return "articles/index";
    }
}
