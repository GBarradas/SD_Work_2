package sd.rentRoom.rest;

import jakarta.ws.rs.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Path(value="/msg")
public class MsgResource {

    @Autowired
    BDConexao bd;

    @GET
    @Produces({"application/json"})
    public synchronized String function(){
        return "ola";
    }

    @Path("/send")
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public synchronized String sendMsg(
            @QueryParam("aid") int aid,
            @QueryParam("name") String name,
            @QueryParam("msg") String msg
    ){
        bd.enviarMensagem(aid, name, msg);
        return "MENSAGEM ENVIADA!";
    }

    @Path("/see")
    @GET
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized List<Mensagem> getMsg(
        @QueryParam("aid") int aid
    ){
        List<Mensagem> msgs = null;
        msgs = bd.getMensagem(aid);
        return msgs;
    }
}
