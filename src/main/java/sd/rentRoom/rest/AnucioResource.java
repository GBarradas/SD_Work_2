package sd.rentRoom.rest;


import jakarta.ws.rs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import sd.rentRoom.rest.Anuncio;

import java.util.Date;
import java.util.List;

@Path(value = "/anuncios")
public class AnucioResource {

    public BDConexao bd;
    public AnucioResource() throws Exception
    {
        bd = new BDConexao();
    }

    @GET
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getByTipo(
        @QueryParam("tipo") String tipo
    ){
        List<Anuncio> ls = null;
        ls = bd.Filtrar("tipo ilike '" + tipo + "'");
        return ls;
    }

    @Path("/novo")
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json", "application/xml"})
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

    @Path("/listar")
    @GET
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounce(
            @QueryParam("tipo") String tipo,
            @QueryParam("descricao") String descricao,
            @QueryParam("zona") String zona
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

    @Path("/{aid}")
    @GET
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized Anuncio getAnnounce(
            @PathParam("aid") int aid
    ){
        Anuncio a = new Anuncio();
        a = bd.getAnuncio(aid);
        System.out.println(a.getEstado());
        if(a.getEstado().equals("inativo"))
            return null;
        return a;
    }
    @Path("/user")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounceUser(
            Anuncio a
    ){
        String filtros = "anunciante ilike '%"+a.getAnunciante()+"%'";
        List<Anuncio> ann;
        ann = bd.Filtrar(filtros);
        return ann;
    }

    @Path("/gestao/listagem")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounce(
           Anuncio a
    ){
        List<Anuncio> anns = null;
        anns = bd.listaAnunciosEstado(a.getEstado());
        return anns;
    }
    @Path("/gestao/alterar")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
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

    @Path("/gestao/aprovar")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized String aproveAnnounce(
             Anuncio a
    ){
        System.out.println(a.getAid());
        bd.alterarEstado(a.getAid(), "ativo");
        return "ANUNCIO APROVADO!";
    }

    @Path("/gestao")
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json", "application/xml"})
    public synchronized Anuncio getAnnounceGestao(
             Anuncio a
    ){
        a = bd.getAnuncio(a.getAid());
        if(a==null)
            return null;
        return a;
    }

}
