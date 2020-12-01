package br.com.lucasmartins.publisher.domain.mappers;

import java.util.UUID;

import br.com.lucasmartins.publisher.domain.dtos.NewsDTO;
import br.com.lucasmartins.publisher.domain.models.News;

public class NewsMapper {
    
    public static News fromDTO(NewsDTO newsDTO) {
        return News.builder()
                    .id(newsDTO.getId() != null ? UUID.fromString(newsDTO.getId()) : null)
                    .author(newsDTO.getAuthor())
                    .title(newsDTO.getTitle())
                    .body(newsDTO.getBody())
                    .externalId(newsDTO.getExternalId())
                .build();
    }


    public static NewsDTO fromEntity(News newsEntity) {
        return NewsDTO.builder()
                    .id(newsEntity.getId().toString())
                    .author(newsEntity.getAuthor())
                    .title(newsEntity.getTitle())
                    .body(newsEntity.getBody())
                    .externalId(newsEntity.getExternalId())
                .build();
    }

}
