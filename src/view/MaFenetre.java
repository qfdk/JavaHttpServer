package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import app.Serveur;
import control.Observer;

/**
 * @author qfdk MaFenetre.java 2015年10月9日
 */
public class MaFenetre extends JFrame implements ActionListener, Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Serveur serveur = null;
	private JButton bp_start;
	private JButton bp_stop;
	private JTextArea jta;
	private JPanel jp_top;
	private JPanel jp_bottom;
	private JScrollPane jsb;

	/**
	 * Le constucteur de classe MaFenetre.java
	 * 
	 * @param titre
	 */
	public MaFenetre(String titre) {
		super(titre);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(new Dimension(500, 300));
//		this.setResizable(false);
		init();
		placement();
	}

	private void placement() {
		jp_top.add(bp_start);
		jp_top.add(bp_stop);
		jp_bottom.add(jsb);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(jp_top, BorderLayout.NORTH);
		this.getContentPane().add(jp_bottom, BorderLayout.CENTER);
	}

	private void init() {
		bp_start = new JButton("Start");
		bp_start.addActionListener(this);
		bp_stop = new JButton("Stop");
		bp_stop.addActionListener(this);
		bp_stop.setEnabled(false);
		jta = new JTextArea();
		jp_top = new JPanel(new GridLayout(1, 2));
		jp_bottom = new JPanel(new BorderLayout());
		jsb = new JScrollPane(jta);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(bp_start)) {
			serveur = new Serveur(8080);
			serveur.register(this);
			serveur.start();
			jta.setText("[info] Server started :)\n" + serveur.getInfo() + "\n");
			// magic
			DefaultCaret caret = (DefaultCaret)jta.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			
			bp_start.setEnabled(false);
			bp_stop.setEnabled(true);
		}
		if (e.getSource().equals(bp_stop)) {
			bp_start.setEnabled(true);
			bp_stop.setEnabled(false);
			String tmp = jta.getText();
			try {
				serveur.arret();
			} catch (IOException | InterruptedException e1) {
				serveur.stop();
			}
			jta.setText(tmp + "[info] Server stopped");
//			jta.setSelectionStart(jta.getText().length());
		}
	}

	@Override
	public void update(String msg) {
		String tmp = jta.getText();
		jta.setText(tmp + msg);
	}

}
