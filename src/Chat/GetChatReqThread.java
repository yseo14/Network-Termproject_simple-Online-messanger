package Chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/* << GetChatRequestThread >>
 * 생성장소
 * KakaoClientThread의 run()에서 allowed Login되었을 떄
 * 
 * 유저가 로그인~로그아웃 동안 다른 유저로부터 계속해서 채팅 요청을 접수받는 역할을 합니다. 
 * 1) 채팅 매니저로부터 TCP 연결 요청이 들어옵니다.
 * 2) listening socket의 accept => connection socket 셍성됨 (TCP 연결) 
 * 3) connection socket를 생성자 인자로 받는 ProcessChatReqThread를 생성합니다. 
 * 4) (채팅 수락 후 채팅 기능) 또는 (채팅 거절 기능)을 수행하도록 하는 Thread인 ProcessChatReqThread를 따로 만들어서 run
 *     => GetChatRequestThread의 채팅 요청 접수 기능의 반응성을 높입니다.
 * 5) back to 1)
 */
public class GetChatReqThread implements Runnable {
   // User Info
   String user_id = null;
   
   // Net
   int listeningSocketPort= 0;
   ServerSocket listeningSocket = null;
   Socket connectionSocket = null;
   
   // DB
   Statement stmt = null;
   ResultSet rs = null;
   PreparedStatement pstm = null;
   
   // Constructor: default
   public GetChatReqThread(String user_id, ServerSocket listeningSocket) {
      this.user_id = user_id;
      this.listeningSocket = listeningSocket;
   }

   @Override
   public void run() {
      // DB에 해당 정보 기입
      try (Connection conn = JDBC.connection())
      {
         listeningSocket.setReuseAddress(true);
         
         
         // DB에 해당 정보 넣기
         // 로그아웃하면 이 정보를 없애야 해.
         stmt = conn.createStatement();
         String insertQry = "insert into getChatReqInfo (user_id, ip, port) " + " values (locathost, " + listeningSocketPort + ")";
         System.out.println();
         //rs = stmt.executeQuery(insertQry);
         
         System.out.println("채팅 요청 접수를 시작합니다.");
         
         
            connectionSocket = listeningSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
            String inMsg = null;
            String outMsg = null;
            
            // 바로 채팅할 수 있게!
            ChatRoomGUI chatRoomGUI = new ChatRoomGUI(); // 채팅방 화면 띄우고
            ChatRoomGUI chatRoomGUI2 = new ChatRoomGUI();
            System.out.println("GetChatRequest!");
            
     
            	inMsg = in.readLine();            	
            	chatRoomGUI.addConversation(inMsg);
            		outMsg = chatRoomGUI.getConversation();
            		chatRoomGUI.addConversation(outMsg);
            		out.write(outMsg + "\n");
            		out.flush();
                       	
        
         } catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
      }  
      
   
}