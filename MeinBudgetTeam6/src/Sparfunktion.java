

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;



import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



	public class Sparfunktion extends Menu{
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		private static final String sep = System.lineSeparator(); // /r/n

		private JFrame frame;
		private JFrame m1;
		
		private JPanel buttons = new JPanel();
		JPanel form = new JPanel();
		
		private JTable Spartabelle;
	
		Date Startdatum = new Date();
		
		JScrollPane scroll = new JScrollPane(form);
		
		public Sparfunktion() {
			init();
			setup();
		}
		
		public Sparfunktion(JFrame m1) {
			this.m1=m1;
			init();
			setup();
		}
		private void init(){
			frame = new JFrame();
			frame.setBounds(100, 100, 800, 600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new BorderLayout());
			frame.setVisible(true);
		}
		
		private void setup() {
			GridBagLayout gb = new GridBagLayout();
			form.setLayout(gb);
			
			JLabel hl = headline("Sparfunktion");
			frame.add(hl, BorderLayout.NORTH);

			//Datum
			JLabel lbl_Zdatum = new JLabel("Bitte das Datum eingeben (dd.MM.yyyy)");
			JTextField Zieldatum = new JTextField();
			Zieldatum.setColumns(10);
			addComp(form,  gb,			lbl_Zdatum,				0,			1,	1,	1);
			addComp(form,  gb,			Zieldatum,					1,			1,	2,	1);
				
			//Betrag
			JLabel lbl_tBetrag = new JLabel("Betrag");
			JTextField tBetrag= new JTextField();
			tBetrag.setColumns(7);
			addComp(form,  gb,			lbl_tBetrag,			0,			3,	1,	1);
			addComp(form,  gb,			tBetrag,				1,			3,	1,	1);
				
			//Speichern
			JButton btnBerechnen = new JButton("Berechnen");
			buttons.add(btnBerechnen);
			//addComp(form,  gb,			btnSpeichern,			0,			6,	2,	1);

			
			btnBerechnen.addActionListener(new ActionListener(){
				@SuppressWarnings("deprecation")
				public void actionPerformed(ActionEvent e){
			
					try {
						Date ZielDate = new Date();
						
						//String a = "01.";
						
						SimpleDateFormat formatter = new SimpleDateFormat ("dd.MM.yyyy");		
						//ZielDate = formatter.parse(a.concat(Zieldatum.getText()));
						ZielDate = formatter.parse(Zieldatum.getText());
						Calendar cal = Calendar.getInstance();
						cal.setTime(ZielDate);
						
						Calendar cal_cur = Calendar.getInstance();
						cal_cur.setTime(Startdatum);
						
						//if (isDate(tdate.getText()) == true){
						int aktuellerMonat = 0;
						int aktuellesJahr = 0;
						float MonatsBetrag = 0;
						
						//int StartTag = Startdatum.getDate();
						int StartTag = cal_cur.get(Calendar.DAY_OF_MONTH);
						
						String Betrag = "";

						//Berechnung des Start Monats und Jahres
						if (StartTag == 1){
							//aktuellerMonat = Startdatum.getMonth()+1;
							aktuellerMonat = cal_cur.get(Calendar.MONTH)+1;
						}
						else {
							//aktuellerMonat = Startdatum.getMonth()+2;
							aktuellerMonat = cal_cur.get(Calendar.MONTH)+2;
						}
						//aktuellesJahr = Startdatum.getYear()+1900;
						aktuellesJahr = cal_cur.get(Calendar.YEAR);
						
						Betrag = tBetrag.getText();
						
						int anzahlmonate = 0;
						/*
						if (ZielDate.getYear()-Startdatum.getYear()<=1){
							anzahlmonate= 12-(Startdatum.getMonth()+1) + ZielDate.getMonth()+1;
						}
						else {
							anzahlmonate= 12-(Startdatum.getMonth()+1) + ZielDate.getMonth()+1;
							anzahlmonate = anzahlmonate + (ZielDate.getYear()-Startdatum.getYear()-1)*12;
						}
						*/
						if(cal.get(Calendar.YEAR)-cal_cur.get(Calendar.YEAR)==0){ 
							anzahlmonate = 12-(cal_cur.get(Calendar.MONTH)+1); //wenn die Jahresdifferenz 0 ist, sollen nat�rlich die Monate des Zieldatums nicht dazuaddiert werden 
						}else{	
							if (cal.get(Calendar.YEAR)-cal_cur.get(Calendar.YEAR)<=1){
								anzahlmonate= (12-(cal_cur.get(Calendar.MONTH)+1)) + cal.get(Calendar.MONTH)+1;
							}else {
								anzahlmonate= 12-(cal_cur.get(Calendar.MONTH)+1) + cal.get(Calendar.MONTH)+1;
								anzahlmonate = anzahlmonate + (cal.get(Calendar.YEAR)-cal_cur.get(Calendar.YEAR)-1)*12;
							}
						}
						MonatsBetrag = Float.parseFloat(Betrag) / (anzahlmonate);
						
						JOptionPane.showMessageDialog(null, anzahlmonate);
						
						Object[][] data = new Object[anzahlmonate+1][4];

						data[0][0] = new String ("Monat");
						data[0][1] = new String ("Jahr");
						data[0][2] = new String ("Sparbetrag");
						data[0][3] = new String ("kum. Sparbetrag");
						
						float cumvalue=0;
						
						for (int i=1; i<=anzahlmonate;i++){
							data[i][0] = aktuellerMonat;
							data[i][1] = aktuellesJahr;
							data[i][2] = Math.round(MonatsBetrag);
							data[i][3] = Math.round(cumvalue+MonatsBetrag);
							if (aktuellerMonat ==12) {aktuellerMonat = 1;aktuellesJahr= aktuellesJahr+1;}
							else{aktuellerMonat = aktuellerMonat +1;}
							cumvalue= cumvalue+MonatsBetrag;
						}
				
						String[] Spartabelle_Spalten = {"Monat","Sparbetrag","kum. Sparbetrag","Test"};
						
						Spartabelle = new JTable (data,Spartabelle_Spalten);
						
						addComp(form,  gb,			Spartabelle,				1,			4,	1,	1);
						
						frame.revalidate();
						frame.repaint();					 
					} catch (ParseException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "falsches Datum");
					}
					}
			});
			
			//Schlie�en
			JButton btnAbbruch = new JButton("Abbrechen");
			buttons.add(btnAbbruch);
			
			btnAbbruch.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					backHM();
				}
			});
			
			
			frame.add(buttons, BorderLayout.SOUTH);
			frame.getContentPane().add(scroll);
			frame.validate();
		}

	private void backHM() {
		m1.setVisible(true);
		frame.dispose();
	}
	
}
	
