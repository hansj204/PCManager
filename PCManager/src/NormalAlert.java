
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
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class NormalAlert extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public NormalAlert(String message) {		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setBounds((int)screenSize.width / 2 - 100, (int)screenSize.getHeight() / 2- 90, 280, 180);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel(message);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(20, 97, 242, 24);
		label.setFont(new Font("나눔스퀘어", Font.PLAIN, 13));
		contentPane.add(label);
		
		RoundedButton okBtn = new RoundedButton("확인");
		okBtn.setFont(new Font("나눔스퀘어", Font.PLAIN, 12));
		okBtn.setBounds(199, 141, 72, 24);
		contentPane.add(okBtn);
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		Image img = new ImageIcon("img/success.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH ); 
		JLabel warning = new JLabel(new ImageIcon(img));
		warning.setBounds(105, 17, 70, 70);
		contentPane.add(warning);		
	}
}
