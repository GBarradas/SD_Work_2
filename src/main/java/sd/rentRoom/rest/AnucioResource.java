package sd.rentRoom.rest;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.ws.rs.*;

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
    @Produces({"application/json", "application/xml"})
    public synchronized Request getAnuncios(
            @QueryParam("tipo" ) String tipo
    ){
        List<Anuncio> a = bd.Filtrar("tipo ilike  '"+tipo+"'");
        Request r =new Request( a);
        return r;
    }

    @Path("/addOferta")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized Anuncio addOferta(
            @QueryParam("genero") String genero,
            @QueryParam("zona") String zona,
            @QueryParam("Anunciante") String anunciante,
            @QueryParam("Tipologia") String tipologia,
            @QueryParam("preço") double preco
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

}
