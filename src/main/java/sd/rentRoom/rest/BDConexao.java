/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sd.rentRoom.rest;

import org.jvnet.hk2.annotations.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.sql.*;

/**
 *
 * @author barradas e baião
 */
public class BDConexao {
    private String dbUrl;
    private String dbName;
    private String dbUser;
    private String dbPass;
    Connection con = null;

    public BDConexao() throws IOException
    {
        Properties p = new Properties();
        InputStream fr = new FileInputStream("src/main/resources/application.properties");
        p.load(fr);
        // obter informações sobre a base de dados
        dbUrl = p.getProperty("bd.url");
        dbName = p.getProperty("bd.name","");
        dbUser = p.getProperty("bd.user","");
        dbPass = p.getProperty("bd.pass","");
        try {
            // Conectar á base de dados
            Class.forName("org.postgresql.Driver");
            // url = "jdbc:postgresql://host:port/database",
            con = DriverManager.getConnection("jdbc:postgresql://" + dbUrl + ":5432/" + dbName,
                    dbUser,
                    dbPass);

            stmt = con.createStatement();
            System.out.println("Conexão bem Sucecedida!!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems setting the connection");
        }
    }
    public BDConexao bd(){
        return this;
    }

    Statement stmt = null;

    public void disconnect() {    // fechar a ligacao á base de dados
        try {
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int registarAnuncio(Anuncio a){      //registar anuncio na bd
        try{
            stmt.executeUpdate("insert into anuncios(tipo, estado, anunciante, preco, genero, zona, data, tipologia,descricao) values('"+
                    a.getTipo()+"', '"+
                    "inativo', '"+
                    a.getAnunciante()+"', '"+
                    a.getPreco()+"', '"+
                    a.getGenero()+"', '"+
                    a.getZona()+"', '"+
                    new Date() +"', '"+
                    a.getTipologia()+"', '"+
                    a.getDescricao()+ "')");
            //obter o id com que o anuncio ficou
            ResultSet rs = stmt.executeQuery("select max(aid) as id from anuncios;");
            while(rs.next()){
                return rs.getInt("id");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    public List<Anuncio> Filtrar (String filtros){      //obter os anuncios aplicando os respetivos filtros
        List<Anuncio> resultado = new ArrayList<Anuncio>();
        filtros =filtros.replace("&", " and ")+";";
        System.out.println(filtros);
        try{
            ResultSet rs = stmt.executeQuery("select * from anuncios where estado = 'ativo' and "+filtros);
            while (rs.next()){
                Anuncio a = new Anuncio();
                a.setAid(rs.getInt("aid"));
                a.setZona(rs.getString("zona"));
                a.setPreco(rs.getInt("preco"));
                a.setGenero(rs.getString("genero"));
                a.setData(rs.getDate("data"));
                a.setAnunciante(rs.getString("anunciante"));
                a.setTipologia(rs.getString("tipologia"));
                a.setTipo(rs.getString("tipo"));
                a.setEstado(rs.getString("estado"));
                a.setDescricao(rs.getString("descricao"));
                resultado.add(a);
            }
            return resultado;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public List<Anuncio> AnunciosUser(String Anunciante){   //obter anuncios de um anunciante
        List<Anuncio> resultado = new ArrayList<Anuncio>();
        String  filtros = "anunciante = '"+Anunciante+"'";
        try{
            ResultSet rs = stmt.executeQuery("select * from anuncios where "+filtros+ "order by aid");
            while (rs.next()){
                Anuncio a = new Anuncio();
                a.setAid(rs.getInt("aid"));
                a.setZona(rs.getString("zona"));
                a.setPreco(rs.getInt("preco"));
                a.setGenero(rs.getString("genero"));
                a.setData(rs.getDate("data"));
                a.setAnunciante(rs.getString("anunciante"));
                a.setTipologia(rs.getString("tipologia"));
                a.setTipo(rs.getString("tipo"));
                a.setEstado(rs.getString("estado"));
                a.setDescricao(rs.getString("descricao"));
                resultado.add(a);
            }
            return resultado;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Anuncio getAnuncio(int aid){     //obter anuncio pelo id do anuncio
        try{
            ResultSet rs = stmt.executeQuery("select * from anuncios where aid = "+aid);
            if(rs.next()){
                Anuncio a = new Anuncio();
                a.setAid(rs.getInt("aid"));
                a.setZona(rs.getString("zona"));
                a.setPreco(rs.getInt("preco"));
                a.setGenero(rs.getString("genero"));
                a.setData(rs.getDate("data"));
                a.setAnunciante(rs.getString("anunciante"));
                a.setTipologia(rs.getString("tipologia"));
                a.setTipo(rs.getString("tipo"));
                a.setEstado(rs.getString("estado"));
                a.setDescricao(rs.getString("descricao"));

                return a;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void enviarMensagem(int aid, String remetente, String msg){  //registar uma mensagem para um determinado anuncio
        try{
            stmt.executeUpdate("insert into mensagens values('"+msg+"','"+remetente+"','"+aid+"', '"+new Date()+"')");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public List<Mensagem> getMensagem(int aid){     //obter mensagens enviadas para um anuuncio
        List<Mensagem> resultado = new ArrayList<Mensagem>();
        try{
            ResultSet rs = stmt.executeQuery("select * from mensagens where aid = "+aid);
            while(rs.next()){
                Mensagem m = new Mensagem();
                m.setAid(rs.getInt("aid"));
                m.setMsg(rs.getString("msg"));
                m.setRemetente(rs.getString("remetente"));
                m.setDate(rs.getDate("data"));
                resultado.add(m);
            }
            return resultado;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public List<Anuncio> listaAnunciosEstado(String  estado){   //obter todos os anuncios de um determinado anuncio
        List<Anuncio> resultado = new ArrayList<Anuncio>();
        String  query = "select * from anuncios where estado = '"+ estado+"'";
        System.out.println(query);
        try{
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                Anuncio a = new Anuncio();
                a.setAid(rs.getInt("aid"));
                a.setZona(rs.getString("zona"));
                a.setPreco(rs.getInt("preco"));
                a.setGenero(rs.getString("genero"));
                a.setData(rs.getDate("data"));
                a.setAnunciante(rs.getString("anunciante"));
                a.setTipologia(rs.getString("tipologia"));
                a.setTipo(rs.getString("tipo"));
                a.setEstado(rs.getString("estado"));
                a.setDescricao(rs.getString("descricao"));
                resultado.add(a);
            }
            return resultado;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void alterarEstado(int aid, String estado){  //alterar o estado de um anuncio para o estado necessario
        try{
            stmt.executeUpdate("update anuncios set estado ='"+estado+"' where aid = "+aid);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}