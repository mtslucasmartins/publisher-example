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
import br.com.lucasmartins.publisher.repositories.NewsRiakRepository;

@Service
public class NewsService {

    private NewsRiakRepository newsRiakRepository;
    private FileSystemService nfsService;

    @Autowired
    public NewsService(
                       NewsRiakRepository newsRiakRepository,
                       FileSystemService nfsService) {
        this.newsRiakRepository = newsRiakRepository;
        this.nfsService = nfsService;
    }

    public List<NewsDTO> listAllNews() throws Exception {
        return newsRiakRepository.fetch() 
                    .stream().map(NewsMapper::fromEntity).collect(Collectors.toList());
    }

    public NewsDTO findByExternalId(String key) throws Exception {
        return NewsMapper.fromEntity(newsRiakRepository.findByKey(key));
    }

    public NewsDTO publish(NewsDTO newsDTO) throws Exception {
        UUID id = UUID.randomUUID();

        News news = NewsMapper.fromDTO(newsDTO);
        news.setId(id);
        news.setExternalId(generateFilename(news.getTitle()));

        newsRiakRepository.store(news);
        nfsService.upsert(news);

        return NewsMapper.fromEntity(news);
    }

    public NewsDTO update(String key, NewsDTO newsDTO) throws Exception {
        News news = newsRiakRepository.findByKey(key);

        news.setId(UUID.fromString(newsDTO.getId()));
        news.setAuthor(newsDTO.getAuthor());
        news.setTitle(newsDTO.getTitle());
        news.setBody(newsDTO.getBody());
        news.setExternalId(key);

        newsRiakRepository.store(news);
        nfsService.upsert(news);

        return NewsMapper.fromEntity(news);
    }

    public boolean delete(String key) throws Exception {
        newsRiakRepository.deleteByKey(key);
        nfsService.delete(key);
    
        return true;
    }

    private String generateFilename(String title) {
        String filename = Normalizer.normalize(title.toLowerCase(), Normalizer.Form.NFD);
        
        filename = filename.replaceAll("\\s", "-"); // replace spaces for hiphens.
        filename = filename.replaceAll("[^a-z0-9-]", ""); // remove non alphanumeric and non hiphens.

        return filename;
    }

}