
package original.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.text.DecimalFormat;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Anununcios", propOrder = {
        "aid",
        "tipo",
        "estado",
        "genero",
        "zona",
        "anunciante",
        "tipologia",
        "data",
        "preço",
})
public class Anuncio
{
    @XmlElement(required = true)
    private String tipo;
    @XmlElement(required = true)
    private String estado;
    @XmlElement(required = true)
    private String genero;
    @XmlElement(required = true)
    private String zona;
    @XmlElement(required = true)
    private String anunciante;
    @XmlElement(required = true)
    private String tipologia;
    @XmlElement(required = true)
    private int aid;
    @XmlElement(required = false)
    private Date data;
    @XmlElement(required = false)
    private Double preco;

    @XmlElement(required = false)
    private String descricao;

    public Anuncio(String tipo, String estado, String anunciante, double preco, String genero, String zona, int aid, Date data, String tipologia, String descricao){
        this.tipo = tipo;
        this.estado = estado;
        this.anunciante = anunciante;
        this.preco = preco;
        this.genero = genero;
        this.zona = zona;
        this.aid = aid;
        this.data = data;
        this.tipologia = tipologia;
        this.descricao = descricao;
    }
    public Anuncio(){
        this.tipo = null;
        this.estado = null;
        this.anunciante = null;
        this.preco = -1.0;
        this.genero = null;
        this.zona = null;
        this.aid = -1;
        this.data = null;
        this.tipologia = null;
        this.descricao = null;
    }
    public void setAid(int aid){
        this.aid = aid;
    }
    public void setTipo(String t){
        this.tipo = t;
    }
    public void setEstado(String e){
        this.estado = e;
    }
    public void setAnunciante(String n) {
        this.anunciante = n;
    }
    public void setPreco(double p) {
        this.preco = p;
    }
    public void setGenero(String g) {
        this.genero = g;
    }
    public void setZona(String z) {
        this.zona = z;
    }
    public void setData(Date d){
        this.data = d;
    }
    public void setTipologia(String s){
        this.tipologia = s;
    }
    public void setDescricao(String d){
        this.descricao = d;
    }

    public String getTipo() {
        return this.tipo;
    }
    public String getEstado() {
        return this.estado;
    }
    public String getAnunciante() {
        return this.anunciante;
    }
    public double getPreco() {
        return this.preco;
    }
    public String getGenero() {
        return this.genero;
    }
    public String getZona() {
        return this.zona;
    }
    public int getAid() {
        return this.aid;
    }
    public Date getData() {
        return this.data;
    }
    public String getTipologia(){
        return this.tipologia;
    }
    public String getDescricao(){
        return this.descricao;
    }

    @Override
    public String toString(){
        DecimalFormat df = new DecimalFormat("#,##0.00€");

        String p = "-------------------------------------------------------\n" +
                "\t Aid: "+ aid + "\n"+
                "\t Tipo: " + tipo + "\n"+
                "\t Estado: "+ estado + "\n"+
                "\t Genero: " + genero+   "\n"+
                "\t Zona: " + zona + "\n"+
                "\t Anunciante:  "+ anunciante + "\n"+
                "\t Tipologia: "+ tipologia + "\n"+
                "\t Data: " + data.toString() + "\n"+
                "\t Preço: "+ df.format(preco) + "\n";

        return p;
    }
}