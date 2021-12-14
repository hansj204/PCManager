import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class manageUsingPC extends JFrame {

	private JPanel contentPane;
	HashMap<String, String> pcTypeList = new HashMap<>();
	Font font = new Font("나눔스퀘어", Font.PLAIN, 12);
	public final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private String userId;
	private Connection connect;
	private RoundedButton searchBtn;
	
	JTable userPCTable;
	DefaultTableModel userPCTableModel;
	DefaultTableModel pcEQTableModel;
	JTable pcEQTable;
	
	public manageUsingPC() {}

	public manageUsingPC(Connection connect, String userId, RoundedButton searchBtn) {
		this.connect= connect;
		this.userId = userId;
		this.searchBtn = searchBtn;
		
		setUsingPC();
		upArrow();
		downArrow();
	}
	
	@SuppressWarnings("serial")
	private void setUsingPC() {
		ImageIcon icon = new ImageIcon("img/setting.png");
		setIconImage(icon.getImage());
		setTitle("사용중인 PC 장비 목록");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds((int)screenSize.width / 2 - 284, (int)screenSize.getHeight() / 2 - 200, 565, 501);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(null);
		setContentPane(contentPane);

		userPCTableModel = new DefaultTableModel(new String[]{"시리얼 넘버", "PC 종류", "PC 이름"}, 0){ 
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		userPCTable = new JTable(userPCTableModel);		
		userPCTable.setRowSelectionAllowed(true);
		userPCTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		userPCTable.setBackground(Color.WHITE);
		userPCTable.setFont(font);
		userPCTable.setSelectionBackground(Color.LIGHT_GRAY);
		userPCTable.setRowHeight(20);
		
		JScrollPane pcScrollPane = new JScrollPane(userPCTable);
		pcScrollPane.setBounds(22, 47, 515, 170);
		pcScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pcScrollPane.getViewport().setBackground(Color.WHITE);
		
		JLabel lblPc = new JLabel("사용중인 PC 장비 목록");
		lblPc.setFont(new Font("나눔스퀘어", Font.PLAIN, 13));
		lblPc.setBounds(22, 19, 155, 20);
		contentPane.add(lblPc);
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
    	
    	pcEQTableModel = new DefaultTableModel(new String[]{"시리얼 넘버", "PC 종류", "PC 이름"}, 0){ 
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};;
		
		pcEQTable = new JTable(pcEQTableModel);
		pcEQTable.setBackground(Color.WHITE);
		pcEQTable.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		pcEQTable.setRowHeight(20);
		pcEQTable.setRowSelectionAllowed(true);
		pcEQTable.setSelectionBackground(Color.LIGHT_GRAY);

		try(Statement state= connect.createStatement(); ResultSet rs =state.executeQuery("SELECT * FROM PC_EQUIPMENT WHERE SERIALNUM NOT IN (SELECT SERIALNUM FROM PC_USERS);")) {
    	
			pcEQTableModel.setRowCount(0);
			
			while(rs.next()) {
				Boolean overlap = false;
				String pcType = "";
				
				for(String key : pcTypeList.keySet()) {
					if(key.equals(rs.getString("PCTYPE"))) pcType = pcTypeList.get(key);
				}
				
				for(int idx = 0; idx < pcEQTableModel.getRowCount(); idx++) {
					if(pcEQTableModel.getValueAt(idx, 0).equals(rs.getString("SERIALNUM"))) overlap = true;					
				}
				
				if(overlap) continue;
				
				pcEQTableModel.addRow(new Object[]{ rs.getString("SERIALNUM"), pcType, rs.getString("PCNAME") });
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		JScrollPane pcAllScrollPane = new JScrollPane(pcEQTable);
		pcAllScrollPane.setBounds(22, 257, 515, 170);
		pcAllScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pcAllScrollPane.getViewport().setBackground(Color.WHITE);
		contentPane.add(pcAllScrollPane);
    	
    	RoundedButton okBtn = new RoundedButton("확인");
    	okBtn.setFont(font);
		okBtn.setBounds(455, 437, 82, 25);
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
					error = true;
					new WarnAlert(e1.getMessage()).setVisible(true);
					e1.printStackTrace();
				}
				
				if(!error) {
					for(String property : propertyList) {
						try(PreparedStatement ps = connect.prepareStatement("INSERT INTO PC_USERS VALUES (?,?)")) {				
							ps.setString(1, userId);
							ps.setString(2, (String) property);
							ps.executeUpdate();
							
						} catch (SQLException e1) {
							error = true;							
							new WarnAlert(e1.getMessage()).setVisible(true);
							e1.printStackTrace();
						}
					}
				}
				
				if(!error) {
					new NormalAlert("저장되었습니다.").setVisible(true);														
					searchBtn.doClick();
				}
				
				dispose();
			}
		});
	}
	
	private void upArrow() {		
		Image img = new ImageIcon("img/up-arrow.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH ); 
		JLabel upBtn = new JLabel(new ImageIcon(img));
		upBtn.setBounds(253, 229, 20, 20);
		contentPane.add(upBtn);		
		upBtn.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
								
				int[] selectedList = pcEQTable.getSelectedRows();
				Integer[] wrapperList = new Integer[pcEQTable.getSelectedRows().length];
				int j = 0;
				
				if(1 > selectedList.length) return;
				
				for(int selectedIdx : selectedList) {
				
					Object[] row = new Object[pcEQTable.getColumnCount()];
					
					for(int i = 0; i < pcEQTable.getColumnCount(); i++) {
						row[i] = pcEQTableModel.getValueAt(selectedIdx, i);
					}
					
					userPCTableModel.addRow(row);		
					wrapperList[j] = Integer.valueOf(selectedIdx);					
					j++;
				}
				
				Arrays.sort(wrapperList, Comparator.reverseOrder());
				
				for(int selectedIdx : wrapperList) {
					pcEQTableModel.removeRow(selectedIdx);
				}
			}
		});
	}
	
	private void downArrow() {
		Image img = new ImageIcon("img/down-arrow.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH ); 
		JLabel downBtn = new JLabel(new ImageIcon(img));
		downBtn.setBounds(283, 229, 20, 20);
		contentPane.add(downBtn);
		
		JLabel lblNewLabel = new JLabel("사용 가능한 PC 장비 목록");
		lblNewLabel.setFont(new Font("나눔스퀘어", Font.PLAIN, 13));
		lblNewLabel.setBounds(22, 232, 155, 20);
		contentPane.add(lblNewLabel);
			
		downBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				int[] selectedList = userPCTable.getSelectedRows();
				Integer[] wrapperList = new Integer[userPCTable.getSelectedRows().length];
				int j = 0;
				
				if(1 > selectedList.length) return;
				
				for(int selectedIdx : selectedList) {
					
					Object[] row = new Object[userPCTable.getColumnCount()];
					
					for(int i = 0; i < userPCTable.getColumnCount(); i++) {
						row[i] = userPCTableModel.getValueAt(selectedIdx, i);
					}
					
					pcEQTableModel.addRow(row);
					wrapperList[j] = Integer.valueOf(selectedIdx);					
					j++;
				}
				
				Arrays.sort(wrapperList, Comparator.reverseOrder());
				
				for(int selectedIdx : wrapperList) {
					userPCTableModel.removeRow(selectedIdx);
				}
			}
		});
	}
}