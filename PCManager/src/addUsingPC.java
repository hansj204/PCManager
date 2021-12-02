import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import element.RoundedButton;

public class addUsingPC extends JFrame {

	private JPanel contentPane;
	Item[] pcTypeList = new Item[4];
	Object info[];
	
	public addUsingPC(manageUsingPC parent, String userId, Connection connect) throws SQLException {
		
		Statement state= connect.createStatement();
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(1150, 440, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		
		pcTypeList[0] = new Item("", "전체");
		pcTypeList[1] = new Item("m", "모니터");
		pcTypeList[2] = new Item("p", "PC");
		pcTypeList[3] = new Item("n", "노트북");

		DefaultTableModel pcEQTableModel = new DefaultTableModel(new String[]{"시리얼 넘버", "PC 종류", "PC 이름"}, 0){
			
			@Override
			public boolean isCellEditable(int row, int column) {
				if(row == getRowCount() - 1) return true;
				return false;
			}
			
			@Override
			public int getRowCount() {
				return super.getRowCount();
			}
		};
		
		JTable pcEQTable = new JTable(pcEQTableModel);
		pcEQTable.setBackground(Color.WHITE);
		pcEQTable.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		pcEQTable.setEnabled(false);
		pcEQTable.setRowHeight(20);
		pcEQTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = pcEQTable.rowAtPoint(e.getPoint());
				ArrayList<String> data = new ArrayList<>();
				
				data.add((String) pcEQTable.getModel().getValueAt(row, 0));
				
				parent.addPropery(data);
				
				dispose();
			}
		});
		contentPane.setLayout(null);
		
		JScrollPane pcScrollPane = new JScrollPane(pcEQTable);
		pcScrollPane.setBounds(12, 10, 410, 241);
		pcScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pcScrollPane.setBackground(Color.WHITE);
		contentPane.add(pcScrollPane);
		
		try(ResultSet rs =state.executeQuery("SELECT * FROM PC_EQUIPMENT WHERE SERIALNUM NOT IN (SELECT SERIALNUM FROM PC_USERS);")) {
    	
			pcEQTableModel.setRowCount(0);
			
			while(rs.next()) {
				String pcType = "";
				
				for(Item type : pcTypeList) {
					if(type.getId().equals(rs.getString("PCTYPE"))) pcType = type.getName();
				}
				
				pcEQTableModel.addRow(new Object[]{ rs.getString("SERIALNUM"), rs.getString("PCTYPE"), rs.getString("PCNAME") });
			}
			
			
				
		} catch (Exception e1) {
			System.out.println(e1);
		}
	}
}
