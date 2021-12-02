import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import element.RoundedButton;
import javax.swing.border.LineBorder;

public class managePC extends JFrame {

	private JPanel contentPane;
	JTextField serialNum;
	JComboBox<Item> pcType;
	JTextField pcName;
	private JTextField purhaseDate;
	JTextField manufactureDate;
	HashMap<String, String> pcTypeList = new HashMap<>();
	
	private RoundedButton searchBtn;
	RoundedButton saveBtn;
	private RoundedButton deleteBtn;
	
	private Connection connect;
	Font font = new Font("나눔스퀘어", Font.PLAIN, 12);
	ArrayList<String> info = new ArrayList<>();
	
	/**
	 * @wbp.parser.constructor
	 */
	public managePC(Connection connect, RoundedButton searchBtn) {
		this.connect= connect;
		this.searchBtn = searchBtn;
		
		setAddPC();		
		setComponent(true);
		insert();
	}
	
	public managePC(Connection connect, RoundedButton searchBtn, String pcSerialNum) {
		
		this.connect= connect;
		this.searchBtn = searchBtn;
		
		setAddPC();
		setComponent(false);
		update();
		delete();
		
		try(Statement state= connect.createStatement(); 
				ResultSet rs =state.executeQuery("SELECT * FROM PC_EQUIPMENT WHERE SERIALNUM LIKE '%"+pcSerialNum+"%';")) {
			
				while(rs.next()) {
					info.add(rs.getString("SERIALNUM"));
					
					info.add(rs.getString("PCTYPE"));
					info.add(rs.getString("PCNAME"));
					info.add(rs.getString("MANUFACTUREDATE"));
					info.add(rs.getString("PURCHASEDATE"));
				}

				setData();
				
			} catch (Exception e1) {
				System.out.println(e1.getCause());
			}
	}
	
