package br.com.lucasmartins.publisher.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.lucasmartins.publisher.domain.models.News;

@Repository
public interface NewsRepository extends JpaRepository<News, UUID> {
    
    @Query("select n from News n where n.externalId = :externalId")
    public News findByExternalId(@Param("externalId") String externalId);

}
