package sd.rentRoom.rest;

import jakarta.ws.rs.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Path(value="/msg")
public class MsgResource {

    public BDConexao bd;
    public MsgResource() throws Exception
    {
        bd = new BDConexao();
    }

    @GET
    @Produces({"application/json"})
    public synchronized String function(){
        return "ola";
    }

    @Path("/send")
    @POST
    @Consumes({"application/json", "application/xml"})
    @Produces({"application/json", "application/xml"})
    public synchronized String sendMsg(
            Mensagem msg
    ){
        System.out.println(msg.getAid()+"|"+msg.getRemetente()+"|"+msg.getMsg());
        bd.enviarMensagem(msg.getAid(), msg.getRemetente(), msg.getMsg());
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
