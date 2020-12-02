package br.com.lucasmartins.publisher.services;

import java.io.File;
import java.text.MessageFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import br.com.lucasmartins.publisher.domain.models.News;
import lombok.extern.slf4j.Slf4j;

/**
 * Serviço responsável pelo gerenciamento de arquivos no sistema.
 * 
 */
@Slf4j
@Service
public class FileSystemService {

    private static final String PATH = "/var/lib/data";

    public void upsert(News news) throws Exception {
        try {
            String filename = MessageFormat.format("{0}/{1}.json", PATH, news.getExternalId());

            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(new File(filename), news);
        } catch (Exception ex) {
            log.warn("Something went wrong while serializing object to file system!");
        }
    }

    public void delete(String key) {
        try {
            String filename = MessageFormat.format("{0}/{1}.json", PATH, key);

            new File(filename).delete();
        } catch (Exception ex) {
            log.warn("Something went wrong while serializing object to file system!");
        }
    }
    
}