	public void setAddPC() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(750, 440, 414, 332);
		setTitle("상세");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.white);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(new Color(255, 0, 0, 0));
		panel.setBounds(12, 10, 374, 231);
		contentPane.add(panel);

		
		JLabel serialNumLabel = new JLabel("시리얼 넘버");
		serialNumLabel.setBounds(35, 33, 212, 35);
		serialNumLabel.setFont(font);
		contentPane.add(serialNumLabel);
		
		serialNum = new JTextField();
		serialNum.setBounds(129, 38, 212, 25);
		serialNum.setColumns(10);
		serialNum.setFont(font);
		contentPane.add(serialNum);
		
		JLabel pcTypeLabel = new JLabel("PC 종류");
		pcTypeLabel.setBounds(35, 73, 212, 35);
		pcTypeLabel.setFont(font);
		contentPane.add(pcTypeLabel);
		
		pcTypeList.put("m", "모니터");
		pcTypeList.put("p", "PC");
		pcTypeList.put("n", "노트북");

		pcType = new JComboBox<Item>();
		pcType.setEnabled(false);
		pcType.setBounds(129, 78, 212, 25);
		pcType.setBackground(Color.WHITE);
		pcType.setFont(font);

		for(String key : pcTypeList.keySet()) {
			pcType.addItem(new Item(key, pcTypeList.get(key)));
		}

		contentPane.add(pcType);
		
		JLabel pcNameLabel = new JLabel("이름");
		pcNameLabel.setBounds(35, 108, 212, 35);
		pcNameLabel.setFont(font);
		contentPane.add(pcNameLabel);
		
		pcName = new JTextField();
		pcName.setBounds(129, 113, 212, 25);
		pcName.setFont(font);
		pcName.setColumns(10);
		contentPane.add(pcName);

		JLabel manufactureDateLabel = new JLabel("제조연월");
		manufactureDateLabel.setBounds(35, 143, 212, 35);
		manufactureDateLabel.setFont(font);
		contentPane.add(manufactureDateLabel);
		
		manufactureDate = new JTextField();
		manufactureDate.setBounds(129, 148, 212, 25);
		manufactureDate.setColumns(10);
		manufactureDate.setFont(font);
		contentPane.add(manufactureDate);
		
		JLabel purhaseDateLabel = new JLabel("구매일");
		purhaseDateLabel.setBounds(35, 178, 212, 35);
		purhaseDateLabel.setFont(font);		
		contentPane.add(purhaseDateLabel);
		
		purhaseDate = new JTextField();
		purhaseDate.setEnabled(false);
		purhaseDate.setBounds(129, 188, 212, 25);
		purhaseDate.setColumns(10);
		purhaseDate.setFont(font);
		contentPane.add(purhaseDate);
	}
	
	private void setComponent(Boolean isReadonly) {		
		
		serialNum.setBackground(Color.WHITE);
		pcType.setBackground(Color.WHITE);
		pcName.setBackground(Color.WHITE);
		manufactureDate.setBackground(Color.WHITE);
		purhaseDate.setBackground(Color.WHITE);
		
		serialNum.setEditable(isReadonly);
		pcType.setEnabled(isReadonly);
		pcName.setEditable(isReadonly);
		manufactureDate.setEditable(isReadonly);
		purhaseDate.setEditable(isReadonly);
	}
	
	private void setData() {
		serialNum.setText(info.get(0));
		
		int idx = 0;

		for(String key : pcTypeList.keySet()) {
			if(key.equals(info.get(1))) pcType.setSelectedIndex(idx);
			idx++;
		}

		pcName.setText(info.get(2));
		manufactureDate.setText(info.get(3));		
		purhaseDate.setText(info.get(4));
		
	}
	
	private void insert() {		
		RoundedButton insertBtn = new RoundedButton("저장");
		insertBtn.setBounds(304, 255, 82, 25);
		insertBtn.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		contentPane.add(insertBtn);
		
		insertBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == insertBtn){
					try(PreparedStatement ps = connect.prepareStatement("INSERT INTO PC_EQUIPMENT VALUES (?,?,?,?,?)")){
						
	                    Item item = (Item)pcType.getSelectedItem();
						
						ps.setString(1, serialNum.getText());
						
						System.out.println(1);
						ps.setString(2, item.getId());
						ps.setString(3, pcName.getText());
						ps.setString(4, "2021-10-19");
						ps.setString(5, "2021-10-19");
						int res = ps.executeUpdate();
						
						if (res == 1) {
							System.out.println("============저장 성공============");
							searchBtn.doClick();
							dispose();
						}
						
					} catch (Exception e1) {
						System.out.println(e1.getMessage());
					}
				}
			}
		});
	}
	
	private void modify() {
		saveBtn = new RoundedButton("저장");
		saveBtn.setFont(font);
		saveBtn.setBounds(304, 255, 82, 25);
		contentPane.add(saveBtn);
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				Item item = (Item)pcType.getSelectedItem();
				String updateSQL = "UPDATE PC_EQUIPMENT SET PCTYPE = ?, PCNAME = ?, PURCHASEDATE = ? WHERE SERIALNUM=?;";
				
				try(PreparedStatement ps = connect.prepareStatement(updateSQL)){
					
					ps.setString(1, item.getId());
					ps.setString(2, pcName.getText());
					ps.setString(3, purhaseDate.getText());
					ps.setString(4, serialNum.getText());
					int res = ps.executeUpdate();
					
					if (res == 1) {
						System.out.println("============수정 성공============");
						searchBtn.doClick();
						dispose();
					}
					
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
			}
		});
	}
	
	private void update() {
		RoundedButton updateBtn = new RoundedButton("수정");
		updateBtn.setFont(font);
		updateBtn.setBounds(304, 255, 82, 25);
		contentPane.add(updateBtn);		
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setComponent(true);
				updateBtn.setVisible(false);
				deleteBtn.setVisible(false);
				modify();
				cancel();
			}
		});
	}
	
	private void cancel() {
		RoundedButton cancelBtn = new RoundedButton("취소");
		cancelBtn.setFont(font);
		cancelBtn.setBounds(214, 255, 82, 25);
		contentPane.add(cancelBtn);
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setComponent(false);
				setData();
				cancelBtn.setVisible(false);
				saveBtn.setVisible(false);
				update();
				delete();
			}
		});
		
	}
	
	private void delete() {
		deleteBtn = new RoundedButton("삭제");
		deleteBtn.setFont(font);
		deleteBtn.setBounds(214, 255, 82, 25);
		contentPane.add(deleteBtn);
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Item item = (Item)pcType.getSelectedItem();
				String updateSQL = "DELETE FROM PC_EQUIPMENT WHERE SERIALNUM= ?;";
				
				try(PreparedStatement ps = connect.prepareStatement(updateSQL)){
					
					ps.setString(1, serialNum.getText());
					int res = ps.executeUpdate();
					
					System.out.println("============삭제 성공============");
					searchBtn.doClick();
					dispose();
					
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
			}
		});
	}
}
