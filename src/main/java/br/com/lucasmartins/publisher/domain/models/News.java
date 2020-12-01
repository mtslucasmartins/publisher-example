package br.com.lucasmartins.publisher.domain.models;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
public class News implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Column
    private String title;

    @Column
    private String author;

    @Column
    private String body;

    @Column
    private String externalId;

    @PrePersist
    @PreUpdate
    public void prePersit() {
        this.externalId = Normalizer.normalize(this.title.toLowerCase(), Normalizer.Form.NFD);
        this.externalId = this.externalId.replaceAll("\\s", "-").replaceAll("[^a-z0-9-]", "");
    }


    // private LocalDateTime createdAt;

    // private LocalDateTime updatedAt;

}
