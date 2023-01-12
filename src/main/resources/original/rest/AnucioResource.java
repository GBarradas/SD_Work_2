package original.rest;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anuncios")
public class AnucioResource {

    public BDConexao bd;
    public AnucioResource() throws Exception
    {
        bd = new BDConexao();
    }

    //@GET
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    /*public synchronized List<Anuncio> getByTipo(
        @QueryParam("tipo") String tipo
    ){
        List<Anuncio> ls = null;
        ls = bd.Filtrar("tipo ilike '" + tipo + "'");
        return ls;
    }
    */
    //@Path("/novo")
    @PostMapping(value = "/novo")
    //@Consumes({"application/json"})
    //@Produces({"application/json", "application/xml"})
    public synchronized int addOferta(
            /*@QueryParam("tipo") String tipo,
            @QueryParam("genero") String genero,
            @QueryParam("zona") String zona,
            @QueryParam("anunciante") String anunciante,
            @QueryParam("tipologia") String tipologia,
            @QueryParam("preco") double preco*/
            Anuncio a
    ){

       /* Anuncio a = new Anuncio();
        a.setGenero(genero);
        a.setZona(zona);
        a.setAnunciante(anunciante);
        a.setTipologia(tipologia);
        a.setPreco(preco);
        a.setData(new Date());
        a.setTipo("oferta");*/

        int aid = bd.registarAnuncio(a);
        return aid;
    }

    //@Path("/listar")
    @GetMapping(value = "/listar")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounce(
            @RequestParam("tipo") String tipo,
            @RequestParam("descricao") String descricao,
            @RequestParam("zona") String zona
    ){
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
    ){
        Anuncio a = new Anuncio();
        a = bd.getAnuncio(aid);
        System.out.println(a.getEstado());
        if(a.getEstado().equals("inativo"))
            return null;
        return a;
    }
    //@Path("/user")
    @PostMapping("user")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounceUser(
            Anuncio a
    ){
        String filtros = "anunciante ilike '%"+a.getAnunciante()+"%'";
        List<Anuncio> ann;
        ann = bd.Filtrar(filtros);
        return ann;
    }



}
