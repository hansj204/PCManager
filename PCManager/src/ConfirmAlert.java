
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ConfirmAlert extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public ConfirmAlert(managePC parent, String message) {		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setBounds((int)screenSize.width / 2 - 140, (int)screenSize.getHeight() / 2- 90, 280, 180);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel(message);
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("나눔스퀘어", Font.PLAIN, 13));
		label.setBounds(20, 85, 242, 40);
		
		JScrollPane scrollPane = new JScrollPane(label, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBounds(20, 85, 242, 40);
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.add(scrollPane);
		
		RoundedButton okBtn = new RoundedButton("확인");
		okBtn.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		okBtn.setBounds(199, 141, 72, 24);
		contentPane.add(okBtn);
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.deleteData();
				dispose();
			}
		});
		
		RoundedButton cancelBtn = new RoundedButton("취소");
		cancelBtn.setForeground(Color.WHITE);
		cancelBtn.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		cancelBtn.setBackground(Color.WHITE);
		cancelBtn.setBounds(116, 141, 72, 24);
		contentPane.add(cancelBtn);
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		Image img = new ImageIcon("img/attention.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH ); 
		JLabel warning = new JLabel(new ImageIcon(img));
		warning.setBounds(105, 17, 70, 70);
		contentPane.add(warning);		
	}

}
