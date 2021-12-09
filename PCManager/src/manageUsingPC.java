import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class manageUsingPC extends JFrame {

	private JPanel contentPane;
	HashMap<String, String> pcTypeList = new HashMap<>();
	
	private String userId;
	private Connection connect;
	private RoundedButton searchBtn;
	
	JTable userPCTable;
	DefaultTableModel userPCTableModel;
	addUsingPC usingPC = null;

	Font font = new Font("나눔스퀘어", Font.PLAIN, 12);
	public final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public manageUsingPC(Connection connect, String userId, RoundedButton searchBtn) {
		this.connect= connect;
		this.userId = userId;
		this.searchBtn = searchBtn;
		
		setUsingPC();
		insert();
		delete();
	}
	
	private void setUsingPC() {
		ImageIcon icon = new ImageIcon("img/setting.png");
		setIconImage(icon.getImage());
		setTitle("사용중인 PC 장비 목록");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(750, 440, 414, 332);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		addWindowListener(new WindowAdapter() {						
			@Override
			public void windowClosing(WindowEvent e) { 
				if(null != usingPC)usingPC.dispose();
			}
		});
		
		//사용자 - 이용 컴퓨터 목록				
		userPCTableModel = new DefaultTableModel(new String[]{"시리얼 넘버", "PC 종류", "PC 이름"}, 0);
		
		userPCTable = new JTable(userPCTableModel);
		
		userPCTable.setBackground(Color.WHITE);
		userPCTable.setFont(font);
		userPCTable.setSelectionBackground(Color.LIGHT_GRAY);
		userPCTable.setRowHeight(20);
		
		JScrollPane pcScrollPane = new JScrollPane(userPCTable);
		pcScrollPane.setBounds(12, 45, 374, 206);
		pcScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pcScrollPane.getViewport().setBackground(Color.WHITE);
		contentPane.add(pcScrollPane);
		
		pcTypeList.put("m", "모니터");
		pcTypeList.put("p", "PC");
		pcTypeList.put("n", "노트북");
		
    	try(Statement state= connect.createStatement(); ResultSet rs =state.executeQuery("SELECT PE.* FROM PC_USERS PU "
		+ "JOIN PC_EQUIPMENT PE ON PE.SERIALNUM = PU.SERIALNUM WHERE PU.USERID = '"+ userId +"';")) {

			userPCTableModel.setRowCount(0);
			
			while(rs.next()) {

				String pcType = "";
				
				for(String key : pcTypeList.keySet()) {
					if(key.equals(rs.getString("PCTYPE"))) pcType = pcTypeList.get(key);
				}
				
				userPCTableModel.addRow(new Object[]{ rs.getString("SERIALNUM"), pcType, rs.getString("PCNAME") });
			}
				
		} catch (Exception e1) {
			new WarnAlert(e1.getMessage());
		}
    	
    	RoundedButton okBtn = new RoundedButton("확인");
    	okBtn.setFont(font);
		okBtn.setBounds(304, 268, 82, 25);
		contentPane.add(okBtn);
		okBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Boolean error = false;
				ArrayList<String> propertyList = new ArrayList<>();
				
				for(int i = 0; i < userPCTableModel.getRowCount(); i++) {
					propertyList.add((String) userPCTableModel.getValueAt(i, 0));
				}
				
				try(PreparedStatement ps = connect.prepareStatement("DELETE FROM PC_USERS WHERE USERID = ?;")) {					
					ps.setString(1, userId);
					ps.executeUpdate();
															
				} catch (Exception e1) {
					error = false;
					new WarnAlert(e1.getMessage()).setVisible(true);
					e1.printStackTrace();
				}
				
				if(!error) {
					for(String property : propertyList) {
						try(PreparedStatement ps = connect.prepareStatement("INSERT INTO PC_USERS VALUES (?,?)")) {				
							ps.setString(1, userId);
							ps.setString(2, (String) property);
							int res = ps.executeUpdate();
							
							if (res == 1) {
								new NormalAlert("저장되었습니다.").setVisible(true);														
								searchBtn.doClick();
							}
							
						} catch (SQLException e1) {
							new WarnAlert(e1.getMessage()).setVisible(true);
							e1.printStackTrace();
						}
					}
				}
				
				dispose();
			}
		});
	}
	
	protected void addProperty(String propery) {
		try(Statement state= connect.createStatement(); ResultSet rs =state.executeQuery("SELECT * FROM PC_EQUIPMENT WHERE SERIALNUM = '"+propery+"';")) {
			
			while(rs.next()) {

				String pcType = "";
				
				for(String key : pcTypeList.keySet()) {
					if(key.equals(rs.getString("PCTYPE"))) pcType = pcTypeList.get(key);
				}
				
				userPCTableModel.addRow(new Object[]{ rs.getString("SERIALNUM"), pcType, rs.getString("PCNAME") });
			}
		} catch (Exception e1) {
			System.out.println(e1.getCause());
		}
	}
	
	private void insert() {
		
		manageUsingPC frame = this;
		
		Image img = new ImageIcon("img/plus.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH ); 
		JLabel plusBtn = new JLabel(new ImageIcon(img));
		plusBtn.setBounds(20, 15, 20, 20);
		contentPane.add(plusBtn);		
		plusBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if(null != usingPC)usingPC.dispose();
					usingPC = new addUsingPC(frame, userId, connect);
					usingPC.setVisible(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	private void delete() {
		Image img = new ImageIcon("img/minus.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH ); 
		JLabel deleteBtn = new JLabel(new ImageIcon(img));
		deleteBtn.setBounds(50, 15, 20, 20);
		contentPane.add(deleteBtn);
			
		deleteBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				userPCTableModel.removeRow(userPCTable.getSelectedRow());
			}
		});
	}
}
