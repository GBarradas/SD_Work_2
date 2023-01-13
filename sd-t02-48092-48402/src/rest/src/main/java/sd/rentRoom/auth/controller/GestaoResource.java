package sd.rentRoom.auth.controller;

import org.springframework.web.bind.annotation.*;
import sd.rentRoom.auth.dto.Anuncio;

import java.util.List;

@RestController
@RequestMapping("/gestao")
public class GestaoResource {
    public BDConexao bd;
    public GestaoResource() throws Exception
    {
        bd = new BDConexao();
    }

    //@Path("/gestao")
    @PostMapping()
    //@Consumes({"application/json"})
    //@Produces({"application/json", "application/xml"})
    public synchronized Anuncio getAnnounceGestao(
            @RequestBody Anuncio a
    ){
        a = bd.getAnuncio(a.getAid());
        if(a==null)
            return null;
        return a;
    }
    @PostMapping("/listagem")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounce(
           @RequestBody Anuncio a
    ){
        List<Anuncio> anns = null;
        anns = bd.listaAnunciosEstado(a.getEstado());
        return anns;
    }

    @PostMapping("/alterar")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized String changeAnnounce(
           @RequestBody Anuncio a
    ){
        a = bd.getAnuncio(a.getAid());
        if(a==null)
            return null;
        if (a.getEstado().equals("ativo"))
            bd.alterarEstado(a.getAid(), "inativo");
        if(a.getEstado().equals("inativo"))
            bd.alterarEstado(a.getAid(), "ativo");
        return "ESTADO ALTERADO!";
    }
    @PostMapping("/aprovar")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized String aproveAnnounce(
            @RequestBody Anuncio a
    ){
        System.out.println(a.getAid());
        bd.alterarEstado(a.getAid(), "ativo");
        return "ANUNCIO APROVADO!";
    }
}
