package Chat;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
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

public class mainTemp {

   private JFrame frame;

   Socket socket = null;
   BufferedReader in = null;
   PrintWriter out = null;
   Color b = new Color(85, 102, 119);
   Color c = new Color(102, 153, 204);
   Font font = new Font("Aharoni 굵게", Font.BOLD, 30);
   Font font1 = new Font("Aharoni 굵게", Font.BOLD, 15);
   Font font2 = new Font("Aharoni 굵게 ", Font.BOLD, 20);
   Font font3 = new Font("Aharoni 굵게", Font.BOLD, 12);
   String myName=null;
   String myNickname=null;
   String myTw=null;
   
   
   // req
   String outMsg = null;
   String inMsg = null;
   String movieName = null;
   String movieDate;
   String movieDirect = null;
   String movieGerne = null;
   String movieActor = null;
   String genreNm = null;
   String movietime;
   
   static Statement stmt = null;
   static Statement stmt2 = null;
   static Statement stmt3 = null;
   static ResultSet rs = null;
   static ResultSet rs2 = null;
   static ResultSet rs3 = null;
   static PreparedStatement pstm = null;

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
   public mainTemp(String id) {
      initialize(id);
   }

   /**
    * Initialize the contents of the frame.
    */
   private void initialize(String id) {

      try {
         socket = new Socket("Localhost", 8888);
         in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
         out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
      } catch (IOException e) {
         e.printStackTrace();
      }
//      유저 정보 DB에서 읽어오기   
      try (Connection con = JDBC.connection()) {
         stmt = con.createStatement();
         String getProfile = "select name,nickname,today_words from user where user_id = \'" + id + "\' ";
         rs = stmt.executeQuery(getProfile);
         while(rs.next()) {
            System.out.println(rs.getString(1)+","+rs.getString(1)+","+rs.getString(2)+",");
            myName=rs.getString("name");
            myNickname=rs.getString("nickname");
            myTw=rs.getString("today_words");
            
         }
      } catch (SQLException E) {
         E.printStackTrace();
      }

      frame = new JFrame();
      frame.setBounds(100, 100, 400, 640);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().setLayout(null);

      JPanel sideBar = new JPanel();
      sideBar.setBounds(0, 0, 78, 500);
      sideBar.setBackground(new Color(213, 213, 213));
      frame.getContentPane().add(sideBar);
      sideBar.setLayout(null);

      JButton homeBtn = new JButton("home");
      homeBtn.setFont(new Font("굴림", Font.PLAIN, 8));
      homeBtn.setBounds(12, 26, 55, 55);
      sideBar.add(homeBtn);

      JButton logoutBtn = new JButton("logout");
      logoutBtn.setFont(new Font("굴림", Font.PLAIN, 8));
      logoutBtn.setBounds(12, 435, 55, 55);
      sideBar.add(logoutBtn);

      JPanel main = new JPanel();
      main.setBounds(78, 0, 308, 500);
      main.setBackground(new Color(246, 246, 246));
      frame.getContentPane().add(main);
      main.setLayout(null);

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
      	JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
      	JSONObject movieInfoResult = (JSONObject)jsonObject.get("movieInfoResult");
      	JSONObject movieInfo = (JSONObject)movieInfoResult.get("movieInfo");

      	JSONArray nations = (JSONArray)movieInfo.get("nations");
      	JSONObject nations_nationNm = (JSONObject)nations.get(0);

      	JSONArray directors = (JSONArray)movieInfo.get("directors");
      	JSONObject directors_peopleNm = (JSONObject)directors.get(0);

      	JSONArray genres = (JSONArray)movieInfo.get("genres");

      	JSONArray actors = (JSONArray)movieInfo.get("actors");

     	
      	 movieName = (String) movieInfo.get("movieNm");
      	 movieDate = (String) movieInfo.get("openDt");
      	 movietime = (String) movieInfo.get("showTm");
      	 movieDirect = (String) directors_peopleNm.get("peopleNm");
      	
     	
      	for(int i = 0; i < genres.size(); i++) {
          	JSONObject genres_genreNm = (JSONObject)genres.get(i);
          	genreNm += genres_genreNm.get("genreNm") + " ";
      	}
     	

  	}catch(Exception e) {
  		e.printStackTrace();
  	}
  	
