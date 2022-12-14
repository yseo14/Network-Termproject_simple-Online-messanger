package Chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * <<ChatServerThread>>
 * 특징 
 * KakaotServerthread를 상속받아서 KakaotServerthread의 기능도 수행할 수 있도록 한다.
 * ChatServerThread의 기능 중 사용자의 기본적인 요청을 처리하는 기능을 가져오기 위함이다. 
 * 
 * 기능
 * 채팅 요청자와 채팅 피요청자에게 각각 인풋을 받고 아웃풋을 주는 역할을 한다. 
 */

public class ChatManagerThread implements Runnable{
   // ChatRoom Info
   String user_id = null;
   String counterpart_id = null;
   String user_ip = null;
   String counterpart_ip = null;
   int user_port = -1;
   int counterpart_port = -1;
   
   // Net 
   // for chat requester
   Socket socket_requester = null;
   BufferedReader in_requester = null;
   BufferedWriter out_requester = null;
   String inMsg_requester = null;
   String outMsg_requester = null;
      
   // Net 
   // for chat requester
   Socket socket_requested = null;
   BufferedReader in_requested = null;
   BufferedWriter out_requested = null;
   String inMsg_requested = null;
   String outMsg_requested = null;
   
   // DB
   Statement stmt = null;
   ResultSet rs = null;
   PreparedStatement pstm = null;

   
   public ChatManagerThread(String user_id, String counterpart_id) {
      this.user_id = user_id;
      this.counterpart_id = counterpart_id;
   }
   
   // Connect to Server
      private void connectToParticipant() {
         try (Connection conn = JDBC.connection())
         {
            String checkQry = null;
            
            // get user Info(ip, port) configuration
            stmt = conn.createStatement();
            checkQry = "select ip, port from user_connection where user_id = \"" + user_id +  "\"";
            rs = stmt.executeQuery(checkQry);
            rs.next();
            user_ip = rs.getString(1);
            user_port = rs.getInt(2);
            
            // get CounterPart Info(ip, port) configuration
            stmt = conn.createStatement();
            checkQry = "select ip, port from user_connection where user_id = \"" + counterpart_id +  "\"";
            rs = stmt.executeQuery(checkQry);
            rs.next();
            counterpart_ip = rs.getString(1);
            counterpart_port = rs.getInt(2);
            
            // Make TCP connection with requester and reqeusted person each.
            socket_requester = new Socket(user_ip, user_port);
            socket_requested = new Socket(counterpart_ip, counterpart_port);
            
            // Make inputstream and outputstream
            in_requester = new BufferedReader(new InputStreamReader(socket_requester.getInputStream()));
            out_requester = new BufferedWriter(new OutputStreamWriter(socket_requester.getOutputStream()));
            
         } catch (SQLException e) {
            e.printStackTrace();
         } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }

   @Override
   public void run() {
      System.out.println("채팅방 메니저 쓰레드 시작");
      
   }

}