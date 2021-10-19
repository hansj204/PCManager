import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTree;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JInternalFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.Color;

class Item {

  private String id;
  private String name;

  public Item(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
  
  @Override
  public String toString() {
    return name;
  }
}

public class PCEquipment extends JFrame {
	
	private JPanel searchArea;
	private JPanel buttonArea;
	private JScrollPane scrollPane;

	public PCEquipment(Connection connect) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 881, 552);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		String header[] = {"번호", "PC 종류", "이름", "사용자", "반납여부"};
		String contents[][] = {
				{"11","모니터", "모니터1", "a", "Y"},
				{"22","데스크탑", "데스크탑1", "a", "Y"},
		};
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane);
		
		JToolBar toolBar_1 = new JToolBar();
		toolBar_1.setOrientation(SwingConstants.VERTICAL);
		tabbedPane.addTab("PC 장비 목록", null, toolBar_1, null);
		
		searchArea = new JPanel();
		toolBar_1.add(searchArea);
		searchArea.setBackground(Color.WHITE);
		
		JLabel serialNumLabel = new JLabel("시리얼 번호");
		serialNumLabel.setForeground(Color.BLACK);
		serialNumLabel.setEnabled(false);
		searchArea.add(serialNumLabel);
		
		JTextField serialNum = new JTextField();
		searchArea.add(serialNum);
		serialNum.setColumns(10);
		
		JLabel pcTypeLabel = new JLabel("PC 종류");
		searchArea.add(pcTypeLabel);
		
		JComboBox<Item> pcType = new JComboBox<Item>();
		pcType.addItem(new Item("", "전체"));
		pcType.addItem(new Item("m", "모니터"));
		pcType.addItem(new Item("p", "PC"));
		pcType.addItem(new Item("n", "노트북"));
		searchArea.add(pcType);
		
		JLabel serialNumLabel_1 = new JLabel("이름");
		searchArea.add(serialNumLabel_1);
		
		JTextField pcName = new JTextField();
		pcName.setColumns(10);
		searchArea.add(pcName);
		
		JButton searchBtn = new JButton("검색");
		searchArea.add(searchBtn);
		
		buttonArea = new JPanel();
		toolBar_1.add(buttonArea);
		
		JButton addBtn = new JButton("추가");
		buttonArea.add(addBtn);
		
		JTable table = new JTable(contents, header) ;
		table.setEnabled(false);
		scrollPane = new JScrollPane(table);
		toolBar_1.add(scrollPane);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		tabbedPane.addTab("PC 사용자 목록", null, scrollPane_1, null);
		

		ActionListener eventHandler = new ActionListener() {
                    
            @Override
            public void actionPerformed(ActionEvent e) {                
                if(e.getSource() == addBtn){
                    new AddPC(connect).setVisible(true);
                } else if(e.getSource() == searchBtn){
                    Item item = (Item)pcType.getSelectedItem();
                    
        			try(Statement state= connect.createStatement()) {
        				ResultSet rs =state.executeQuery("SELECT * FROM PC_EQUIPMENT "
        						+ "WHERE SERIALNUM = '%"+serialNum.getText()+"%' "
        						+ (("" == item.getId())? "": "AND PCTYPE = '"+item.getId()+"' ")
        						+ "AND NAME like '%"+pcName.getText()+"%' ;");	
        				
        				while(rs.next()) {
        					String SERIALNUM = rs.getString("SERIALNUM"); 
        					String PCTYPE = rs.getString("PCTYPE"); 
        					String NAME = rs.getString("NAME");
        					
        					System.out.println(SERIALNUM + PCTYPE + NAME);
        				}
        				
        				rs.close();
        			} catch (Exception e1) {
        				System.out.println("검색 실패");
        			}
                }                
            }
        };
        
		addBtn.addActionListener(eventHandler);		
		searchBtn.addActionListener(eventHandler);
	}
}
