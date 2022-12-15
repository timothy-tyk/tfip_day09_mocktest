package myserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import myserver.HttpWriter;

public class HttpServer implements Runnable{
  private Integer PORT;
  private String[] filePath;
  private DataInputStream dis;
  private DataOutputStream dos;
  private Socket s;
  
  public HttpServer(){};
  public HttpServer(Integer PORT, String[]filePath){
    this.PORT = PORT;
    this.filePath = filePath;
  }
  
  public void startServer(Integer PORT){
    System.out.println(String.format("Waiting for Connection / Listening on PORT %d",PORT));
    ExecutorService threadpool = Executors.newFixedThreadPool(3);
      
    try {
      ServerSocket server = new ServerSocket(PORT); 
      Socket socket = server.accept();
      HttpClientConnection clientHandler = new HttpClientConnection();
      threadpool.submit(clientHandler);
      //Input
      DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
      //Output
      DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
      System.out.println("Connected on "+PORT);
      
      handleRequestMethod(socket);
      server.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void checkPaths(String[] filePath){
    //Check paths
        for(Integer i = 0; i<filePath.length;i++){
        System.out.println("Checking "+filePath[i]);
        String fp = filePath[i];
        Path p = Paths.get(fp);
        File f = new File(fp);
        if(!f.exists() || !f.isDirectory()){
          System.out.println("Error");
          System.exit(1);
        }
      } 
  }
  public void handleRequestMethod(Socket socket)throws Exception{
    // get RequestMethod
      OutputStream os = socket.getOutputStream();
      
      String requestMethod = request.split(" ")[0];
      System.out.println(request);
      HttpWriter clientOut = new HttpWriter(os);
      // Action 1 - Not a GET method
      if(!requestMethod.equals("GET")){
      clientOut.writeString(String.format("HTTP/1.1 405 Method Not Allowed\r\n\n\r\n\n%s not supported\r\n",requestMethod));
      clientOut.flush();
      clientOut.close();
      }
      // Action 2 - Resource does not exist
      String resource = request.split(" ")[1];
      if(resource.equals("/")) resource = "/index.html";

      for(int i=0; i<filePath.length;i++){
        File resourceFile = new File(filePath[i], resource.replace("/", ""));
        
        boolean resourceExistsCheck = resourceFile.exists();
        if(resourceExistsCheck){
          System.out.println(resourceFile.toPath());
          // Action 3 - Resource Exists
          writeToClient(resourceFile, os);
          break;
        }
        clientOut.writeString(String.format("HTTP/1.1 404 Not Found\r\n\n\n\r\n%s not found\r\n",resource));
        clientOut.flush();
        clientOut.close();
        }
      }

      public void writeToClient(File resourceFile, OutputStream os)throws Exception{
        // Action 3 - Resource Exists
          HttpWriter clientOut = new HttpWriter(os);
          byte[] resourceBytes = Files.readAllBytes(resourceFile.toPath());
          clientOut.writeString("HTTP/1.1 200 OK\r\n");
          clientOut.writeBytes(resourceBytes);
          clientOut.flush();
          clientOut.close();
          os.close();
      }

        
  

  @Override
  public void run() {
    try {
      //Start Server
      // checkPaths(filePath);
      startServer(PORT);
      


    } catch (Exception e) {
      e.printStackTrace();
    }
      
    
  }
}
