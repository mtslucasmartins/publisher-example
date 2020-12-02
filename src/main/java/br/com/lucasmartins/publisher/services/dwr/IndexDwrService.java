package br.com.lucasmartins.publisher.services.dwr;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.lucasmartins.publisher.domain.dtos.NewsDTO;
import br.com.lucasmartins.publisher.services.NewsService;

@Service
@RemoteProxy(name = "IndexDwrService")
public class IndexDwrService {
    
    private NewsService newsService;

    @Autowired
    public IndexDwrService(NewsService newsService) {
        this.newsService = newsService;
    }

    @RemoteMethod
    public List<NewsDTO> listNews() throws Exception {
        return this.newsService.listAllNews();
    }

    @RemoteMethod
    public NewsDTO findByExternalId(String externalId) throws Exception {
        return this.newsService.findByExternalId(externalId);
    }

}
