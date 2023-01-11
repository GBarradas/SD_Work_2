
package sd.rentRoom.client;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import sd.rentRoom.rest.Anuncio;

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
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author barradas e baião
 */
public class Client {
    static final String URI = "http://localhost:8080";
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static String nomeUser;

    //menu que mostra todas as operações que o utilizador pode efetuar
    public static void menuPrinicipal() throws IOException, ParseException {
        int func = 0;
        try {
            System.out.print(
                    "--------------------------" +
                            "Escolha a funcionalidade:" +
                            "1 - Register Anuncio" +
                            "2 - Listar Anuncios" +
                            "3 - Obter Anuncio" +
                            "4 - Enviar Mensagem" +
                            "5 - Consultar Mensagens" +
                            "6 - Os Seus Anuncios" +
                            "7 - Sair");
            func = Integer.parseInt(br.readLine());


        } catch (Exception e) {
            System.err.println("ERRO! INPUT INVÁLIDO!");
            menuPrinicipal();
        }
        switch (func) {
            case 1:
                registarAnuncio();
                break;
            case 2:
                filtrarAnuncios();
                break;
            case 3:
                obterAnuncio();
                break;
            case 4:
                enviarMensagem();
                break;
            case 5:
                consultarMensagens();
                break;
            case 6:
                anunciosDoProprio();
                break;
            case 7:
                System.out.println("ADEUS!!");
                System.exit(1);
                break;
            default:
                System.err.println("ERRO! INPUT INVÁLIDO!");
                menuPrinicipal();
                break;
        }
    }

