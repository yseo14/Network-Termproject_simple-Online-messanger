package Chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;


public class ChatRoom implements Runnable {   
   /* Net */
   // Server 역할, connection to ChatManager
   ServerSocket listeningSocket = null;
   Socket connectionSocket = null;
   BufferedReader in_mgr = null;
   BufferedWriter out_mgr = null;
   String inMsg_mgr = null;
   String outMsg_mgr = null;
   
   // GUI
    private JFrame frame;
    private JTextField msgTf;
    private JButton enterBtn;
    private JPanel chatPanel;
    private JLabel chatLbl;
    private JButton exitBtn;
    JTextArea chatArea;
    
    // Chat
    int wantToOut = 0;

   
   // Constructor
   public ChatRoom(ServerSocket listeningSocket) {
      this.listeningSocket = listeningSocket;
      initialize();
   }
   
   private void initialize() {
         frame = new JFrame();
         frame.setBounds(100, 100, 300, 500);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.getContentPane().setLayout(null);
         
         msgTf = new JTextField();
         msgTf.setHorizontalAlignment(SwingConstants.LEFT);
         msgTf.setBounds(0, 409, 228, 54);
         frame.getContentPane().add(msgTf);
         msgTf.setColumns(10);
         
         enterBtn = new JButton("전송");
         enterBtn.setBounds(226, 409, 60, 54);
         frame.getContentPane().add(enterBtn);
         
         chatPanel = new JPanel();
         chatPanel.setBackground(new Color(176, 196, 222));
         chatPanel.setBounds(0, 0, 286, 405);
         frame.getContentPane().add(chatPanel);
         chatPanel.setLayout(null);
         
         chatArea = new JTextArea();
         chatArea.setEditable(false);
         chatArea.setBackground(new Color(176, 196, 222));
         chatArea.setBounds(0, 10, 286, 395);
         chatPanel.add(chatArea);
         
         exitBtn = new JButton("나가기");
         exitBtn.setFont(new Font("굴림", Font.PLAIN, 8));
         exitBtn.setBounds(226, 435, 60, 28);
         frame.getContentPane().add(exitBtn);
         
         exitBtn.addActionListener(new ActionListener() { 
         @Override
         public void actionPerformed(ActionEvent e) {
            wantToOut = 1;
         }
          });
         
      }


   @Override
   public void run() {
      try {
         // ChatManager accept()하기
         connectionSocket = listeningSocket.accept();
         
         // Stream 만들기
         in_mgr = new BufferedReader(new InputStreamReader(this.connectionSocket.getInputStream()));
         out_mgr = new BufferedWriter(new OutputStreamWriter(this.connectionSocket.getOutputStream()));

         // 메시지 보내기
         while(wantToOut != 1) {
            // actionListener에서 나가기 버튼 누르면 wantToOut값 변경해서 while문 빠져나가게 한 다음 thread종료 되도록 한다.
            
            // 메시지를 보낸다.
            outMsg_mgr = msgTf.getText(); // 만약 값이 없으면 NULL 값이 들어간다.
            out_mgr.write(outMsg_mgr);
            out_mgr.flush();
            
            // 메시지 받기
            // 메시지를 하나 보내고 난 뒤
            // 서버에서 보낼 거 없으면 널값 보내서 read 블럭 걸리지 않게 한다.!!!!!!!!!
            inMsg_mgr = in_mgr.readLine();
            
            //append
            chatArea.append(inMsg_mgr);
         }
         
         // wantToOut = 1이 되어 while 종료

      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}