  	 JLabel movie = new JLabel("<최신 상영작>");
  	 movie.setBounds(10, 0, 149, 60);
     publicData.add(movie);
     
      JLabel lblNewLabel = new JLabel("영화이름: "+ movieName);
      lblNewLabel.setBounds(10, 20, 149, 60);
      publicData.add(lblNewLabel);
      
      JLabel Director = new JLabel("감독: "+ movieDirect);
      Director.setBounds(200, 20, 149, 60);
      publicData.add(Director);
      
      JLabel Date = new JLabel("개봉일: " + movieDate);
      Date.setBounds(10, 50, 149, 60);
      publicData.add(Date);
      
      JLabel time = new JLabel("상영시간: "+ movietime+ "분 ");
      time.setBounds(200, 50, 149, 60);
      publicData.add(time);
      
      
      
      
      outMsg = getuserInfo(id);
      out.write(outMsg);
      out.flush();
      try {
         inMsg = in.readLine();
      } catch (IOException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }
      String[] token = inMsg.split("/");
      int num = Integer.valueOf(token[0]);
      System.out.println("count- " + num);
      
      String columnTitle[] = { "Name", "Today's Word", "Current Status" };
      String data[][] = new String[100][3];
      int j = 1;
      for (int i = 0; i < num; i++) {
         data[i][0] = token[j];
         data[i][1] = token[j + 1];
         data[i][2] = token[j + 2];
         j = j + 3;
      }
      JTable table = new JTable(data, columnTitle) {
         public boolean isCellEditable(int row, int column) {
            return false;
         }
      };
      table.setRowHeight(40);
      JScrollPane scroll = new JScrollPane(table);
      scroll.setBounds(5, 106, 300, 394);
//        scroll.setLocation(90,100);
      main.add(scroll);
      
      JPanel myProfile = new JPanel();
      myProfile.setBounds(5, 5, 303, 66);
      main.add(myProfile);
      myProfile.setLayout(null);
      
      JPopupMenu popupMenu = new JPopupMenu();
      addPopup(myProfile, popupMenu);
      
      JButton modifyBtn = new JButton("프로필 수정하기");
      modifyBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            //프로필 수정 gui
         }
      });
      popupMenu.add(modifyBtn);
      
      JLabel nameLbl = new JLabel(myName);
      nameLbl.setBounds(12, 10, 96, 30);
      myProfile.add(nameLbl);
      
      JLabel nicknameLbl = new JLabel("@"+myNickname);
      nicknameLbl.setFont(new Font("굴림", Font.PLAIN, 10));
      nicknameLbl.setBounds(12, 43, 77, 15);
      myProfile.add(nicknameLbl);
      
      JLabel todaywordLbl = new JLabel(myTw);
      todaywordLbl.setBounds(112, 17, 179, 38);
      myProfile.add(todaywordLbl);
      
      JLabel friend = new JLabel("친구");
      friend.setBounds(5, 81, 77, 23);
      main.add(friend);
      table.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            String outMsg = null;
            if (e.getButton() == 1) {
               int row = table.getSelectedRow();
               String counterID = (String) table.getModel().getValueAt(row, 0);
//               String counterWord = (String) table.getModel().getValueAt(row, 1);
               String counterState = (String) table.getModel().getValueAt(row, 2);
               /*
                * try { new profile(poid,word, state); } catch (SQLException e1) { // TODO
                * Auto-generated catch block e1.printStackTrace(); }
                */

               if (counterState.equals("active")) {
                  outMsg = chatReq(counterID);
                  out.write(outMsg);
                  out.flush();
               }
               try {
                  inMsg = in.readLine();
               } catch (IOException e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
               }
            }
         }
      });
      logoutBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try (Connection con = JDBC.connection()) {
               String unactive = "unactive";
               String out = "Update user set status = \'" + unactive + "\' where user_id = \'" + id + "\'";
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

   private String getuserInfo(String id) {
      String reqMsg = "UserInfo/" + id + "\n";
      return reqMsg;
   }

   private String chatReq(String counterID) {
      String reqMsg = "CHATWITH/" + counterID + "\n";
      return reqMsg;
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
}