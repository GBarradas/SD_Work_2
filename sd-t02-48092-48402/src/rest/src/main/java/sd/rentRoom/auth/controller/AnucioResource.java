package sd.rentRoom.auth.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sd.rentRoom.auth.dto.Anuncio;

import java.util.List;

@RestController
@RequestMapping("/anuncios")
public class AnucioResource {

    public BDConexao bd;
    public AnucioResource() throws Exception
    {
        bd = new BDConexao();
    }
    public void getBDConect() throws Exception
    {
        this.bd = new BDConexao();
    }

    //@GET
    @GetMapping
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getByTipo(
        @RequestParam("tipo") String tipo
    ){
        List<Anuncio> ls = null;
        ls = bd.Filtrar("tipo ilike '" + tipo + "'");
        return ls;
    }
    //@Path("/novo")
    @PostMapping(value = "/novo",consumes ="application/json" )
    //@Consumes({"application/json"})
    //@Produces({"application/json", "application/xml"})
    public synchronized int addOferta(

           @RequestBody Anuncio a
    ){


        int aid = bd.registarAnuncio(a);
        return aid;
    }

    //@Path("/listar")
    @GetMapping(value = "/listar")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounce(
            @RequestParam(value = "tipo",required = false) String tipo,
            @RequestParam(value = "descricao",required = false) String descricao,
            @RequestParam(value = "zona",required = false) String zona
    ) throws Exception {
        getBDConect();
        System.out.println("LISTA ANUNCIOS");
        List<Anuncio> a = null;
        String filtros = "";
        if(descricao != null) {
            filtros += "descricao ilike '%" + descricao + "%'";
        }
        if(zona != null){
            if(!filtros.equals(""))
                filtros+= " AND ";
            filtros += " zona ilike '%"+zona+"%'";
        }
        if(tipo != null){
            if(!filtros.equals(""))
                filtros+= " AND ";
            filtros += "tipo ilike '"+tipo+"'";
        }
        if(tipo == null && zona == null && descricao == null)
            return null;
        System.out.println(filtros);
        a=bd.Filtrar(filtros);
    return a;
    }
    /*
    @Path("/user@{user}")
    @GET
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounceProprio(
            @PathParam("user") String user
    ){
        System.out.println("LISTA ANUNCIOS");
        List<Anuncio> a = null;
        String filtros = "anunciante ilike ´"+user+"'";

        System.out.println(filtros);
        a=bd.Filtrar(filtros);
        return a;
    }*/

    //@Path("/{aid}")
    @GetMapping("/{aid}")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized Anuncio getAnnounce(
            @PathVariable("aid") int aid
    ) throws Exception {
        getBDConect();

        Anuncio a = new Anuncio();
        a = bd.getAnuncio(aid);

        if(a == null){
            return null;
        }
        else if(a.getEstado().equals("inativo"))
            return null;
        return a;
    }
    //@Path("/user")
    @PostMapping("user")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounceUser(
            @RequestBody Anuncio a
    ) throws Exception {
        getBDConect();

        String filtros = "anunciante ilike '%"+a.getAnunciante()+"%'";
        List<Anuncio> ann;
        ann = bd.Filtrar(filtros);
        return ann;
    }



}
