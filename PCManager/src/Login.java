import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JPanel contentPane;
	private Connection connect;
	private JTextField userId;
	private JPasswordField password;
	private JLabel errorLabel;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	
	public Login(Connection connect) {
		
		this.connect = connect;
		
		ImageIcon icon = new ImageIcon("img/setting.png");
		setIconImage(icon.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setBounds((int)screenSize.width / 2 - 156, (int)screenSize.getHeight() / 2- 189, 317, 378);		
		setTitle("로그인");
		setResizable(false);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		userId = new JTextField();
    	userId.setBounds(52, 150, 205, 32);
    	userId.setColumns(10);
    	userId.addKeyListener(new KeyAdapter() {
    		@Override
    		public void keyPressed(KeyEvent e) {
    			if (e.getKeyCode()==KeyEvent.VK_ENTER){
    				checkAdmin();
    	        }
    		}
		});
    	contentPane.add(userId);
    	
    	password = new JPasswordField();
    	password.setColumns(10);
    	password.setBounds(52, 214, 205, 32);
    	password.addKeyListener(new KeyAdapter() {
    		@Override
    		public void keyPressed(KeyEvent e) {
    			if (e.getKeyCode()==KeyEvent.VK_ENTER){
    				checkAdmin();
    	        }
    		}
		});
    	contentPane.add(password);
    	
    	errorLabel = new JLabel();
    	errorLabel.setBounds(52, 245, 205, 19);
    	errorLabel.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
    	errorLabel.setForeground(Color.RED);
    	contentPane.add(errorLabel);
    	
    	RoundedButton okBtn = new RoundedButton("확인");
    	okBtn.setText("로그인");
    	okBtn.setBounds(52, 274, 205, 32);
    	okBtn.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
    	contentPane.add(okBtn);    	
    	
    	Image img = new ImageIcon("img/setting.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH ); 
    	lblNewLabel = new JLabel(new ImageIcon(img));
    	lblNewLabel.setBounds(111, 31, 80, 80);    	
    	contentPane.add(lblNewLabel);
    	
    	lblNewLabel_1 = new JLabel("아이디");
    	lblNewLabel_1.setFont(new Font("나눔스퀘어", Font.PLAIN, 13));
    	lblNewLabel_1.setBounds(52, 133, 57, 15);
    	contentPane.add(lblNewLabel_1);
    	
    	lblNewLabel_2 = new JLabel("비밀번호");
    	lblNewLabel_2.setFont(new Font("나눔스퀘어", Font.PLAIN, 13));
    	lblNewLabel_2.setBounds(52, 197, 57, 15);
    	contentPane.add(lblNewLabel_2);
    	okBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				checkAdmin();
			}
		});
	}
	
	private void checkAdmin() {
		String checkAdmin = "SELECT * FROM EMPLOYEE E WHERE USERID = '"+ userId.getText() +"' AND PASSWORD = '"+ password.getText()+"' AND E.DEPARTMENTCODE = 'EP';";
		JFrame _this = this;
		
		try(Statement state = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); ResultSet rs =state.executeQuery(checkAdmin)) {
			
			rs.last();
			
			if(1 != rs.getRow()) {
				errorLabel.setText("로그인에 실패하였습니다.");
				return;
			}
			
			_this.setVisible(false);
			MainPage window = new MainPage(connect);
			window.setVisible(true);
			
			window.addWindowListener(new WindowAdapter() {						
				@Override
				public void windowClosing(WindowEvent e) { 
					try {
						connect.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally {
						System.exit(0);
					}
				}

			});
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
