import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;

public class InitFrame {

	public static Connection connect;
	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
					
					connect = DriverManager.getConnection("jdbc:sqlserver://localhost:1443;databaseName=PCManager", "pcadmin", "1");
					
					if (connect != null) {
						Login login = new Login(connect);
						login.setVisible(true);						
						login.addWindowListener(new WindowAdapter() {

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
						throw new Exception();
					}

				} catch(Exception e){
		            e.printStackTrace();
		        }
			}
		});
	}

	public InitFrame() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
