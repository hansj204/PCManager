import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

class Item {

  private String key;
  private String value;
  
  public Item() {}

  public Item(String id, String name) {
    this.key = id;
    this.value = name;
  }

  public String getId() {
    return key;
  }

  public String getName() {
    return value;
  }
  
  @Override
	public String toString() {
		return value;
	}
}

@SuppressWarnings("serial")
public class MainPage extends JFrame {
	
	private JPanel searchArea;
	private JPanel buttonArea;
	private JScrollPane scrollPane;
	private JScrollPane userScrollPane;
	Font font = new Font("나눔스퀘어", Font.BOLD, 14);
	private JTextField pcName;
	private JTextField userId;
	manageUsingPC manageUsingPC = null;
	
	public final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public MainPage(Connection connect) {
		ImageIcon icon = new ImageIcon("img/setting.png");
		setIconImage(icon.getImage());
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((int)screenSize.width / 2 - 419, (int)screenSize.getHeight() / 2- 301, 938, 602);
		setResizable(false);
		setTitle("PCManager");
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new MatteBorder(1, 1, 2, 1, (Color) new Color(0, 0, 0)));
		tabbedPane.setFont(font);
		tabbedPane.setBackground(Color.WHITE);
		panel.add(tabbedPane);
		
		//PC 장비 목록 
		JToolBar pcEQListArea = new JToolBar();
		pcEQListArea.setFloatable(false);
		pcEQListArea.setBackground(Color.WHITE);
		pcEQListArea.setOrientation(SwingConstants.VERTICAL);
		tabbedPane.addTab("PC 장비 목록", null, pcEQListArea, null);
		
