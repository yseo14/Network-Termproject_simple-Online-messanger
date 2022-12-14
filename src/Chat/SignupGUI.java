package Chat;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class SignupGUI {

	private JFrame frame;
	private JTextField id_TF;
	private JTextField name_TF;
	private JTextField nickname_TF;
	private JTextField birth_TF;
	private JPasswordField pwField;
	private JTextField email_TF;
    private JTextField phoneNum_TF;
    
	String crypto=null;
	JOptionPane message = new JOptionPane();
	// DB
    Statement stmt = null;
    ResultSet rs = null;
    PreparedStatement pstm = null;
    

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SignupGUI window = new SignupGUI();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	public SignupGUI() {
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		SHA256 sha256=new SHA256();
		frame = new JFrame();
		frame.setTitle("회원가입");
		frame.getContentPane().setBackground(new Color(247,230,0));
		frame.setResizable(false);
		frame.setBounds(100, 100, 350, 490);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel signin = new JLabel("회원가입");
		signin.setBounds(25, 35, 101, 31);
		signin.setFont(new Font("Aharoni 굵게",Font.BOLD,17));
		frame.getContentPane().add(signin);
		
		JLabel id = new JLabel("아이디: ");
		id.setBounds(38, 92, 52, 23);
		frame.getContentPane().add(id);
		
		id_TF = new JTextField();
		id_TF.setBounds(103, 89, 140, 30);
		frame.getContentPane().add(id_TF);
		id_TF.setColumns(10);
		
		JButton dupButton = new JButton("중복확인");
		dupButton.setForeground(new Color(255, 255, 255));
		dupButton.setBackground(new Color(58, 29, 29));
		dupButton.setFont(new Font("굴림", Font.PLAIN, 8));
		dupButton.setBounds(244, 94, 76, 23);
		frame.getContentPane().add(dupButton);
		
		JLabel pw = new JLabel("비밀번호: ");
		pw.setBounds(38, 138, 65, 23);
		frame.getContentPane().add(pw);
		
		pwField = new JPasswordField();
		pwField.setBounds(103, 135, 140, 30);
		frame.getContentPane().add(pwField);
		//비밀번호 암호화
//		try {
//			crypto=sha256.encrypt(String.valueOf(passwordField.getPassword()));
//			System.out.println(crypto);
//		} catch (NoSuchAlgorithmException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		JLabel name = new JLabel("이름: ");
		name.setBounds(38, 184, 65, 23);
		frame.getContentPane().add(name);
		
		name_TF = new JTextField();
		name_TF.setColumns(10);
		name_TF.setBounds(103, 181, 140, 30);
		frame.getContentPane().add(name_TF);
		
		JLabel nickname = new JLabel("닉네임: ");
		nickname.setBounds(38, 230, 65, 23);
		frame.getContentPane().add(nickname);
		nickname_TF = new JTextField();
		nickname_TF.setColumns(10);
		nickname_TF.setBounds(103, 227, 140, 30);
		
		frame.getContentPane().add(nickname_TF);
		JLabel birth = new JLabel("생년월일: ");
		birth.setBounds(38, 278, 65, 23);
		frame.getContentPane().add(birth);
		
		birth_TF = new JTextField();
		birth_TF.setColumns(10);
		birth_TF.setBounds(103, 275, 140, 30);
		frame.getContentPane().add(birth_TF);
		
		JLabel email = new JLabel("이메일: ");
		email.setBounds(38, 326, 65, 23);
		frame.getContentPane().add(email);
		
		email_TF = new JTextField();
		email_TF.setColumns(10);
		email_TF.setBounds(103, 323, 140, 30);
		frame.getContentPane().add(email_TF);
		
		JLabel phoneNum = new JLabel("전화번호: ");
		phoneNum.setBounds(38, 375, 65, 23);
		frame.getContentPane().add(phoneNum);
		
		phoneNum_TF = new JTextField();
		phoneNum_TF.setColumns(10);
		phoneNum_TF.setBounds(103, 372, 140, 30);
		frame.getContentPane().add(phoneNum_TF);

		JButton signup_Button = new JButton("가입하기");
		signup_Button.setForeground(new Color(255, 255, 255));
		signin.setFont(new Font("Aharoni 굵게",Font.BOLD,20));
		signup_Button.setBackground(new Color(58, 29, 29));
		signup_Button.setBounds(210, 412, 110, 31);
		frame.getContentPane().add(signup_Button);
		
		dupButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try (Connection conn=JDBC.connection()){
					stmt = conn.createStatement();
					String dupCheck="select user_id from user where user_id=\'"+id_TF.getText()+ "\'";
					System.out.println(dupCheck);

					rs = stmt.executeQuery(dupCheck);
					if(rs.next()) {
						message.showMessageDialog(null,"중복된 아이디입니다.");
						id_TF.setText("");
					}
					else {
						message.showMessageDialog(null,"사용가능한 아이디입니다.");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		signup_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try(Connection conn=JDBC.connection())
				{
					crypto=sha256.encrypt(String.valueOf(pwField.getPassword()));
//					System.out.println(crypto);
					String signup = "insert into user values (\'" + id_TF.getText() + "\', \'" + crypto + "\', \'" + name_TF.getText() + "\', \'" + nickname_TF.getText() + "\', \'" + email_TF.getText()+ "\', \'"+phoneNum_TF.getText()+"\', \'"+birth_TF.getText()+"\', NULL,NULL,now(),\'"+"unactive"+"\',\'"+0+"\')";
					System.out.println(signup);
					
					pstm = conn.prepareStatement(signup);
					pstm.executeUpdate();
					
					frame.setVisible(false);
				}
				catch(SQLException e1)
				{
					e1.printStackTrace();
				}
				 catch (NoSuchAlgorithmException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
			}
		});
		
		frame.setVisible(true);
	}
}
