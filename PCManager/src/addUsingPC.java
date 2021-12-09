import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class addUsingPC extends JFrame {

	private JPanel contentPane;
	HashMap<String, String> pcTypeList = new HashMap<>();
	Object info[];
	ArrayList<String> data = new ArrayList<>();
	
	public addUsingPC(manageUsingPC parent, String userId, Connection connect) throws SQLException {
		
		Statement state= connect.createStatement();
		
		ImageIcon icon = new ImageIcon("img/setting.png");
		setIconImage(icon.getImage());
		setTitle("PC 장비 목록");
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(1150, 440, 450, 308);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		
		pcTypeList.put("m", "모니터");
		pcTypeList.put("p", "PC");
		pcTypeList.put("n", "노트북");

		DefaultTableModel pcEQTableModel = new DefaultTableModel(new String[]{"시리얼 넘버", "PC 종류", "PC 이름"}, 0);
		
		JTable pcEQTable = new JTable(pcEQTableModel);
		pcEQTable.setBackground(Color.WHITE);
		pcEQTable.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		pcEQTable.setRowHeight(20);
		pcEQTable.setRowSelectionAllowed(true);
		pcEQTable.setSelectionBackground(Color.LIGHT_GRAY);
		pcEQTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				parent.addProperty((String) pcEQTable.getValueAt(pcEQTable.getSelectedRow(), 0));
				dispose();
			}
		});
		contentPane.setLayout(null);
		
		JScrollPane pcScrollPane = new JScrollPane(pcEQTable);
		pcScrollPane.setBounds(15, 14, 410, 255);
		pcScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pcScrollPane.getViewport().setBackground(Color.WHITE);
		contentPane.add(pcScrollPane);
		
		try(ResultSet rs =state.executeQuery("SELECT * FROM PC_EQUIPMENT WHERE SERIALNUM NOT IN (SELECT SERIALNUM FROM PC_USERS);")) {
    	
			pcEQTableModel.setRowCount(0);
			
			while(rs.next()) {
				Boolean overlap = false;
				String pcType = "";
				
				for(String key : pcTypeList.keySet()) {
					if(key.equals(rs.getString("PCTYPE"))) pcType = pcTypeList.get(key);
				}
				
				for(int idx = 0; idx < parent.userPCTable.getRowCount(); idx++) {
					if(parent.userPCTableModel.getValueAt(idx, 0).equals(rs.getString("SERIALNUM"))) overlap = true;					
				}
				
				if(overlap) continue;
				
				pcEQTableModel.addRow(new Object[]{ rs.getString("SERIALNUM"), pcType, rs.getString("PCNAME") });
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