		searchArea = new JPanel();
		pcEQListArea.add(searchArea);
		searchArea.setBackground(Color.WHITE);
		searchArea.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		searchArea.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.LIGHT_GRAY));
		
		JPanel serialNumArea = new JPanel();
		serialNumArea.setBackground(Color.WHITE);
		serialNumArea.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 20));
		serialNumArea.setLayout(new GridLayout(0, 1, 0, 0));
		searchArea.add(serialNumArea);
		
		JLabel serialNumLabel = new JLabel("시리얼 번호");
		serialNumArea.add(serialNumLabel);
		serialNumLabel.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		
		JTextField serialNum = new JTextField();
		serialNumArea.add(serialNum);
		serialNum.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		serialNum.setColumns(15);
		serialNum.setPreferredSize(new Dimension(200, 25));
		
		JPanel pcTypeArea = new JPanel();
		pcTypeArea.setBackground(Color.WHITE);
		pcTypeArea.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 20));
		searchArea.add(pcTypeArea);
		pcTypeArea.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel pcTypeLabel = new JLabel("PC 종류");
		pcTypeArea.add(pcTypeLabel);
		pcTypeLabel.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		
		Item[] pcTypeList = new Item[4];
		pcTypeList[0] = new Item("", "전체");
		pcTypeList[1] = new Item("m", "모니터");
		pcTypeList[2] = new Item("p", "PC");
		pcTypeList[3] = new Item("n", "노트북");
		
		JComboBox<Item> pcType = new JComboBox<Item>();
		pcType.setBackground(Color.WHITE);
		pcType.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		
		for(Item item : pcTypeList) {
			pcType.addItem(item);
		}
		
		pcType.setPreferredSize(new Dimension(180, 25));
		pcTypeArea.add(pcType);
		
		JPanel pcNameArea = new JPanel();
		pcNameArea.setBackground(Color.WHITE);
		pcNameArea.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 20));
		searchArea.add(pcNameArea);
		pcNameArea.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel pcNameLabel = new JLabel("이름");
		pcNameArea.add(pcNameLabel);
		pcNameLabel.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		
		pcName = new JTextField();
		pcName.setPreferredSize(new Dimension(200, 25));
		pcName.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		pcName.setColumns(15);
		pcNameArea.add(pcName);
		
		JPanel useYNArea = new JPanel();
		useYNArea.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 20));
		useYNArea.setBackground(Color.WHITE);
		searchArea.add(useYNArea);
		useYNArea.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel useYnLabel = new JLabel("사용여부");
		useYnLabel.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		useYNArea.add(useYnLabel);
		
		JComboBox<Item> useYNList = new JComboBox<Item>();
		useYNList.setPreferredSize(new Dimension(180, 25));
		useYNList.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		useYNList.addItem(new Item("", "전체"));
		useYNList.addItem(new Item("Y", "Yes"));
		useYNList.addItem(new Item("N", "No"));
		useYNList.setBackground(Color.WHITE);
		useYNArea.add(useYNList);
		
		JPanel searchBtnArea = new JPanel();
		searchBtnArea.setBackground(Color.WHITE);
		searchArea.add(searchBtnArea);
		searchBtnArea.setBorder(BorderFactory.createEmptyBorder(6 , 0 , 0 , 0));
		searchBtnArea.setPreferredSize(new Dimension(60, 50));
		searchBtnArea.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		RoundedButton searchPCBtn = new RoundedButton("검색");
		searchBtnArea.add(searchPCBtn);
		searchPCBtn.setVerticalAlignment(SwingConstants.BOTTOM);
		searchPCBtn.setFont(font);		
		
		searchPCBtn.doClick();
		
		buttonArea = new JPanel();
		buttonArea.setBackground(Color.WHITE);
		pcEQListArea.add(buttonArea);
		buttonArea.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		buttonArea.setBorder(BorderFactory.createEmptyBorder(20 , 0 , 0 , 0));
		
		RoundedButton addBtn = new RoundedButton("추가");
		addBtn.setFont(font);
		buttonArea.add(addBtn);
		
		DefaultTableModel pcTableModel = new DefaultTableModel(new String[]{"시리얼 번호", "PC 종류", "이름", "사용중"}, 0) ;
		
		JTable pcEQTable = new JTable(pcTableModel) ;
		pcEQTable.setBackground(Color.WHITE);
		pcEQTable.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		pcEQTable.setRowHeight(20);
		pcEQTable.setSelectionBackground(Color.LIGHT_GRAY);
		
		pcEQTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				int row = pcEQTable.rowAtPoint(evt.getPoint());
				
				new managePC(connect, searchPCBtn, (String)pcEQTable.getModel().getValueAt(row, 0)).setVisible(true);
			}
		});
		
		scrollPane = new JScrollPane(pcEQTable);
		scrollPane.getViewport().setBackground(Color.WHITE);
		pcEQListArea.add(scrollPane);
						
		//사용자 목록
		JToolBar pcUserListArea = new JToolBar();
		pcUserListArea.setFloatable(false);
		pcUserListArea.setBackground(Color.WHITE);
		pcUserListArea.setOrientation(SwingConstants.VERTICAL);
		tabbedPane.addTab("PC 사용자 목록", null, pcUserListArea, null);
		
		JPanel userSearchArea = new JPanel();
		userSearchArea.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.LIGHT_GRAY));
		userSearchArea.setBackground(Color.WHITE);
		pcUserListArea.add(userSearchArea);
		userSearchArea.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel userIdArea = new JPanel();
		userIdArea.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 20));
		userIdArea.setBackground(Color.WHITE);
		userSearchArea.add(userIdArea);
		userIdArea.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel userIdLabel = new JLabel("사번");
		userIdLabel.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		userIdArea.add(userIdLabel);
		
		userId = new JTextField();
		userId.setPreferredSize(new Dimension(200, 25));
		userId.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		userId.setColumns(15);
		userIdArea.add(userId);
		
		JPanel departmentArea = new JPanel();
		departmentArea.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 20));
		departmentArea.setBackground(Color.WHITE);
		userSearchArea.add(departmentArea);
		departmentArea.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel pcTypeLabel_1 = new JLabel("부서");
		pcTypeLabel_1.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		departmentArea.add(pcTypeLabel_1);
		
		JComboBox<Item> department = new JComboBox<Item>();
		department.setPreferredSize(new Dimension(180, 25));
		department.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		department.setBackground(Color.WHITE);
		departmentArea.add(department);
		
		HashMap<String, String> departmentList = new HashMap<>();
		departmentList.put("", "전체");
		
		try(Statement state= connect.createStatement(); ResultSet rs =state.executeQuery("SELECT * from DEPARTMENT;")) {				
			while(rs.next()) {        					
				departmentList.put(rs.getString("DEPARTMENTCODE"), rs.getString("DEPARTMENTNAME"));
			}				
		} catch (Exception e1) {
			e1.getStackTrace();
		}
		
		for(String key : departmentList.keySet()) {
			Item item = new Item(key, departmentList.get(key));
			department.addItem(item);
		}
		
		JPanel userNameArea = new JPanel();
		userNameArea.setBorder(BorderFactory.createEmptyBorder(0 , 0 , 0 , 20));
		userNameArea.setBackground(Color.WHITE);
		userSearchArea.add(userNameArea);
		userNameArea.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel pcNameLabel_1 = new JLabel("이름");
		pcNameLabel_1.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		userNameArea.add(pcNameLabel_1);
		
		JTextField userName = new JTextField();
		userName.setPreferredSize(new Dimension(200, 25));
		userName.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		userName.setColumns(15);
		userNameArea.add(userName);
		
		JPanel searchBtnArea2 = new JPanel();
		searchBtnArea2.setPreferredSize(new Dimension(60, 50));
		searchBtnArea2.setBorder(BorderFactory.createEmptyBorder(6 , 0 , 0 , 0));
		searchBtnArea2.setBackground(Color.WHITE);
		userSearchArea.add(searchBtnArea2);
		searchBtnArea2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		RoundedButton searchUserBtn = new RoundedButton("검색");
		searchBtnArea2.add(searchUserBtn);
		searchUserBtn.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		searchUserBtn.doClick();
		
		String userColumn[] = {"사번", "부서", "이름"};		
		DefaultTableModel usertableModel = new DefaultTableModel(userColumn, 0);
		
		JTable userTable = new JTable(usertableModel);
		userTable.setRowHeight(20);
		userTable.setSelectionBackground(Color.LIGHT_GRAY);
		userTable.setFont(new Font("나눔스퀘어", Font.PLAIN, 14));
		
		userScrollPane = new JScrollPane(userTable);
		userScrollPane.getViewport().setBackground(Color.WHITE);
		
		JPanel buttonArea2 = new JPanel();
		buttonArea2.setBorder(BorderFactory.createEmptyBorder(20 , 0 , 0 , 0));
		buttonArea2.setBackground(Color.WHITE);
		pcUserListArea.add(buttonArea2);
		buttonArea2.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		pcUserListArea.add(userScrollPane);
		
		userTable.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent evt) {
				int row = userTable.rowAtPoint(evt.getPoint());
				if(null != manageUsingPC) manageUsingPC.dispose();
				manageUsingPC = new manageUsingPC(connect, (String)userTable.getModel().getValueAt(row, 0), searchPCBtn);
				manageUsingPC.setVisible(true);
			}
		});

		//이벤트
		ActionListener eventHandler = new ActionListener() {
                    
            @Override
            public void actionPerformed(ActionEvent e) {                
                if(e.getSource() == addBtn){
                    new managePC(connect, searchPCBtn).setVisible(true);                    
                } else if(e.getSource() == searchPCBtn){
                    Item typeItem = (Item)pcType.getSelectedItem();
                    Item useItem = (Item)useYNList.getSelectedItem();
                    
        			try(Statement state= connect.createStatement(); 
        				ResultSet rs =state.executeQuery("SELECT A.* FROM (SELECT *, CASE WHEN SERIALNUM IN (SELECT SERIALNUM FROM PC_USERS) THEN 'Y' ELSE 'N' END AS USE_YN FROM PC_EQUIPMENT WHERE SERIALNUM LIKE '%"
        						+serialNum.getText()+"%'" + (("" == typeItem.getId())? "": "AND PCTYPE = '"+typeItem.getId()+"' ") + "AND PCNAME LIKE '%"+pcName.getText()+"%') A"
        						+ (("" == useItem.getId())? "": " WHERE USE_YN = '"+useItem.getId()+"' "))) {

        				
        				pcTableModel.setRowCount(0);
        				        				
        				while(rs.next()) {      
        					
            				String pcType = "";
        					
        					for(Item item : pcTypeList) {
            					if(item.getId().equals(rs.getString("PCTYPE"))) pcType = item.getName();
            				}
        					
        					String useYn = rs.getString("USE_YN").equals("Y")? "Yes" : "No";
        					
        					pcTableModel.addRow(new Object[]{ rs.getString("SERIALNUM"), pcType, rs.getString("PCNAME"), useYn });        					
        				}
        				
        			} catch (Exception e1) {
        				System.out.println(e1);
        			}
                }  else if(e.getSource() == searchUserBtn){
                	
                	 Item departmentItem = (Item)department.getSelectedItem();
                	
        			try(Statement state= connect.createStatement(); 
        				ResultSet rs =state.executeQuery("SELECT A.USERID, B.DEPARTMENTNAME, A.USERNAME from EMPLOYEE A, DEPARTMENT B WHERE A.DEPARTMENTCODE = B.DEPARTMENTCODE"
        						+ " AND A.USERID LIKE '%"+userId.getText()+"%' AND A.USERNAME LIKE '%"+userName.getText() +"%' AND A.DEPARTMENTCODE LIKE '%"+departmentItem.getId() +"%'" )) {
        				
        				usertableModel.setRowCount(0);
        				
        				while(rs.next()) {        					
        					usertableModel.addRow(new Object[]{ rs.getString("USERID"), rs.getString("DEPARTMENTNAME"), rs.getString("USERNAME") });        					
        				}
        				
        			} catch (Exception e1) {
        				e1.printStackTrace();
        			}
                }
            }
        };
        
        searchPCBtn.addActionListener(eventHandler);	
		addBtn.addActionListener(eventHandler);		
		searchUserBtn.addActionListener(eventHandler);
		searchPCBtn.doClick();
		searchUserBtn.doClick();	
	}
}
