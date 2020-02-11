import java.net.*;
import java.io.*;

public class ExtractorClient {

    //client kode under
    public static void main(String[] args) throws IOException{

        String host = "127.0.0.1";
        int port = 5555;
        String adr = "https://www.oslomet.no/om/ansatteoversikt";

        if (args.length > 0) {
            //første argument: host
            host = args[0];
            if (args.length > 1) {

                //andre argument: port
                port = Integer.parseInt(args[1]);
            }
            if (args.length > 2) {

                //tredje argument: nett-adresse
                adr = args[2];
            }
            if (args.length > 3) {

                //feilmelding: flere enn tre argumenter
                System.err.println("Usage: java extractor [<host name>] [<port number>] [<web adresss to be processsed>");
                System.exit(1);
            }
        }
        System.out.println("Email Extractor Client - Given server: " + host + ":" + port + " ");
        System.out.println("Given address: " + adr);

        try (
            Socket clientSocket = new Socket(host, port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ){
            out.println(adr);
            System.out.println("Client sending package to port: " + port + " at " + host);
            String input;
            while(true) {
                while((input = in.readLine()) != null) {
                    System.out.println(input);
                }
            }
        }catch (IOException e){
           System.out.println("Code 2: IOException in program.");
        }finally{
            System.out.println("Client finished operation.");
        }
    }

}








