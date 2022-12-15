package myserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpClientConnection implements Runnable{
  private String host;
  private Integer PORT;
  private Socket client;
  

  @Override
  public void run(){
    try {
      Socket client = new Socket(host, PORT);

      InputStream is = client.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader bfr = new BufferedReader(isr);
      String request = bfr.readLine();
      System.out.println(request);
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      try {
        client.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
