package sd.rentRoom.rest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mensagem")
public class Mensagem implements java.io.Serializable
{
    @XmlElement(required = true)
    private int aid;
    @XmlElement(required = true)
    private String remetente;
    @XmlElement(required = true)
    private String msg;
    @XmlElement(required = true)
    private Date data;


    public Mensagem(int aid, String remetente, String msg,Date data){
        this.aid = aid;
        this.remetente = remetente;
        this.msg = msg;
        this.data = data;
    }
    public Mensagem(){
        this.aid = -1;
        this.remetente = null;
        this.msg = null;
        this.data = null;
    }
    public void setAid (int aid ){
        this.aid = aid;
    }
    public void setRemetente (String remetente){
        this.remetente = remetente;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public void setDate(Date d){
        this.data = d;
    }
    public int getAid (){
        return this.aid;
    }
    public String getRemetente(){
        return this.remetente;
    }
    public String getMsg (){
        return this.msg;
    }
    public Date getData(){
        return this.data;
    }

    @Override
    public String toString(){
        return "-------------------------------------------------------------------------------\n     "
                +remetente+"("+data+"): " +msg+";";
    }
}