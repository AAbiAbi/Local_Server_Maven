//import java.io.*;
//import java.net.*;
//
////import com.sun.tools.sjavac.server.RequestHandler;
//import org.apache.commons.cli.*;
//import org.apache.http.HttpException;
//import org.apache.http.HttpRequest;
//import org.apache.http.HttpResponse;
//import org.apache.http.protocol.HttpContext;
//import org.apache.http.protocol.HttpRequestHandler;
//import java.io.*;
//import java.net.*;
//import org.apache.commons.cli.Options;
//
//
//public class SimpleWebServer {
//    private static final int DEFAULT_PORT = 8080;
//    private static final String DEFAULT_DOCROOT = "src/root";
//
//    public static void main(String[] args) {
//        // Define command line options
//        Options options = new Options();
//        options.addOption("port", true, "port number to listen on");
//        options.addOption("document_root", true, "document root directory");
//
//        // Parse command line arguments
//        CommandLineParser parser = new DefaultParser();
//        CommandLine cmd = null;
//        try {
//            cmd = parser.parse(options, args);
//        } catch (ParseException e) {
//            System.err.println("Error: " + e.getMessage());
//            System.exit(1);
//        }
//
//        // Get port number from command line arguments or use default
//        int port = DEFAULT_PORT;
//        if (cmd.hasOption("port")) {
//            port = Integer.parseInt(cmd.getOptionValue("port"));
//        }
//
//        // Get document root directory from command line arguments or use default
//        String docroot = DEFAULT_DOCROOT;
//        if (cmd.hasOption("document_root")) {
//            docroot = cmd.getOptionValue("document_root");
//        }
//
//        // Start server
//        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            System.out.println("Listening on port " + port);
//
//            while (true) {
//                Socket clientSocket = null;
//                try {
//                    clientSocket = serverSocket.accept();
//                    //if(!clientSocket.isClosed()){
//                    System.out.println("Accepted connection from " + clientSocket.getInetAddress());
//                    //HelloWorld hw = new HelloWorld();
//                    //Request_handler requestHandler = new Request_handler(clientSocket, docroot);
//                    Request_Handler requestHandler = new Request_Handler(clientSocket, docroot);
//
//                    Thread thread = new Thread(requestHandler);
//                    thread.start();
//                    //}
//                }
//                catch (SocketException e) {
//                    if (clientSocket != null && !clientSocket.isClosed()) {
//                        try {
//                            clientSocket.close();
//                        } catch (IOException ex) {
//                            // handle the exception
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Error: " + e.getMessage());
//            System.exit(1);
//        }
//    }
//}
//
//
//class Request_Handler implements Runnable{
//    public Request_Handler() {
//        System.out.println("hi");
//    }
//    private Socket clientSocket;
//    private static String docRoot = "src/root";
//
//    public Request_Handler(Socket clientSocket) {
//        this.clientSocket = clientSocket;
//    }
//    public Request_Handler(Socket clientSocket, String docRoot) {
//        this.clientSocket = clientSocket;
//        this.docRoot = docRoot;
//    }
//    @Override
//    public void run() {
//        try {
//            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            String request = in.readLine();
//            if(request == null){
//                in.close();
//                //clientSocket.close();
//                System.out.println("request null");
//                return;
//            }
//            System.out.println("Received request: " + request);
//
//            OutputStream out = clientSocket.getOutputStream();
//
//            if (request.startsWith("GET ")) {
//                String[] parts = request.split(" ");
//                if (parts.length != 3) {
//                    //writeResponse(out, 400, "Bad Request", "Invalid request line",true);
//                    out.write("HTTP/1.1 400 Bad Request\r\n".getBytes());
//                    out.write("\r\n".getBytes());
//                    out.write("<h1>400 Bad Request</h1>".getBytes());
//                    return;
//                }
//                String path = parts[1];
//                System.out.println(path);
//                String fileExtension = getFileExtension(path);
//                //System.out.println(fileEx[0]);
//                //System.out.println(fileEx.length);
//                //String fileExtension = fileEx[1];
//                String httpPro = parts[2];
//                String[] versionArray = httpPro.split("/");//["HTTP","1.0"]
//                String httpVer = versionArray[1];
//                boolean http11 = httpVer.equals("1.1");
//                boolean keepAlive = http11;
//                //System.out.println("Path"+ path);
//                String documentRoot = "src/root";
//
//                if(path.equals("/")){
//                    path = "/index.html";
//                }
//                System.out.println(docRoot + path);
//
//                File file = new File(docRoot + path);
//                if (file.exists()) {
//                    if (!file.canRead()) {
//                        out.write("HTTP/1.1 403 Forbidden\r\n".getBytes());
//                        out.write("Content-Type: text/html\r\n".getBytes());
//                        out.write("\r\n".getBytes());
//                    }
//                    if (fileExtension.equals("html") || fileExtension.equals("txt")) {
//                        BufferedReader fileIn = new BufferedReader(new FileReader(file));
//                        out.write("HTTP/1.1 200 OK\r\n".getBytes());
//                        out.write("Content-Type: text/html\r\n".getBytes());
//                        out.write("\r\n".getBytes());
//                        String line;
//                        while ((line = fileIn.readLine()) != null) {
//                            out.write(line.getBytes());
//                        }
//                        fileIn.close();
//                    } else if (fileExtension.equals("jpg") || fileExtension.equals("gif")) {
//                        FileInputStream fileIn = new FileInputStream(file);
//                        byte[] buffer = new byte[1024];
//                        int bytesRead;
//                        while ((bytesRead = fileIn.read(buffer)) != -1) {
//                            out.write(buffer, 0, bytesRead);
//                        }
//                        fileIn.close();
//                        out.write("HTTP/1.1 200 OK\r\n".getBytes());
//                        out.write(("Content-Type: image/jpeg" + fileExtension + "\r\n").getBytes());
//                        out.write("\r\n".getBytes());
//
//                    }
//                } else {
//                    out.write("HTTP/1.1 404 Not Found\r\n".getBytes());
//                    out.write("\r\n".getBytes());
//                    out.write("<h1>404 Not Found</h1>".getBytes());
//                }
//            } else {
//                out.write("HTTP/1.1 501 Not Implemented\r\n".getBytes());
//                out.write("\r\n".getBytes());
//                out.write("<h1>501 Not Implemented</h1>".getBytes());
//            }
//
//            out.flush();
//            out.close();
//            in.close();
//            clientSocket.close();
//
////            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
////            OutputStream out = clientSocket.getOutputStream();
////
////            String request = in.readLine();
////            System.out.println("Request received: " + request);
////
////            out.write("HTTP/1.1 200 OK\r\n".getBytes());
////            out.write("Content-Type: text/html\r\n\r\n".getBytes());
////            out.write("<html><body><h1>Hello, World!</h1></body></html>".getBytes());
////
////            out.flush();
////            out.close();
////            in.close();
////            clientSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }catch(NullPointerException e){
//            System.out.println("NPE");
//            return;
//        }
//    }
//    public static String getFileExtension(String fileName) {
//        String extension = "";
//
//        int i = fileName.lastIndexOf('.');
//        if (i > 0) {
//            extension = fileName.substring(i + 1);
//        }
//
//        return extension;
//    }
//}
//
