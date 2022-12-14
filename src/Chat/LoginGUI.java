package Chat;
import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.event.*;
import java.sql.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class LoginGUI
{
   // User Input(Info)
   String input_id = null;
   String input_password = null;
   String crypto=null;
   
   // Net
   String serverIP = "localhost";
   int serverPort = 8888;
   Socket socket = null;
   JOptionPane message = new JOptionPane();
   // GUI
   private Image img = new ImageIcon("../img/kakaotalk.png").getImage();

    public LoginGUI()
    {
       // connect to server
       try {         
         socket = new Socket(serverIP, serverPort);
      } catch (IOException e) {
    		message.showMessageDialog(null, "카카오톡 로그인 서버에 연결할 수 없습니다." + '\n' + "사용자 네트워크 연결이 불안정 하거나, 방화벽 등에 의해 카카오톡 사용이 차단된 환경일 수 있습니다." );
         // 에러 창 띄우기
         // 카카오톡 처럼 에러창 확인 누르면 누르인 창이 뜨긴 하지만 로그인 버튼 누르면 다시 에러창 뜨고 다음으로 넘어가지 않도록 한다.
      }       
       SHA256 sha256=new SHA256();
        JFrame jf = new JFrame();
        jf.setSize(400,450);
        jf.setLocationRelativeTo(null);
        jf.setTitle("login");
        jf.setLayout(null);
     
        Color K = new Color(247,230,0);
        Color b=new Color(255,255,255);
        Color c = new Color(0,172,238);
        Color g = new Color(58, 29, 29);
        jf.setBackground(K);
        jf.getContentPane().setBackground(K);

        Font font = new Font("Aharoni 굵게",Font.BOLD,30);
        Font font2 = new Font("Aharoni 굵게",Font.BOLD,17);
        Font font3 = new Font("Aharoni 굵게",Font.BOLD,13);
        
        Image changeImg = img.getScaledInstance(125, 50, Image.SCALE_SMOOTH);
        ImageIcon changeIcon = new ImageIcon(changeImg);

        JLabel twitter = new JLabel(changeIcon);
        twitter.setBounds(140,80,130,50);
        jf.add(twitter); 
        
       
        JLabel id = new JLabel("아이디 : ");
        id.setSize(120,30);
        id.setLocation(36,320);
        id.setHorizontalAlignment(JLabel.CENTER);
        id.setForeground(new Color(128,128,128));
        jf.add(id);
 
        final JTextField id_text = new JTextField();
        id_text.setSize(200,30);
        id_text.setLocation(120,320);
        jf.add(id_text);

        JLabel password = new JLabel("비밀번호 : ");
        password.setSize(90,30);
        password.setLocation(60,360);
        password.setForeground(new Color(128,128,128));
        jf.add(password);

        final JPasswordField password_text = new JPasswordField();
        password_text.setSize(200,30);
        password_text.setLocation(120,360);
        jf.add(password_text);

        JButton login = new JButton("로그인 ");
        login.setSize(350,35);
        login.setLocation(15,265);
        login.setFont(font3);
        login.setBackground(b);
        login.setForeground(g);
        jf.add(login);
 
        JButton signup = new JButton("회원가입 ");
        signup.setSize(350,35);
        signup.setLocation(15,220);
        signup.setFont(font3);
        signup.setBackground(g);
        signup.setForeground(b);
        signup.setOpaque(true);
        signup.setBorderPainted(false);
        jf.add(signup);

        jf.setVisible(true);
        
        JLabel find_password = new JLabel("비밀번호를 잊으셨습니까?  ");
        find_password.setSize(140,30);
        find_password.setLocation(240,190);
        find_password.setForeground(new Color(128,128,128));
        jf.add(find_password);
        
        find_password.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == 1)
                {
                   jf.setVisible(false);
                    //new Find_pw();
                }
            }
        });
 
        signup.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
//                jf.setVisible(false);
                new SignupGUI();
            }
        });
        
      
        login.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent E)
            {
               if(socket == null) {
            		message.showMessageDialog(null, "카카오톡 로그인 서버에 연결할 수 없습니다." + '\n' + "사용자 네트워크 연결이 불안정 하거나, 방화벽 등에 의해 카카오톡 사용이 차단된 환경일 수 있습니다." );
               }
               else {
            	   	input_id = id_text.getText();
                    input_password = password_text.getText();
                    try {
						crypto=sha256.encrypt(input_password);
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    
                    Thread cThread = new Thread(new KakaoClientThread(input_id, crypto));
                    cThread.setName("카카오클라이언트쓰레드");
                    cThread.start();
    
                    jf.setVisible(false); 
               }
            } 
        });

    }
}
