import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ExtractorClient {

    //client kode under
    public static void main(String[] args) throws IOException{
        String adr = "0";
        String host = "";
        int port = 5555;
        while(true){
            Scanner input = new Scanner(System.in);

            if (args.length > 0) {
                //første argument: host
                host = args[0];
                if (args.length > 1) {

                    //andre argument: port
                    port = Integer.parseInt(args[1]);
                }
                if (args.length > 2) {

                    //feilmelding: flere enn tre argumenter
                    System.err.println("Usage: java extractor [<host name>] [<port number>]");
                    System.exit(1);
                }
            }

            System.out.println("Email Extractor Client - Waiting for user input of address (Write 0 for standard, write -1 for exit)");
            adr = input.next();
            if(adr.equals("-1")){
                System.out.println("Program shuts down.");
                break;
            }
            System.out.println("Address entered = " + adr);
            run(adr, host, port);

        }

    }

    public static void run(String adr, String host, int port){
        if(adr.equals("0"))adr = "https://www.oslomet.no/om/ansatteoversikt";


        System.out.println("Given server: " + host + ":" + port + " ");
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
                    if(input.equals("]"))return;
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








