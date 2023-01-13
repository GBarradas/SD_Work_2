package sd.rentRoom.auth.controller;

import org.springframework.web.bind.annotation.*;
import sd.rentRoom.auth.dto.Mensagem;
import sd.rentRoom.auth.mqtt.Publisher;

import java.util.List;

@RestController
@RequestMapping("/msg")
public class MsgResource {

    public BDConexao bd;
    private Publisher publisher;
    public MsgResource() throws Exception
    {

        bd = new BDConexao();
        publisher = new Publisher();
    }

    @GetMapping()
    //@Produces({"application/json"})
    public synchronized String function(){
        return "ola";
    }

    //@Path("/send")
    @PostMapping("/send")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized String sendMsg(
            @RequestBody Mensagem msg
    ) throws Exception {
        System.out.println(msg.getAid()+"|"+msg.getRemetente()+"|"+msg.getMsg());
        bd.enviarMensagem(msg.getAid(), msg.getRemetente(), msg.getMsg());
        publisher.sendNotification(("anuncio"+msg.getAid()),("Recebeu um mensagem no anuncio "+msg.getAid()));
        return "MENSAGEM ENVIADA!";
    }

    //@Path("/see")
    @GetMapping("/see")
    //@Consumes({"application/json", "application/xml"})
    //@Produces({"application/json", "application/xml"})
    public synchronized List<Mensagem> getMsg(
        @RequestParam("aid") int aid
    ){
        List<Mensagem> msgs = null;
        msgs = bd.getMensagem(aid);
        return msgs;
    }
}
