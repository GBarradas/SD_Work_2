package sd.rentRoom.client;


import org.json.JSONArray;
import org.json.JSONObject;
import sd.rentRoom.rest.Anuncio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author barradas e baião
 */
public class ClienteGestao {

    static final String URI = "http://localhost:8080";
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] Args) throws IOException
    {
        String host = "localhost";
        String port = "9000";
        if(Args.length != 2){
            System.out.println("ERRO ARGUMENTOS: <Host> <port>");
            System.exit(1);
        }
        host = Args[0];
        port = Args[1];


            menu();
    }
    //menu que mostra todas as opções de  que o utilizador pode fazer
    public static void menu() throws IOException
    {
        try {
            System.out.print(
                             "---------------------------------\n"+
                             "Escolha a funcionalidade:\n"+
                             "1 - Listar anuncios por estado\n"+
                             "2 - Obter detalhes anuncio\n"+
                             "3 - aprovar anuncio\n"+
                             "4 - alterar estado de um anuncio\n"+
                             "5 - Sair\n\n"+

                             "Opcção: ");
            int escolha = Integer.parseInt(br.readLine());
            switch (escolha){
                case 1:
                    listarAnuncios();
                    break;
                case 2:
                    verAnuncio();
                    break;
                case 3:
                    AprovarAnuncio();
                    break;
                case 4:
                    alterarEstadoAnuncio();
                    break;
                case 5:
                    System.out.println("ADEUS !!");
                    System.exit(1);
                    break;
                default:
                    System.err.println("OPÇÃO INVALIDA");
                    menu();
                    break;
            }
        }
        catch(Exception e){
            System.err.println("OPÇÃO INVALIDA");
            menu();
        }
    }
    // lista os anuncios pelo estado desejado
    public static void listarAnuncios() throws IOException {
        while (true) {
            System.out.print("Escolha um estado (inativo ou ativo):");
            String estado = br.readLine();
            HttpURLConnection cnt = (HttpURLConnection) new URL(
                    URI+"/anuncios"+"/gestao/listagem").openConnection();
            cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            cnt.setRequestProperty("Accept", "application/json");
            cnt.setRequestMethod("POST");
            cnt.setDoOutput(true);
            String obj = "{\"estado\":\""+estado+"\"}";
            if (estado.toLowerCase().equals("inativo")) {

                BufferedReader l = new BufferedReader(new InputStreamReader(cnt.getInputStream()));
                String r = l.readLine();
                JSONArray resultados = new JSONArray(r);
                // PRINTAR OS ANUNCIOOS
                break;
            } else if (estado.toLowerCase().equals("ativo")) {

                BufferedReader l = new BufferedReader(new InputStreamReader(cnt.getInputStream()));
                String r = l.readLine();
                JSONArray resultados = new JSONArray(r);
                // PRINTAR OS ANUNCIOOS
                break;
            } else {
                System.err.println("INPUT INVALIDO");
            }
        }
        menu();
    }
    // obter um anuncio pelo aid
    public static void verAnuncio() throws IOException
    {
        int aid;
        while(true){
            try{
                System.out.print("Indique o ID do Anuncio: ");
                aid = Integer.parseInt(br.readLine());
                break;
            }
            catch (Exception e){
                System.err.println("INPUT INVALIDO!");
            }
        }
        HttpURLConnection cnt = (HttpURLConnection) new URL(
                URI+"/anuncios"+"/gestao/alterar").openConnection();
        cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        cnt.setRequestProperty("Accept", "application/json");
        cnt.setRequestMethod("POST");
        cnt.setDoOutput(true);
        if(a == null){
            System.err.println("ANUNCIO NÂO ENCONTRADO!");
            menu();
        }
        System.out.println(a);
        menu();
    }
    //Altera o estado de um anuncio para ativo
    public static void AprovarAnuncio() throws IOException
    {
        int aid;
        while(true){
            try{
                System.out.print("Indique o ID do Anuncio: ");
                aid = Integer.parseInt(br.readLine());
                break;
            }
            catch (Exception e){
                System.err.println("INPUT INVALIDO!");
            }
        }
        obj.alterarEstado(aid, "ativo");
        menu();
    }
    // altera o estado de um anuncio de ativo para inativo ou vs versa
    public static void alterarEstadoAnuncio() throws IOException
    {
        int aid;
        while(true){
            try{
                System.out.print("Indique o ID do Anuncio: ");
                aid = Integer.parseInt(br.readLine());
                break;
            }
            catch (Exception e){
                System.err.println("INPUT INVALIDO!");
            }
        }
        String estado = obj.getAnuncio(aid).getEstado().toLowerCase();
        if(estado.equals("ativo")){
            obj.alterarEstado(aid, "inativo");
        }
        else if(estado.equals("inativo")){
            obj.alterarEstado(aid, "ativo");
        }
        else{
            System.err.println("ANUNCIO NÃO ENCONTRADO");
            menu();
        }
        menu();
    }

    public static void printAnuncio(JSONObject obj) throws java.text.ParseException {
        String dateStr = obj.getString("birthdate");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = sdf.parse(dateStr);
        DecimalFormat df = new DecimalFormat("#,##0.00€");

        System.out.println(
                "-------------------------------------------------------\n" +
                        "\t Aid: "+ obj.getInt("aid") + "\n"+
                        "\t Tipo: " + obj.getString("tipo") + "\n"+
                        "\t Estado: "+ obj.getString("estado") + "\n"+
                        "\t Genero: " + obj.getString("genero") +   "\n"+
                        "\t Zona: " + obj.getString("zona") + "\n"+
                        "\t Anunciante:  "+ obj.getString("anunciante") + "\n"+
                        "\t Tipologia: "+ obj.getString("tipologia") + "\n"+
                        "\t Data: " + date.toString() + "\n"+
                        "\t Preço: "+ df.format(obj.getDouble("preco")) + "\n");

    }
}