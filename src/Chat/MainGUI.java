package Chat;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// 계속해서 다른 기능(e.g., 채팅기능)을 수행하는건 Main GUI에서 한다! 

public class MainGUI {

   private JFrame frame;

   // GUI
   Color b = new Color(85, 102, 119);
   Color c = new Color(102, 153, 204);
   Font font = new Font("Aharoni 굵게", Font.BOLD, 30);
   Font font1 = new Font("Aharoni 굵게", Font.BOLD, 15);
   Font font2 = new Font("Aharoni 굵게 ", Font.BOLD, 20);
   Font font3 = new Font("Aharoni 굵게", Font.BOLD, 12);
   String myName = null;
   String myNickname = null;
   String myTw = null;

   String movieName = null;
   String movieDate;
   String movieDirect = null;
   String movieGerne = null;
   String movieActor = null;
   String genreNm = null;
   String movietime;

   String outMsg = "MainGUI";
   String counterID = null;
   String counterWord = null;
   String counterState = null;

   static Statement stmt = null;
   static Statement stmt2 = null;
   static Statement stmt3 = null;
   static ResultSet rs = null;
   static ResultSet rs2 = null;
   static ResultSet rs3 = null;
   static PreparedStatement pstm = null;

   private Image home_img = new ImageIcon(LoginGUI.class.getResource("../img/home.png")).getImage();
   private Image search_img = new ImageIcon(LoginGUI.class.getResource("../img/search.png")).getImage();
   private Image logout_img = new ImageIcon(LoginGUI.class.getResource("../img/logout.png")).getImage();
   // User Info
   String user_id = null;

   /**
    * Launch the application.
    */
//   public static void main(String[] args) {
//      EventQueue.invokeLater(new Runnable() {
//         public void run() {
//            try {
//               mainTemp window = new mainTemp("asdf");
//               window.frame.setVisible(true);
//            } catch (Exception e) {
//               e.printStackTrace();
//            }
//         }
//      });
//   }

   /**
    * Create the application.
    */
   public MainGUI(String id) {
      this.user_id = id;
      initialize();
   }

   /**
    * Initialize the contents of the frame.
    */
   private void initialize() {

      /*
       * 유저 정보 서버에서 읽어오기
       */
      getUserInfo();

      /*
       * GUI 초기화
       */
      frame = new JFrame();
      frame.setBounds(100, 100, 400, 640);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().setLayout(null);

      JPanel sideBar = new JPanel();
      sideBar.setBounds(0, 0, 78, 500);
      sideBar.setBackground(new Color(213, 213, 213));
      frame.getContentPane().add(sideBar);
      sideBar.setLayout(null);
      
      Image changeImg = home_img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
      ImageIcon changeIcon = new ImageIcon(changeImg);
      JButton homeBtn = new JButton(changeIcon);
      homeBtn.setBounds(12, 26, 55, 55);
      homeBtn.setBorderPainted(false);
//      homeBtn.setContentAreaFilled(false);
      homeBtn.setBackground(new Color(213, 213, 213));
      homeBtn.setForeground(new Color(255, 255, 255));
      sideBar.add(homeBtn);

      homeBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            new MainGUI(user_id);
            frame.setVisible(false);
         }
      });
      
      Image logoutImage = logout_img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
      ImageIcon logoutIcon = new ImageIcon(logoutImage);
      JButton logoutBtn = new JButton(logoutIcon);
      logoutBtn.setBorderPainted(false);
      logoutBtn.setBackground(new Color(213, 213, 213));
      logoutBtn.setForeground(new Color(255, 255, 255));
      logoutBtn.setFont(new Font("굴림", Font.PLAIN, 8));
      logoutBtn.setBounds(12, 435, 55, 55);
      sideBar.add(logoutBtn);

      JPanel main = new JPanel();
      main.setBounds(78, 0, 308, 500);
      main.setBackground(new Color(246, 246, 246));
      frame.getContentPane().add(main);
      main.setLayout(null);

