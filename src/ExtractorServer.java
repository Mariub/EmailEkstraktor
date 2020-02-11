import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractorServer {

    public static void main(String[] args) throws IOException {

        int port = 5555;

        if (args.length == 1) {
            //et argument, alternativ port valgt
            port = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            //feilmelding: flere enn ett argumenter
            System.err.println("Usage: 'java extractor server [<port number>]' Don't write any additional arguments. ");
            System.exit(1);
        }
        System.out.println("Email Extractor Server - Given listening port: "  + port + " ");
        try (
                ServerSocket serverSocket = new ServerSocket(port);
        ) {
            String receivedString;
            System.out.println("Server started.");
            Socket socket = null;
            while (true) {
                socket = serverSocket.accept();

                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Service clientServer = new Service(socket,out,in);
                clientServer.start();

            }
        } catch (IOException e) {
            System.out.print("Error occured");
            System.out.println(e.getMessage());
        }
    }

    static class Service extends Thread {
        Socket connectSocket;
        InetAddress addr;
        int lclPort, srvPort;
        PrintWriter out;
        BufferedReader in;
        int i = 0;

        public Service(Socket connectSocket, PrintWriter out, BufferedReader in) {
            this.connectSocket = connectSocket;
            addr = connectSocket.getInetAddress();
            lclPort = connectSocket.getLocalPort();
            srvPort = connectSocket.getPort();
            this.out = out;
            this.in = in;
            System.out.println("Thread started");
        }

        @Override
        public void run() {

                try {
                    String receivedString;
                    System.out.println("Method run, waiting for connection...");
                    while (((receivedString = in.readLine()) != null)) {

                        System.out.println("Client connected from: " + addr.getHostAddress() + " on port " + lclPort);
                        System.out.println("Address given was: " + receivedString + " Ready to process...");

                        String output = processToReturn(receivedString);
                        System.out.println("Sending output to client.\n");
                        out.println(output);
                    }
                  
                } catch (IOException e) {
                    out.println("Code 2: Website not a working address.");
                    System.out.println("Error occured");
                }

        }

        public String processToReturn(String url) throws MalformedURLException {
            ArrayList<String> addresses = new ArrayList<String>();
            URL page = new URL(url);
            Pattern pattern = Pattern.compile("\\b[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9.-]+\\b");
            int code;
            try(
                    BufferedReader in = new BufferedReader(new InputStreamReader(page.openStream()));
            ){
             String input;
                System.out.println("Processing of string started.");
             while((input = in.readLine()) != null) {
                 Matcher match = pattern.matcher(input);
                 while(match.find()){
                     addresses.add(match.group()+"\n");
                     i++;
                 }

             }
            }catch (IOException e){
                    System.out.println("Code 2 "+ "\n" + "IOException in program.");
                    return "Code 2: "+ "\n" + "IOException in program.";
                }

            System.out.println("URL finished processing...");
            System.out.println(i + " addresses found.");
            if(addresses.size() == 0) return "Code 1"  + "\n" + "No email addresses found on given URL.";
            addresses.add(0,"Code 0:" + "\n");
            return addresses.toString();
        }
    }
}
