import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddPC extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JLabel lblNewLabel_1;
	private JTextField textField_1;
	
	public AddPC(Connection connect) {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel serialNumLabel = new JLabel("시리얼 넘버");
		contentPane.add(serialNumLabel);
		
		JTextField serialNum = new JTextField();
		serialNum.setColumns(10);
		contentPane.add(serialNum);
		
		JLabel pcTypeLabel = new JLabel("PC 종류");
		contentPane.add(pcTypeLabel);
		
		JComboBox<Item> pcType = new JComboBox<Item>();		
		pcType.addItem(new Item("m", "모니터"));
		pcType.addItem(new Item("p", "PC"));
		pcType.addItem(new Item("n", "노트북"));
		contentPane.add(pcType);
		
		JLabel pcNameLabel = new JLabel("이름");
		contentPane.add(pcNameLabel);
		
		JTextField pcName = new JTextField();
		pcName.setColumns(10);
		contentPane.add(pcName);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JButton insertBtn = new JButton("저장");
		insertBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == insertBtn){
					try(PreparedStatement ps = connect.prepareStatement("INSERT INTO PC_EQUIPMENT VALUES (?,?,?,?)")){
						
	                    Item item = (Item)pcType.getSelectedItem();
						
						ps.setString(1, serialNum.getText());
						ps.setString(2, item.getId());
						ps.setString(3, pcName.getText());
						ps.setString(4, "2021-10-19");
						int res = ps.executeUpdate();
						
						if (res == 1) {
							System.out.println("============저장 성공============");
							dispose();
						}
						
					} catch (Exception e1) {
						System.out.println(e1.getMessage());
					}
				}
			}
		});
		panel.add(insertBtn);		
	}
}
