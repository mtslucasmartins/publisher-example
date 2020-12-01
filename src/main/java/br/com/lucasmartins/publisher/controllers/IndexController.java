package br.com.lucasmartins.publisher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.lucasmartins.publisher.domain.dtos.NewsDTO;
import br.com.lucasmartins.publisher.services.NewsService;

@Controller
public class IndexController {

    private NewsService newsService;

    @Autowired
    public IndexController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public String index(Model model) {
        return "redirect:/noticias";
    }

    @GetMapping("/publish")
    public String publisher(Model model) {
        return "publisher/publisher.html";
    }

    @PostMapping("/publish")
    public ResponseEntity<?> publish(@ModelAttribute NewsDTO newsDTO) throws Exception {
        return ResponseEntity.ok(newsService.publish(newsDTO));
    }

}
