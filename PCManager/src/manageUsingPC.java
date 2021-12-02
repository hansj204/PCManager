import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
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
import javax.swing.table.TableCellRenderer;

import element.RoundedButton;

public class manageUsingPC extends JFrame {

	private JPanel contentPane;
	HashMap<String, String> pcTypeList = new HashMap<>();
	
	private String userId;
	private Connection connect;
	private ArrayList<String> propertyList = new ArrayList<String>();
	Font font;
	JTable userPCTable;
	DefaultTableModel userPCTableModel;
	
	/**
	 * @wbp.parser.constructor
	 */
	public manageUsingPC(Connection connect, String userId) {
		font = new Font("나눔스퀘어", Font.PLAIN, 12);
		this.connect= connect;
		this.userId = userId;
		
		setUsingPC();
		insert();
		delete();
	}
	
	private void setUsingPC() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(750, 440, 414, 332);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		//사용자 - 이용 컴퓨터 목록				
		userPCTableModel = new DefaultTableModel(new String[]{"시리얼 넘버", "PC 종류", "PC 이름"}, 0){
			
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
		
		userPCTable = new JTable(userPCTableModel) {
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component comp=super.prepareRenderer(renderer,row, column);
				int modelRow=convertRowIndexToModel(row);
		        
				if(!isRowSelected(modelRow)) comp.setBackground(Color.WHITE);
		        else comp.setBackground(Color.LIGHT_GRAY);
		        
				return comp;
			};
		};
		
		userPCTable.setBackground(Color.WHITE);
		userPCTable.setFont(font);
		userPCTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		userPCTable.setRowSelectionAllowed(true);
		userPCTable.setEnabled(false);
		userPCTable.setRowHeight(20);
		userPCTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		
		JScrollPane pcScrollPane = new JScrollPane(userPCTable);
		pcScrollPane.setBounds(12, 50, 374, 201);
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
			System.out.println(e1);
		}
    	
    	RoundedButton okBtn = new RoundedButton("확인");
    	okBtn.setFont(font);
		okBtn.setBounds(304, 260, 82, 25);
		contentPane.add(okBtn);
		okBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				for(String property : propertyList) {
					try(PreparedStatement ps = connect.prepareStatement("INSERT INTO PC_USERS VALUES (?,?)")) {				
						ps.setString(1, userId);
						ps.setString(2, (String) property);
						int res = ps.executeUpdate();
						
						if (res == 1) {
							System.out.println("============저장 성공============");							
							dispose();
						}
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
			}
		});
	}
	
	protected void addPropery(ArrayList<String> data) {
		for(String propery : data) {
			
			try(Statement state= connect.createStatement(); ResultSet rs =state.executeQuery("SELECT * FROM PC_EQUIPMENT WHERE SERIALNUM LIKE '%"+propery+"%';")) {
				
				while(rs.next()) {

					String pcType = "";
					
					for(String key : pcTypeList.keySet()) {
						if(key.equals(rs.getString("PCTYPE"))) pcType = pcTypeList.get(key);
					}
					
					userPCTableModel.addRow(new Object[]{ rs.getString("SERIALNUM"), pcType, rs.getString("PCNAME") });
					propertyList.add(propery);
				}
			} catch (Exception e1) {
				System.out.println(e1.getCause());
			}
		}
	}
	
	private void insert() {
		
		manageUsingPC frame = this;
		
		Image img = new ImageIcon("img/plus.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH ); 
		JLabel plusBtn = new JLabel(new ImageIcon(img));
		plusBtn.setBounds(20, 15, 25, 25);
		contentPane.add(plusBtn);		
		plusBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					new addUsingPC(frame, userId, connect).setVisible(true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	private void delete() {
		Image img = new ImageIcon("img/minus.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH ); 
		JLabel deleteBtn = new JLabel(new ImageIcon(img));
		deleteBtn.setBounds(57, 15, 25, 25);
		contentPane.add(deleteBtn);
		deleteBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String updateSQL = "DELETE FROM PC_EQUIPMENT WHERE SERIALNUM= ?;";
				
				try(PreparedStatement ps = connect.prepareStatement(updateSQL)){
					
					for(int idx : userPCTable.getSelectedRows()) {
						ps.setString(1, (String) userPCTable.getModel().getValueAt(idx, 0));
						ps.executeUpdate();
					}
					
					System.out.println("============삭제 성공============");
					dispose();
					
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
			}
		});
	}
}
