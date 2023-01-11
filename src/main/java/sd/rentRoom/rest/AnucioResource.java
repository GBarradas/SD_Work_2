package sd.rentRoom.rest;


import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.ws.rs.*;
import org.springframework.web.bind.annotation.RequestParam;

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
        ls = bd.Filtrar("tipo islike '" + tipo + "'");
        return ls;
    }

    @Path("/novo")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized int addOferta(
            /*@QueryParam("tipo") String tipo,
            @QueryParam("genero") String genero,
            @QueryParam("zona") String zona,
            @QueryParam("anunciante") String anunciante,
            @QueryParam("tipologia") String tipologia,
            @QueryParam("preco") double preco*/
            @QueryParam("Anuncio") Anuncio a
    ){
        /*
        Anuncio a = new Anuncio();
        a.setGenero(genero);
        a.setZona(zona);
        a.setAnunciante(anunciante);
        a.setTipologia(tipologia);
        a.setPreco(preco);
        a.setData(new Date());
        a.setTipo("oferta");
        */
        int aid = bd.registarAnuncio(a);
        return aid;
    }

    @Path("/listar")
    @GET
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounce(
            @RequestParam(name="tipo",required = false) String tipo,
            @RequestParam(name="descricao",required = false) String descricao,
            @RequestParam(name="zona", required = false) String zona
    ){
        List<Anuncio> a = null;
        String filtros = "";
        if(descricao != null) {
            filtros += "descricao ilike '%" + descricao + "%'";
        }
        if(zona != null){
            if(!filtros.equals(""))
                filtros+= "AND";
            filtros += " zona ilike '%"+zona+"%'";
        }
        if(tipo != null){
            if(!filtros.equals(""))
                filtros+= "AND";
            filtros += "tipo ilike '"+tipo+"'";
        }
        a=bd.Filtrar(filtros);
    return a;
    }

    @Path("/{aid}")
    @GET
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized Anuncio getAnnounce(
            @PathParam("aid") int aid
    ){
        Anuncio a = new Anuncio();
        a = bd.getAnuncio(aid);
        return a;
    }

    @Path("/gestao/listagem")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounce(
            @QueryParam("estado") String estado
    ){
        List<Anuncio> anns = null;
        anns = bd.listaAnunciosEstado(estado);
        return anns;
    }
    @Path("/gestao/alterar")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized String changeAnnounce(
        @QueryParam("aid") int aid,
        @QueryParam("estado") String estado
    ){
        bd.alterarEstado(aid, estado);
        return "ESTADO ALTERADO!";
    }

    @Path("/gestao/aprovar")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized String changeAnnounce(
            @QueryParam("aid") int aid
    ){
        bd.alterarEstado(aid, "ativo");
        return "ANUNCIO APROVADO!";
    }
}
