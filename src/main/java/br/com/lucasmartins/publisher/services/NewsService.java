package br.com.lucasmartins.publisher.services;

import java.text.Normalizer;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.lucasmartins.publisher.domain.dtos.NewsDTO;
import br.com.lucasmartins.publisher.domain.mappers.NewsMapper;
import br.com.lucasmartins.publisher.domain.models.News;
import br.com.lucasmartins.publisher.repositories.NewsRepository;
import br.com.lucasmartins.publisher.repositories.NewsRiakRepository;

@Service
public class NewsService {

    private NewsRepository newsRepository;
    private NewsRiakRepository newsRiakRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository, NewsRiakRepository newsRiakRepository) {
        this.newsRepository = newsRepository;
        this.newsRiakRepository = newsRiakRepository;
    }

    public List<NewsDTO> list() throws Exception {
        return newsRepository.findAll() 
                    .stream().map(NewsMapper::fromEntity).collect(Collectors.toList());
    }

    public NewsDTO findByExternalId(String externalId) throws Exception {
        return NewsMapper.fromEntity(newsRepository.findByExternalId(externalId));
    }

    public NewsDTO publish(NewsDTO newsDTO) throws Exception {
        UUID id = UUID.randomUUID();

        News news = NewsMapper.fromDTO(newsDTO);
        news.setId(id);
        news.setExternalId(generateFilename(news.getTitle()));

        newsRiakRepository.store(news);

        return NewsMapper.fromEntity(newsRepository.save(news));
    }

    public NewsDTO update(String externalId, NewsDTO newsDTO) throws Exception {
        News news = newsRepository.findByExternalId(externalId);

        news.setId(UUID.fromString(newsDTO.getId()));
        news.setAuthor(newsDTO.getAuthor());
        news.setTitle(newsDTO.getTitle());
        news.setBody(newsDTO.getBody());

        return NewsMapper.fromEntity(newsRepository.save(news));
    }

    public boolean delete(String externalId) throws Exception {
        News news = newsRepository.findByExternalId(externalId);

        newsRepository.deleteById(news.getId());
    
        return true;
    }

    private String generateFilename(String title) {
        String filename = Normalizer.normalize(title.toLowerCase(), Normalizer.Form.NFD);
        
        filename = filename.replaceAll("\\s", "-"); // replace spaces for hiphens.
        filename = filename.replaceAll("[^a-z0-9-]", ""); // remove non alphanumeric and non hiphens.

        return filename;
    }

}