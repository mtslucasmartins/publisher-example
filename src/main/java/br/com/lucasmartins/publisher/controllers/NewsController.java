package br.com.lucasmartins.publisher.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.lucasmartins.publisher.domain.dtos.NewsDTO;
import br.com.lucasmartins.publisher.services.NewsService;

@Controller
@RequestMapping("/noticias")
public class NewsController {

    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public String index(Model model) {
        return "noticias/noticias.html";
    }

    @GetMapping("/publicar")
    public String publish(Model model) {
        return "noticias/publicar-noticia.html";
    }

    @PostMapping("/publicar")
    // TODO: save as a file and save to riak 
    public String publish(@ModelAttribute NewsDTO newsDTO) throws Exception {
        try {
            newsDTO = newsService.publish(newsDTO);
            return "redirect:/noticias/" + newsDTO.getExternalId();
        } catch (Exception ex) {
            return "noticias/noticias.html";
        }
    }

    @GetMapping("/{externalId}")
    // fetch from riak 
    public String find(@PathVariable("externalId") String externalId, Model model) {
        return "noticias/noticia.html";
    }

    @GetMapping("/{externalId}/editar")
    public String edit(@PathVariable("externalId") String externalId, Model model) {
        return "noticias/editar-noticia.html";
    }

    @PostMapping("/{externalId}/excluir")
    public String delete(@PathVariable("externalId") String externalId, Model model) {
        try {
            newsService.delete(externalId);
            return "redirect:/noticias";
        } catch (Exception ex) {
            return "noticias/editar-noticia.html";
        }
    }

    @PostMapping("/{externalId}/editar")
    public String edit(@PathVariable("externalId") String externalId, @ModelAttribute NewsDTO newsDTO, Model model) {
        try {
            newsDTO = newsService.update(externalId, newsDTO);
            return "redirect:/noticias/" + externalId;
        } catch (Exception ex) {
            return "noticias/editar-noticia.html";
        }
    }

    
}