    // Recolhe e as informações sobre o anuncio e isere na base de dados
    public static void registarAnuncio() throws IOException {

        int i = -1;
        String aux;
        String anunciante = nomeUser;
        String zona = "";
        double preco = 0.0;
        String genero = "";
        String tipo = "";
        String tipologia = "";


        System.out.println("Insira as seguintes informações:");
        while (true) {
            System.out.print("Zona: ");

            aux = br.readLine();
            if (aux.length() <= 2) {
                System.err.println("ZONA INVÁLIDA!");
            } else {
                zona = aux;
                break;
            }
        }

        double p;
        while (true) {
            try {
                System.out.print("Preço: ");

                p = Double.parseDouble(br.readLine());
                if (p <= 0) {
                    System.err.println("PREÇO INVÁLIDO!");
                } else {
                    preco = p;
                    break;
                }
            } catch (Exception e) {
                System.err.println("PREÇO INVÁLIDO!");
            }
        }

        System.out.print(
                "Género:" +
                        "1: Masculino || 2: Feminino || 3: Indiferente\n");
        while (true) {
            System.out.println("Opção: ");
            aux = br.readLine();
            if (aux.equals("1")) {
                genero = "Masculino";
                break;
            } else if (aux.equals("2")) {
                genero = "Feminino";
                break;
            } else if (aux.equals("3")) {
                genero = "Indiferente";
                break;
            } else {
                System.err.println("ERRO! POR FAVOR INSIRA UMA OPÇÃO VÁLIDA!");
            }
        }

        System.out.print(
                "Que tipo de Anuncio deseja realizar:" +
                        "1: Procura || 2: Oferta\n");
        while (true) {
            System.out.print("Opção: ");
            aux = br.readLine();
            if (aux.equals("1")) {
                tipo = "Procura";
                break;
            } else if (aux.equals("2")) {
                tipo = "Oferta";
                break;
            } else {
                System.err.println("ERRO! POR FAVOR INSIRA UMA OPÇÃO VÁLIDA!");
            }
        }

        System.out.println(
                "Tipologia:" +
                        "Quarto || Tx (x = numero de assoalhadas)\n");
        while (true) {
            System.out.print("Opção: ");
            aux = br.readLine();
            aux = aux.toLowerCase();
            if (aux.startsWith("t")) {
                try {
                    int assoalhada = Integer.parseInt(aux.substring(1, aux.length()));
                    if (assoalhada < 0) {
                        System.err.println("ERRO! AO DEFINIR TIPOLOGIA!");
                    } else {
                        tipologia = "T" + assoalhada;
                        break;
                    }
                } catch (Exception e) {
                    System.err.println("ERRO! AO DEFINIR TIPOLOGIA!");
                }
            } else if (aux.equals("quarto")) {
                tipologia = "Quarto";
                break;
            } else {
                System.err.println("ERRO! AO DEFINIR TIPOLOGIA!");
            }
        }
        try {
            HttpURLConnection cnt = (HttpURLConnection) new URL(
                    URI + "/anuncios" + "/add").openConnection();
            cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            cnt.setRequestProperty("Accept", "application/json");
            cnt.setRequestMethod("POST");
            cnt.setDoOutput(true);
            String obj = "{\"tipo\":\"" + tipo + "\", \"estado\":\"inativo\", \"genero\": \"" + genero + "\"," +
                    "\"zona\": \"" + zona.replace(" ", "%20") + "\", " +
                    "\"anunciante\" : \" " + nomeUser.replace(" ", "%20") + "\", " +
                    "\"tipologia\": \"" + tipologia + "\" , \"aid\": \"-1\", " +
                    "}";
            try {
                OutputStream os = cnt.getOutputStream();
                byte[] arr = obj.getBytes(StandardCharsets.UTF_8);
                os.write(arr, 0, arr.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                BufferedReader ri = new BufferedReader(new InputStreamReader(cnt.getInputStream(), StandardCharsets.UTF_8));
                String anun = ri.readLine();
                ri.close();
                i = Integer.parseInt(anun);

            } catch (Exception e) {
                e.printStackTrace();
            }


            System.out.println("ANUNCIO REGISTADO COM ID: " + i);
            menuPrinicipal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // recolhe os filtros e comunica com o objeto remoto para obter os anuncios
    public static void filtrarAnuncios() throws IOException, ParseException {
        String tipo = "";
        String zona = "";
        String descricao = "";

        System.out.println("Selecione o Tipo de Anuncio:\n 1-Oferta | 2-Procura | 3-Ignorar");
        while (true) {
            int opt = Integer.parseInt(br.readLine());
            switch (opt) {
                case 1:
                    tipo = "oferta";
                    break;
                case 2:
                    tipo = "procura";
                    break;
                case 3:
                    break;
                default:
                    System.out.println("INPUT INVALIDO!!");
                    continue;
            }
            break;
        }
        System.out.println("Zona(0-para ignorar) :");
        while (true) {
            String z = br.readLine();
            if (z.equals("0"))
                break;
            if (z.length() < 2) {
                System.out.println("INPUT INVALIDO!!");
                continue;
            }
            zona = z;
            break;
        }
        System.out.println("Descrição(0-para ignorar): ");
        while (true) {
            String d = br.readLine();
            if (d.length() < 2) {
                System.out.println("INPUT INVALIDO!!");
                continue;
            }
            descricao = d;
            break;
        }
        String url = URI + "/anuncios/listar?";
        if (!tipo.equals(""))
            url += "tipo=" + tipo + "&";
        if (!zona.equals(""))
            url += "zona=" + zona.replace(" ", "%20") + "&";
        if (!descricao.equals(""))
            url += "descricao=" + descricao.replace(" ", "%20");
        HttpURLConnection cnt = (HttpURLConnection) new URL(
                url).openConnection();
        cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        cnt.setRequestProperty("Accept", "application/json");
        cnt.setRequestMethod("GET");

        BufferedReader ri = new BufferedReader(new InputStreamReader(cnt.getInputStream()));
        String response = ri.readLine();
        JSONArray jna = new JSONArray(response);
        for(Object jso: jna){

        }


    }

    //le um aid e mostra as informações sobre o anuncio
    public static void obterAnuncio() throws IOException {
        int aid;
        String aux;

        while (true) {
            try {
                System.out.print("Coloque o ID do anuncio desejado: ");
                aux = br.readLine();
                if (aux.equals(null)) {
                    System.err.println("ERRO AO LER ID!");
                } else {
                    aid = Integer.parseInt(aux);
                    //Anuncio a = obj.getAnuncio(aid);
                    //System.out.println(a);
                    break;
                }
            } catch (Exception e) {
                System.err.println("ERRO! VOLTE A TENTAR");
            }
        }
        //menuPrinicipal();
    }

    // le as mensagens e envia para o objeto remoto
    public static void enviarMensagem() throws IOException {
        String msg;
        int aid;
        try {
            System.out.print("Digite o ID do anuncio: ");
            aid = Integer.parseInt(br.readLine());
            System.out.print("Digite a mensagem a enviar: ");
            msg = br.readLine();
            //obj.enviarMensagem(aid, nomeUser, msg);
            System.out.println("MENSAGEM ENVIADA COM SUCESSO!");
            menuPrinicipal();
        } catch (Exception e) {
            System.err.println("ERRO! VOLTE A TENTAR");
            enviarMensagem();
        }
    }

    // obtem as  mensagens de um anuncio desejado
    public static void consultarMensagens() throws IOException {
        int aid;
        try {
            System.out.print("Digite o ID do anuncio: ");
            aid = Integer.parseInt(br.readLine());
            //List<Mensagem> resultados = obj.getMensagens(aid);
            // for(Mensagem m: resultados){
            //    System.out.println(m);
            //}
            //System.out.println("\n"+resultados.size()+ " MENSSAGENS");
            menuPrinicipal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //obtem informações dos anuncios do proprio utilizador
    public static void anunciosDoProprio() throws IOException {
        //List<Anuncio> resultados = obj.anunciosUser(nomeUser);
        //for(Anuncio a: resultados){
        //    System.out.println(a);
        //}
        //System.out.println(resultados.size()+ " ANUNCIOS");
        // menuPrinicipal();
    }

    public static void main(String[] args) {
        String host = "localhost";
        String port = "9000";  // porto do binder
        if (args.length != 3) { // requer 3 argumentos
            System.err.println
                    ("ERRO ARGUMENTOS: <host> <port> <nome de utilizador>");
            System.exit(1);
        }
        host = args[0];
        port = args[1];
        nomeUser = args[2];

        try {
            //inicia a conecção com o objeto remoto
            // obj = (GestorAnuncios) java.rmi.Naming.lookup("rmi://"
            //        + host +":"+port +"/RoomRent");
            System.out.println("Bem vindo " + nomeUser);
            menuPrinicipal();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO AO CONECTAR!");
        }
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