//      JButton prof = new JButton("profile");
//      prof.setFont(new Font("굴림", Font.PLAIN, 8));
//      prof.setBounds(12, 250, 55, 55);
//      sideBar.add(prof);
//      prof.addActionListener(new ActionListener() {
//         @Override
//         public void actionPerformed(ActionEvent e) {
//            try {
//               new profile(user_id);
//            } catch (SQLException e1) {
//               // TODO Auto-generated catch block
//               e1.printStackTrace();
//            }
//         }
//      });
      Image searchImage = search_img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
      ImageIcon searchIcon = new ImageIcon(searchImage);
      JButton search = new JButton(searchIcon);
      search.setBorderPainted(false);
      search.setBackground(new Color(213, 213, 213));
      search.setForeground(new Color(255, 255, 255));
      search.setBounds(12, 130, 55, 55);
      sideBar.add(search);
      search.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               new search(user_id);
            } catch (SQLException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }
      });

      JPanel publicData = new JPanel();
      publicData.setBounds(0, 500, 386, 103);
      frame.getContentPane().add(publicData);
      publicData.setLayout(null);

      String key = "393c763c33b0d66630da66155684bbd5";

      // 파싱한 데이터를 저장할 변수
      String result = "";

      try {

         URL url = new URL("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key="
               + key + "&movieCd=20225061");

         BufferedReader bf;

         bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

         result = bf.readLine();

         JSONParser jsonParser = new JSONParser();
         JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
         JSONObject movieInfoResult = (JSONObject) jsonObject.get("movieInfoResult");
         JSONObject movieInfo = (JSONObject) movieInfoResult.get("movieInfo");

         JSONArray nations = (JSONArray) movieInfo.get("nations");
         JSONObject nations_nationNm = (JSONObject) nations.get(0);

         JSONArray directors = (JSONArray) movieInfo.get("directors");
         JSONObject directors_peopleNm = (JSONObject) directors.get(0);

         JSONArray genres = (JSONArray) movieInfo.get("genres");

         JSONArray actors = (JSONArray) movieInfo.get("actors");

         movieName = (String) movieInfo.get("movieNm");
         movieDate = (String) movieInfo.get("openDt");
         movietime = (String) movieInfo.get("showTm");
         movieDirect = (String) directors_peopleNm.get("peopleNm");

         for (int i = 0; i < genres.size(); i++) {
            JSONObject genres_genreNm = (JSONObject) genres.get(i);
            genreNm += genres_genreNm.get("genreNm") + " ";
         }

      } catch (Exception e) {
         e.printStackTrace();
      }

      JLabel movie = new JLabel("<최신 상영작>");
      movie.setBounds(10, 0, 149, 60);
      publicData.add(movie);

      JLabel lblNewLabel = new JLabel("영화이름: " + movieName);
      lblNewLabel.setBounds(10, 20, 149, 60);
      publicData.add(lblNewLabel);

      JLabel Director = new JLabel("감독: " + movieDirect);
      Director.setBounds(200, 20, 149, 60);
      publicData.add(Director);

      JLabel Date = new JLabel("개봉일: " + movieDate);
      Date.setBounds(10, 50, 149, 60);
      publicData.add(Date);

      JLabel time = new JLabel("상영시간: " + movietime + "분 ");
      time.setBounds(200, 50, 149, 60);
      publicData.add(time);

      /*
       * 유저의 친구 정보 서버에서 읽어오기
       */
      String data[][] = new String[100][3];

      data = getFollowerInfo(user_id);
      String columnTitle[] = { "Name", "Today's Word", "Current Status" };

      JTable table = new JTable(data, columnTitle) {
         public boolean isCellEditable(int row, int column) {
            return false;
         }
      };
      table.setRowHeight(40);
      JScrollPane scroll = new JScrollPane(table);
      scroll.setBounds(5, 106, 300, 394);
      // scroll.setLocation(90,100);
      main.add(scroll);

      JPanel myProfile = new JPanel();
      myProfile.setBounds(5, 5, 303, 66);
      main.add(myProfile);
      myProfile.setLayout(null);

      JPopupMenu popupMenu = new JPopupMenu();
      addPopup(myProfile, popupMenu);

      JButton modifyBtn = new JButton("프로필 수정하기");
      popupMenu.add(modifyBtn);
      modifyBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               new profile(user_id);
            } catch (SQLException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }
      });

      JLabel nameLbl = new JLabel(myName);
      nameLbl.setBounds(12, 10, 96, 30);
      myProfile.add(nameLbl);

      JLabel nicknameLbl = new JLabel("@" + myNickname);
      nicknameLbl.setFont(new Font("굴림", Font.PLAIN, 10));
      nicknameLbl.setBounds(12, 43, 77, 15);
      myProfile.add(nicknameLbl);

      JLabel todaywordLbl = new JLabel(myTw);
      todaywordLbl.setBounds(112, 17, 179, 38);
      myProfile.add(todaywordLbl);

      JLabel friend = new JLabel("친구");
      friend.setBounds(5, 81, 77, 23);
      main.add(friend);

      /*
       * 
       * Chatting 요청 관련
       * 
       * 
       */
      table.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            outMsg = null;
            if (e.getButton() == 1) {
               int row = table.getSelectedRow();
               counterID = (String) table.getModel().getValueAt(row, 0);
               counterWord = (String) table.getModel().getValueAt(row, 1);
               counterState = (String) table.getModel().getValueAt(row, 2);
               System.out.println("클릭" + counterState);
               // 여기서 채팅
               
            }
         }
      });

      JPopupMenu popupFriend = new JPopupMenu();
      addPopup(table, popupFriend);

      JButton chatBtn = new JButton("채팅하기");
      chatBtn.addActionListener(new ActionListener() {
    	  Socket socket = null;
          public void actionPerformed(ActionEvent e) {
             // 채팅
        	  int Rport = 0;

             if (counterState.equals("active")) {

                try  {
                   try (Connection con = JDBC.connection()){
 						stmt = con.createStatement();
 						String searchport = "select port from getchatreqinfo where user_id =(select user_id from user where name = \"" + counterID + "\")";
 						rs = stmt.executeQuery(searchport);
 						if(rs.next()) {
 							Rport = rs.getInt(1);
 							 Socket socket = new Socket("localhost", Rport);
 			                  BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 			                  PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
 						}
 						socket = new Socket("localhost", Rport);
 						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 			            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
 			            String inMsg = null;
 			            String outMsg = null;
 			            
 						System.out.println("MainGUI 371: " + socket);
 						
 						System.out.println("MainGUI");
 						
 						ChatRoomGUI chatRoomGUI = new ChatRoomGUI();
 						ChatRoomGUI chatRoomGUI2 = new ChatRoomGUI();
 								outMsg = chatRoomGUI.getConversation();
 	 			            	chatRoomGUI.addConversation(outMsg);
 	 			            	out.write(outMsg + "\n");
 	 			            	out.flush();
 	 			            	inMsg = in.readLine();            	
 	 			            	chatRoomGUI.addConversation(inMsg);
 							}
 							
 			            	
 			            
 			        
 						
 					} catch (SQLException e2) {
 						e2.printStackTrace();
 					}

                catch (IOException IOe) {
                   IOe.printStackTrace();
                }
             }
          }
       });
      JButton fileBtn = new JButton("파일전송");
      fileBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            // 파일전송
         }
      });
      JButton infoBtn = new JButton("상세정보");
      infoBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               new Sprofile(counterID);
            } catch (SQLException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }
      });
      JButton gameBtn = new JButton("게임하기");
      gameBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            // 채팅
         }
      });
      popupFriend.add(chatBtn);
      popupFriend.add(fileBtn);
      popupFriend.add(infoBtn);
      popupFriend.add(gameBtn);

      logoutBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try (Connection con = JDBC.connection()) {
               String unactive = "unactive";
               String out = "Update user set status = \'" + unactive + "\' where user_id = \'" + user_id + "\'";
               pstm = con.prepareStatement(out);
               pstm.executeUpdate();

            } catch (SQLException E) {
               E.printStackTrace();
            }
            System.exit(0);
         }
      });
      frame.setVisible(true);
   }

   // Main화면이 바뀌어야 할 때 사용
   public void disposeWindow() {
      frame.dispose();
   }

   private static void addPopup(Component component, final JPopupMenu popup) {
      component.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
               showMenu(e);
            }
         }

         public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
               showMenu(e);
            }
         }

         private void showMenu(MouseEvent e) {
            popup.show(e.getComponent(), e.getX(), e.getY());
         }
      });
   }

   /* MainGUI Initialization */
   private void getUserInfo() {
      try  {
    	 Socket socket = new Socket("localhost", 8888);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

         String outMsg = createGetUserInfoReqMsg(user_id);
         out.write(outMsg);
         out.flush();

         String inMsg = in.readLine();
         String[] userInfo = inMsg.split("/");
         myName = userInfo[0];
         myNickname = userInfo[1];
         myTw = userInfo[2];

         // 3-way handshake로 해야하나
         // 서버에게 TCP 연결 해제 요청을 보낸다.
         outMsg = createDisconnectMsg();
         out.write(outMsg);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private String[][] getFollowerInfo(String id) {
      String data[][] = new String[100][3];
      BufferedReader in2 = null;
      PrintWriter out2 = null;
      String inMsg = null;
      String outMsg = null;

      try  {
    	  Socket socket = new Socket("localhost", 8888);
         in2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         out2 = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

         outMsg = createGetFollowerInfoReqMsg(id);
         out2.write(outMsg);
         out2.flush();

         inMsg = in2.readLine();

         String[] token = inMsg.split("/");
         int num = Integer.valueOf(token[0]);
         System.out.println("count- " + num);

         int j = 1;
         for (int i = 0; i < num; i++) {
            data[i][0] = token[j];
            data[i][1] = token[j + 1];
            data[i][2] = token[j + 2];
            j = j + 3;
         }

         // 3-way handshake로 해야하나
         // 서버에게 TCP 연결 해제 요청을 보낸다.
         outMsg = createDisconnectMsg();
         out2.write(outMsg);

      } catch (IOException e) {
         e.printStackTrace();
      }

      return data;
   }

   /* Create Request Msg */
   private String chatReq(String counterID) {
 
      return "CHATWITH/" + counterID + "\n";
   }

   String createDisconnectMsg() {
      return "FIN" + "\n";
   }

   String createGetFollowerInfoReqMsg(String id) {
      return "FollowerInfo/" + id + "\n";
   }

   String createGetUserInfoReqMsg(String id) {
      return "UserInfo/" + id + "\n";
   }
}