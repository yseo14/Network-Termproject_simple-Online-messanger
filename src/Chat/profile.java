package Chat;

import javax.swing.*;
import java.net.Socket;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.*;

import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.sql.*;

public class profile
{
	
	
    public profile(String id) throws SQLException
    {
        JFrame profile = new JFrame();
        profile.setSize(500,350);
        profile.setLocationRelativeTo(null);
        profile.setTitle("profile");
        profile.setLayout(null);
        Color b = new Color(204,229,255);
        Color K = new Color(247,230,0);
        Color L=new Color(255,255,255);
        Color g = new Color(58, 29, 29);
        profile.getContentPane().setBackground(L);

        Font font1 = new Font("Aharoni 굵게",Font.BOLD,22);
        Font font2 = new Font("Aharoni 굵게",Font.BOLD,12);
        Font font3 = new Font("Aharoni 굵게",Font.BOLD,13);
        String user_id = "";
        String name = "";
        String create_time = "";
        String birth = "";
        String today_words = "";
        BufferedReader in = null;
        PrintWriter out = null;
        Socket socket = null;
        
        //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        try (Connection con = JDBC.connection())
        {
            JOptionPane message = new JOptionPane();
            

            Statement stmt = null;
            ResultSet rs = null;
            

            stmt = con.createStatement(); 

            String s4 = "select user_id,name,create_at, birth, today_words from user where user_id = \'" + id + "\'";
            rs = stmt.executeQuery(s4);

         
            
            if(rs.next())
          {
                user_id = rs.getString("user_id");
                name = rs.getString("name");
                create_time = rs.getString("create_at");
                birth = rs.getString("birth");
                today_words = rs.getString("today_words");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
     

            JLabel profile_id = new JLabel(user_id);
            profile_id.setSize(100,25);
            profile_id.setLocation(20,20);
            profile_id.setFont(font1);
            profile.add(profile_id);

            JLabel profile_name = new JLabel("@" + name);
            profile_name.setSize(80,25);
            profile_name.setLocation(20,45);
            profile_name.setFont(font2);
            profile_name.setForeground(new Color(128,128,128));
            profile.add(profile_name);
            
            JLabel profile_TW = new JLabel("상태 메세지: " + today_words);
            profile_TW.setSize(200,25);
            profile_TW.setLocation(47,75);
            profile_TW.setFont(font3);
            profile_TW.setForeground(new Color(128,128,128));
            profile.add(profile_TW);
            
            JLabel TWC = new JLabel("상태 메세지: ");
            TWC.setSize(200,25);
            TWC.setLocation(20,215);
            TWC.setFont(font3);
            TWC.setForeground(new Color(128,128,128));
            profile.add(TWC);
            
            JTextField TW_text = new JTextField();
            TW_text.setSize(150,30);
            TW_text.setLocation(120,215);
            profile.add(TW_text);
            
            JButton change = new JButton("수정");
            change.setSize(75,25);
            change.setLocation(280,215);
            change.setFont(font3);
            change.setOpaque(true);
            change.setBorderPainted(false);
            profile.add(change);
            change.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                	 try (Connection con = JDBC.connection())
                     {
				        	PreparedStatement pstm = null;
				        	String input_TW = TW_text.getText();
				        
		                	String s1 = "update user set today_words = \'" + input_TW + "\' where user_id = \'" + id + "\' ";
		                	
						    pstm = con.prepareStatement(s1);
							pstm.executeUpdate();
							profile.setVisible(false);
							new profile(id);
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                }
            });
            
            JLabel NC = new JLabel("닉네임: ");
            NC.setSize(200,25);
            NC.setLocation(20,265);
            NC.setFont(font3);
            NC.setForeground(new Color(128,128,128));
            profile.add(NC);
            
            JTextField NC_text = new JTextField();
            NC_text.setSize(150,30);
            NC_text.setLocation(100,265);
            profile.add(NC_text);
            
            JButton changeN = new JButton("수정");
            changeN.setSize(75,25);
            changeN.setLocation(260,265);
            changeN.setFont(font3);
            changeN.setOpaque(true);
            changeN.setBorderPainted(false);
            profile.add(changeN);
            change.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                	 try (Connection con = JDBC.connection())
                     {
				        	PreparedStatement pstm = null;
				        	String input_NC = NC_text.getText();
				        
		                	String s1 = "update user set nickname = \'" + input_NC + "\' where user_id = \'" + id + "\' ";
		                	
						    pstm = con.prepareStatement(s1);
							pstm.executeUpdate();
							
							new profile(id);
							profile.setVisible(false);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                }
            });
            
            JLabel profile_create = new JLabel("가입날짜: " + create_time);
            profile_create.setSize(250,50);
            profile_create.setLocation(47,80);
            profile_create.setFont(font3);
            profile_create.setForeground(new Color(128,128,128));
            profile.add(profile_create);
            
            JLabel profile_birth = new JLabel("생년월일: " + birth);
            profile_birth.setSize(200,25);
            profile_birth.setLocation(47,130);
            profile_birth.setFont(font3);
            profile_birth.setForeground(new Color(128,128,128));
            profile.add(profile_birth);

        JButton back = new JButton("뒤로가기");
        back.setSize(90,25);
        back.setLocation(400,20);
        back.setFont(font3);
        back.setBackground(g);
        back.setForeground(L);
        back.setOpaque(true);
        back.setBorderPainted(false);
        profile.add(back);

        back.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                profile.setVisible(false);
            }
        });

       

       profile.setVisible(true);
    }
}
