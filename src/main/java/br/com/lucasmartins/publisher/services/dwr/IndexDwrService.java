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
        return this.newsService.list();
    }

    @RemoteMethod
    public NewsDTO findByExternalId(String externalId) throws Exception {
        return this.newsService.findByExternalId(externalId);
    }

    // private List<NewsDTO> fakenews() throws Exception {
    //     //
    //     List<NewsDTO> news = new ArrayList<NewsDTO>();

    //     String body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean ac nibh urna. Vivamus iaculis at felis id fringilla. Aliquam sit amet imperdiet odio. Fusce diam tortor, suscipit in pulvinar quis, gravida sed nulla.";

    //     NewsDTO n1 = new NewsDTO();
    //     n1.setAuthor("Alice Cunha");
    //     n1.setTitle("Blogueiro terraplanista é abduzido por extraterrestres em forró rala-coxa");
    //     n1.setBody(body);

    //     NewsDTO n2 = new NewsDTO();
    //     n2.setAuthor("André Ricardo");
    //     n2.setTitle("Boneco de posto cartomante trafica órgãos ao vivo na TV ");
    //     n2.setBody(body);

    //     NewsDTO n3 = new NewsDTO();
    //     n3.setAuthor("Marcelo Fernandes");
    //     n3.setTitle("Ministro astrólogo toca tambor com Ronaldinho Gaúcho na orla carioca");
    //     n3.setBody(body);

    //     NewsDTO n4 = new NewsDTO();
    //     n4.setAuthor("João Vasconcellos");
    //     n4.setTitle("Juiz vestido de palhaço é preso em flagrante no Senado");
    //     n4.setBody(body);
        
    //     //
    //     news.addAll(this.newsService.list());

    //     news.add(n1);
    //     news.add(n2);
    //     news.add(n3);
    //     news.add(n4);

    //     return news;
    // }
 
}
