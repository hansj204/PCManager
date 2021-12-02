import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;

public class Main {

	public static Connection connect;
	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
					
					connect = DriverManager.getConnection("jdbc:sqlserver://localhost:1443;databaseName=PCManager", "pcadmin", "1");	
					
					if (connect != null){
						MainPage window = new MainPage(connect); //new Login(connection); //
						window.setVisible(true);
						
						window.addWindowListener(new WindowAdapter() {

							@Override
							public void windowClosing(WindowEvent e) { 
								try {
									connect.close();
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
								System.exit(0);
								
							}

						});
						
					}else {
//						System.out.println(22);
					}

				} catch(ClassNotFoundException e){
		            System.out.println("드라이버 로딩 실패");
		        } catch(Exception e){
		            System.out.println("에러: " + e.getMessage());
		            //e.printStackTrace();
		        }
			}
		});
	}

	public Main() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
