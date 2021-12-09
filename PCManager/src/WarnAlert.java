
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
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class WarnAlert extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public WarnAlert(String message) {		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setBounds((int)screenSize.width / 2 - 140, (int)screenSize.getHeight() / 2- 90, 280, 180);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea textArea = new JTextArea(message);
		textArea.setEditable(false);
		textArea.setFont(new Font("나눔스퀘어", Font.PLAIN, 13));
		textArea.setLineWrap(true);
		textArea.setBounds(20, 85, 242, 40);
		
		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBounds(20, 85, 242, 40);
		contentPane.add(scrollPane);
		
		RoundedButton okBtn = new RoundedButton("확인");
		okBtn.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		okBtn.setBounds(199, 141, 72, 24);
		contentPane.add(okBtn);
		okBtn.addActionListener(new ActionListener() {
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
