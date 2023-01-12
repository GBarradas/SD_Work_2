package sd.rentRoom.auth.client;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author barradas e baião
 */
public class ClienteGestao {

    static final String URI = "http://localhost:8000";
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] Args) throws IOException
    {
        String host = "localhost";
        String port = "9000";
        /*if(Args.length != 2){
            System.out.println("ERRO ARGUMENTOS: <Host> <port>");
            System.exit(1);
        }
        host = Args[0];
        port = Args[1];
    */

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
            try {
                System.out.print("Escolha um estado (inativo ou ativo):");
                String estado = br.readLine();
                if (estado.toLowerCase().equals("inativo")) {
                    HttpURLConnection cnt = (HttpURLConnection) new URL(
                            URI + "/anuncios" + "/gestao/listagem").openConnection();
                    cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    cnt.setRequestProperty("Accept", "application/json");
                    cnt.setRequestMethod("POST");
                    cnt.setDoOutput(true);
                    String send = "{\"estado\":\"" + estado + "\"}";
                    try {
                        OutputStream os = cnt.getOutputStream();
                        byte[] arr = send.getBytes(StandardCharsets.UTF_8);
                        os.write(arr, 0, arr.length);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    BufferedReader l = new BufferedReader(new InputStreamReader(cnt.getInputStream()));
                    String r = l.readLine();
                    JSONArray resultados = new JSONArray(r);
                    for(int i=0; i<resultados.length(); i++){
                        printAnuncio( resultados.getJSONObject(i));
                    }
                    break;
                } else if (estado.toLowerCase().equals("ativo")) {
                    HttpURLConnection cnt = (HttpURLConnection) new URL(
                            URI + "/anuncios" + "/gestao/listagem").openConnection();
                    cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    cnt.setRequestProperty("Accept", "application/json");
                    cnt.setRequestMethod("POST");
                    cnt.setDoOutput(true);
                    String send = "{\"estado\":\"" + estado + "\"}";
                    try {
                        OutputStream os = cnt.getOutputStream();
                        byte[] arr = send.getBytes(StandardCharsets.UTF_8);
                        os.write(arr, 0, arr.length);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    BufferedReader l = new BufferedReader(new InputStreamReader(cnt.getInputStream()));
                    String r = l.readLine();
                    JSONArray resultados = new JSONArray(r);
                    for(int i=0; i<resultados.length(); i++){
                        printAnuncio( resultados.getJSONObject(i));
                    }
                    break;
                } else {
                    System.err.println("INPUT INVALIDO");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        menu();
    }
    // obter um anuncio pelo aid
    public static void verAnuncio() throws IOException
    {
        int aid;
        String aux;
        while(true){
            try{
                System.out.print("Indique o ID do Anuncio: ");
                aux = br.readLine();
                if(aux.equals(null)){
                    System.err.println("ERRO AO LER ID!");
                } else{
                    aid = Integer.parseInt(aux);
                    HttpURLConnection cnt = (HttpURLConnection) new URL(
                    URI+"/anuncios"+"/gestao").openConnection();
                    cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    cnt.setRequestProperty("Accept", "application/json");
                    cnt.setRequestMethod("POST");
                    cnt.setDoOutput(true);
                    String send = "{\"aid\":\""+aid+"\"}";
                    System.out.println(send);
                    try{
                        OutputStream os = cnt.getOutputStream();
                        byte[] arr = send.getBytes(StandardCharsets.UTF_8);
                        os.write(arr, 0, arr.length);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    try{
                        BufferedReader ri = new BufferedReader(new InputStreamReader(cnt.getInputStream(), StandardCharsets.UTF_8));
                        String response = ri.readLine();
                        System.out.println(response);
                        if(response == null){
                            menu();
                        }
                        JSONObject jobj = new JSONObject(response);
                        printAnuncio(jobj);
                        break;
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e){
                System.err.println("INPUT INVALIDO!");
            }
        }
        menu();
    }

    //Altera o estado de um anuncio para ativo
    public static void AprovarAnuncio() throws IOException
    {
        int aid;
        String aux;
        while(true){
            try{
                System.out.print("Indique o ID do Anuncio: ");
                aux = br.readLine();
                if(aux.equals(null)){
                    System.err.println("ERRO AO LER ID!");
                } else{
                    aid = Integer.parseInt(aux);
                    HttpURLConnection cnt = (HttpURLConnection) new URL(
                            URI+"/anuncios"+"/gestao/aprovar").openConnection();
                    cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    cnt.setRequestProperty("Accept", "application/json");
                    cnt.setRequestMethod("POST");
                    cnt.setDoOutput(true);
                    String send = "{\"aid\":\""+aid+"\"}";
                    try{
                        OutputStream os = cnt.getOutputStream();
                        byte[] arr = send.getBytes(StandardCharsets.UTF_8);
                        os.write(arr, 0, arr.length);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    try{
                        BufferedReader ri = new BufferedReader(new InputStreamReader(cnt.getInputStream(), StandardCharsets.UTF_8));
                        String response = ri.readLine();
                        System.out.println(response);
                        break;
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        menu();
    }
    // altera o estado de um anuncio de ativo para inativo ou vs versa
    public static void alterarEstadoAnuncio() throws IOException
    {
        int aid;
        while(true) {
            try {
                System.out.print("Indique o ID do Anuncio: ");
                aid = Integer.parseInt(br.readLine());
                break;
            } catch (Exception e) {
                System.err.println("INPUT INVALIDO!");
            }
        }
        HttpURLConnection cnt = (HttpURLConnection) new URL(
                URI+"/anuncios"+"/gestao/alterar").openConnection();
        cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        cnt.setRequestProperty("Accept", "application/json");
        cnt.setRequestMethod("POST");
        cnt.setDoOutput(true);
        String send = "{\"aid\":\""+aid+"\"}";
        try{
            OutputStream os = cnt.getOutputStream();
            byte[] arr = send.getBytes(StandardCharsets.UTF_8);
            os.write(arr, 0, arr.length);
        } catch(Exception e){
            e.printStackTrace();
        }
        try{
            BufferedReader ri = new BufferedReader(new InputStreamReader(cnt.getInputStream(), StandardCharsets.UTF_8));
            String response = ri.readLine();
            System.out.println(response);
        } catch(Exception e){
            e.printStackTrace();
        }

        menu();
    }

    public static void printAnuncio(JSONObject obj) throws java.text.ParseException {
    String dateStr = obj.getString("data");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date date = sdf.parse(dateStr);
    DecimalFormat df = new DecimalFormat("#,##0.00€");
    sdf = new SimpleDateFormat("dd-MM-yyyy");
    System.out.println(
            "-------------------------------------------------------\n" +
                    "\t Aid: "+ obj.getInt("aid") + "\n"+
                    "\t Tipo: " + obj.getString("tipo") + "\n"+
                    "\t Estado: "+ obj.getString("estado") + "\n"+
                    "\t Genero: " + obj.getString("genero") +   "\n"+
                    "\t Zona: " + obj.getString("zona") + "\n"+
                    "\t Anunciante:  "+ obj.getString("anunciante") + "\n"+
                    "\t Tipologia: "+ obj.getString("tipologia") + "\n"+
                    "\t Data: " + sdf.format(date) + "\n"+
                    "\t Preço: "+ df.format(obj.getDouble("preco")) + "\n");
    }
}