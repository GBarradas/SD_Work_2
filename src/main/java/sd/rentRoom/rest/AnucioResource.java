package sd.rentRoom.rest;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
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
    public synchronized Request getAnuncios(
            @QueryParam("tipo" ) String tipo
    ){
        List<Anuncio> a = bd.Filtrar("tipo ilike  '"+tipo+"'");
        Request r =new Request( a);
        return r;
    }

    @Path("/novo")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized Anuncio addOferta(
            @QueryParam("tipo") String tipo,
            @QueryParam("genero") String genero,
            @QueryParam("zona") String zona,
            @QueryParam("anunciante") String anunciante,
            @QueryParam("tipologia") String tipologia,
            @QueryParam("preco") double preco
    ){
        Anuncio a = new Anuncio();
        a.setGenero(genero);
        a.setZona(zona);
        a.setAnunciante(anunciante);
        a.setTipologia(tipologia);
        a.setPreco(preco);
        a.setData(new Date());
        a.setTipo("oferta");
        int aid = bd.registarAnuncio(a);
        a.setAid(aid);
        return a;
    }

    @Path("/listar")
    @GET
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized List<Anuncio> getAnnounce(
            @QueryParam("descricao") String descricao,
            @RequestParam(name="zona", required = false) String zona
    ){
        List<Anuncio> a = null;
        if(zona==null) {
            a = bd.Filtrar("descricao ilike '%" + descricao + "%'");
        } else{
            a = bd.Filtrar("descricao ilike '%" + descricao + "%' AND zona ilike '"+zona+"'");
        }
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
