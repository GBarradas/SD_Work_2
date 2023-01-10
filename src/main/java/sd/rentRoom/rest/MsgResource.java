package sd.rentRoom.rest;

import jakarta.ws.rs.*;

import java.util.List;

@Path(value="/sentMsg")
public class MsgResource {

    BDConexao bd;

    public MsgResource() throws Exception
    {
        bd = new BDConexao();
    }

    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized String sendMsg(
            int aid,
            String name,
            @QueryParam("msg") String msg
    ){
        bd.enviarMensagem(aid, name, msg);
        return "MENSAGEM ENVIADA!";
    }

    @Path("/seeMsgs")
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
