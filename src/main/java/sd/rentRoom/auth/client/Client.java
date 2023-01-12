
package sd.rentRoom.auth.client;


import org.json.JSONArray;
import org.json.JSONObject;
import sd.rentRoom.auth.mqtt.Subscriber;


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
import java.util.List;

public class Client {
    static private Subscriber pubSub;
    static final String URI = "http://localhost:8000";
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static String nomeUser;

    //menu que mostra todas as operações que o utilizador pode efetuar
    public static void menuPrinicipal() throws Exception {
        int func = 0;
        try {
            List<String> msgs= pubSub.mqttGetMsg();
            if(msgs.size() != 0){
                System.out.println(msgs.size()+" MENSAGENS!");
                for(String m: msgs){
                    System.out.println(m);
                }
            }
            System.out.print(
                    "--------------------------\n" +
                            "Escolha a funcionalidade:\n" +
                            "1 - Register Anuncio\n" +
                            "2 - Listar Anuncios\n" +
                            "3 - Obter Anuncio\n" +
                            "4 - Enviar Mensagem\n" +
                            "5 - Consultar Mensagens\n" +
                            "6 - Os Seus Anuncios\n" +
                            "7 - Sair\n" +
                            "Opção: ");
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
        String descricao = "";


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
        System.out.println("Descrição: ");
        descricao = br.readLine();
        try {
            HttpURLConnection cnt = (HttpURLConnection) new URL(
                    URI + "/anuncios" + "/novo").openConnection();
            cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            cnt.setRequestProperty("Accept", "application/json");
            cnt.setRequestMethod("POST");
            cnt.setDoOutput(true);
            String obj = "{\"tipo\":\"" + tipo + "\", \"estado\":\"inativo\", \"genero\": \"" + genero + "\"," +
                    "\"zona\": \"" + zona + "\", " +
                    "\"anunciante\" : \" " + nomeUser + "\", " +
                    "\"tipologia\": \"" + tipologia + "\" , \"aid\": \"-1\", " +
                    "\"preco\": \""+preco+"\", \"descricao\": \""+descricao+"\"}";
            System.out.println(obj);
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
                if(i == -1){
                    System.out.println("ERRO  AO SUBMETER O ANUNCIO!!");
                    menuPrinicipal();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            System.out.println("ANUNCIO REGISTADO COM ID: " + i);
            while(true){
                System.out.println("Deseja ser notificado? (s/n) :");
                String response = br.readLine();
                if(response.equals("s")||response.equals("S")){
                    pubSub.mqttSubscribe("anuncio"+i);
                    break;
                }
                else if(response.equals("n")|| response.equals("N")){
                    break;
                }
                else{
                    System.err.println("INPUT INVALIDO!!");
                }
            }
            menuPrinicipal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // recolhe os filtros e comunica com o objeto remoto para obter os anuncios
    public static void filtrarAnuncios() throws Exception {
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
            if(d.equals("0"))
                break;
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
        for(int i = 0; i < jna.length(); i++){
            JSONObject jso = jna.getJSONObject(i);
            printAnuncio(jso);
        }
        System.out.println( jna.length() + " ANUNCIOS LISTADOS");
        menuPrinicipal();

    }

    //le um aid e mostra as informações sobre o anuncio
    public static void obterAnuncio() throws Exception{
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
                    HttpURLConnection cnt = (HttpURLConnection) new URL(
                            URI + "/anuncios" + "/"+aid).openConnection();
                    cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    cnt.setRequestProperty("Accept", "application/json");
                    cnt.setRequestMethod("GET");
                    cnt.setDoOutput(true);
                    try {
                        BufferedReader ri = new BufferedReader(new InputStreamReader(cnt.getInputStream()));
                        String response = ri.readLine();
                        JSONObject jobj = new JSONObject(response);

                        printAnuncio(jobj);
                        break;
                    }
                    catch(Exception e){
                        System.err.println("ANUNCIO INVALIDO OU INEXISTENTE!!");
                    }
                }
            } catch (Exception e) {
                System.err.println("ERRO! VOLTE A TENTAR");
            }
        }
        menuPrinicipal();
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
            HttpURLConnection cnt = (HttpURLConnection) new URL(
                    URI + "/msg/send").openConnection();
            cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            cnt.setRequestProperty("Accept", "application/json");
            cnt.setRequestMethod("POST");
            cnt.setDoOutput(true);
            String send = "{\"aid\":\""+aid+"\",\"remetente\":\""+nomeUser+"\",\"msg\":\""+msg+"\"}";
            try {
                OutputStream os = cnt.getOutputStream();
                byte[] arr = send.getBytes(StandardCharsets.UTF_8);
                os.write(arr, 0, arr.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                BufferedReader ri = new BufferedReader(new InputStreamReader(cnt.getInputStream(), StandardCharsets.UTF_8));
                String Smsg = ri.readLine();
                ri.close();
                System.out.println(Smsg);

            } catch (Exception e) {
                e.printStackTrace();
            }

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
            HttpURLConnection cnt = (HttpURLConnection) new URL(
                    URI + "/msg/see?aid="+aid).openConnection();
            cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            cnt.setRequestProperty("Accept", "application/json");
            cnt.setRequestMethod("GET");
            try {
                BufferedReader ri = new BufferedReader(new InputStreamReader(cnt.getInputStream(), StandardCharsets.UTF_8));
                String Smsg = ri.readLine();
                ri.close();
                System.out.println(Smsg);
                JSONArray jArr = new JSONArray(Smsg);
                for(int i = 0; i < jArr.length();i++){
                    printMensagem(jArr.getJSONObject(i));
                }
                System.out.println(jArr.length() + " MENSAGENS!!");

            } catch (Exception e) {
                e.printStackTrace();
            }
            menuPrinicipal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //obtem informações dos anuncios do proprio utilizador
    public static void anunciosDoProprio() throws Exception {
        HttpURLConnection cnt = (HttpURLConnection) new URL(
                URI + "/anuncios/user").openConnection();
        cnt.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        cnt.setRequestProperty("Accept", "application/json");
        cnt.setRequestMethod("POST");
        cnt.setDoOutput(true);
        String send = "{\"anunciante\": \""+nomeUser+"\"}";
        try {
            OutputStream os = cnt.getOutputStream();
            byte[] arr = send.getBytes(StandardCharsets.UTF_8);
            os.write(arr, 0, arr.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BufferedReader ri = new BufferedReader(new InputStreamReader(cnt.getInputStream(), StandardCharsets.UTF_8));
            String Smsg = ri.readLine();
            ri.close();
            JSONArray jArr = new JSONArray(Smsg);
            for(int i = 0; i < jArr.length();i++){
                printAnuncio(jArr.getJSONObject(i));
            }
            System.out.println(jArr.length()+ "ANUNCIOS LISTADOS!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
        menuPrinicipal();
    }

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        String port = "9000";  // porto do binder
        /*if (args.length != 3) { // requer 3 argumentos
            System.err.println
                    ("ERRO ARGUMENTOS: <host> <port> <nome de utilizador>");
            System.exit(1);
        }
        host = args[0];
        port = args[1];
        nomeUser = args[2];
        */
        nomeUser="barradas";
        pubSub = new Subscriber(nomeUser);

            System.out.println("Bem vindo " + nomeUser);
            menuPrinicipal();


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
                "\t Preço: "+ df.format(obj.getDouble("preco")) + "\n" +
                "\t Descrição: "+obj.getString("descricao")+"\n");
    }
    public static void printMensagem(JSONObject jObj) throws java.text.ParseException {
        String dateStr = jObj.getString("data");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println( "-------------------------------------------------------------------------------\n     "
                +jObj.getString("remetente")+"("+sdf.format(date)+"): " +jObj.getString("msg")+";");
    }

}
