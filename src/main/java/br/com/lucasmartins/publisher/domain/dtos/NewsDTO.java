package br.com.lucasmartins.publisher.domain.dtos;

import java.io.Serializable;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DataTransferObject(type="bean", javascript="news")
public class NewsDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;

    private String title;

    private String author;

    private String body;

    private String externalId;

    @RemoteProperty
    public String getId() {
        return this.id;
    }

    @RemoteProperty
    public String getTitle() {
        return this.title;
    }

    @RemoteProperty
    public String getAuthor() {
        return this.author;
    }

    @RemoteProperty
    public String getBody() {
        return this.body;
    }

    @RemoteProperty
    public String getExternalId() {
        return this.externalId;
    }

}