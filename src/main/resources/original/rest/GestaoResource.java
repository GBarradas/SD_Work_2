package original.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gestao")
public class GestaoResource {
    public BDConexao bd;
    public GestaoResource() throws Exception
    {
        bd = new BDConexao();
    }
    //@Path("/gestao/listagem")
    @PostMapping("/listagem")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounce(
            Anuncio a
    ){
        List<Anuncio> anns = null;
        anns = bd.listaAnunciosEstado(a.getEstado());
        return anns;
    }
    //@Path("/gestao/alterar")
    @PostMapping("/alterar")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized String changeAnnounce(
            Anuncio a
    ){
        a = bd.getAnuncio(a.getAid());
        if (a.getEstado().equals("ativo"))
            bd.alterarEstado(a.getAid(), "inativo");
        if(a.getEstado().equals("inativo"))
            bd.alterarEstado(a.getAid(), "ativo");
        return "ESTADO ALTERADO!";
    }

    //@Path("/gestao/aprovar")
    @PostMapping("/aprovar")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized String aproveAnnounce(
            Anuncio a
    ){
        System.out.println(a.getAid());
        bd.alterarEstado(a.getAid(), "ativo");
        return "ANUNCIO APROVADO!";
    }
    //@Path("/gestao")
    @PostMapping("/")
    //@Consumes({"application/json"})
    //@Produces({"application/json", "application/xml"})
    public synchronized Anuncio getAnnounceGestao(
            Anuncio a
    ){
        a = bd.getAnuncio(a.getAid());
        if(a==null)
            return null;
        return a;
    }
}
