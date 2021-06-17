package gov.nara.nwts.ftapp.gui;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.Stats;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * User interface component that will be generated at the completion of each File Analyzer action
 * @author TBrady
 *
 */
class SummaryPanel extends MyBorderPanel {
	private static final long serialVersionUID = 1L;
	JPanel tp;
	//JPanel tp2;
	JPanel filterPanel;
	JPanel filterPanel1;
	JPanel testPanel;
	StatsTable st;
	StatsTable st1;
	JTextField note;
	JTextField fnote;
	DirectoryTable parent;
	JPanel np;
	JButton test = new JButton("Test");
	SummaryPanel(DirectoryTable dt) {
		parent = dt;
		tp = addBorderPanel("요약 총합");//Summary Counts
		JPanel p = addPanel("", BorderLayout.SOUTH);
		note = new JTextField();
		note.setEditable(false);
		note.setBorder(BorderFactory.createEmptyBorder());
		fnote = new JTextField(45);
		fnote.setEditable(false);
		fnote.setBorder(BorderFactory.createEmptyBorder());
		p.add(note);
		p.add(fnote);
		JButton save = new JButton("다른 이름으로 저장");//Export Table
		save.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new TableSaver(parent,st.tm,st.jt,"Stats",st.noExport);
				}
			}
		);
		p.add(save);
		np = addPanel("",BorderLayout.NORTH);
		np.setLayout(new BorderLayout());
		p = new JPanel();
		np.add(p, BorderLayout.NORTH);
		String note = parent.criteriaPanel.actions.getSelectedItem().toString() +": "+parent.criteriaPanel.rootLabel.getText();
		JTextField jtf = new JTextField(note,30);
		parent.detailsPanel.jtfRoot.setText(jtf.getText());
		jtf.setEditable(false);
		p.add(jtf);
		JButton b = new JButton("탭 삭제하기");//Remove Tab
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				parent.tabs.remove(SummaryPanel.this);
				parent.checkTabs();
			}});
		p.add(b);
		filterPanel = new JPanel();
		
		
		np.add(filterPanel, BorderLayout.SOUTH);
		
	}
	
	
	void showStats(Object[][] details,TreeMap<String,Stats> types, String name) {
		if(name.contains("모든기능수행")) {
			filterPanel1 = new JPanel();
			np.add(filterPanel1, BorderLayout.NORTH);
			
			st = new StatsTable(details,types, parent);
			st1 = new StatsTable(details,types, parent);

			tp.removeAll();
			tp.add(new JScrollPane(st1.jt), BorderLayout.CENTER);
			tp.add(new JScrollPane(st.jt), BorderLayout.NORTH);
			filterPanel.removeAll();
			
			for(Iterator<JComboBox>i=st1.filters.iterator();i.hasNext();) {
				JComboBox cb = i.next();
				if (cb!=null) filterPanel.add(cb);
			}		
			//filterPanel1.removeAll();
			for(Iterator<JComboBox>i=st.filters.iterator();i.hasNext();) {
				JComboBox cb = i.next();
				if (cb!=null) filterPanel.add(cb);
			}	
			
			
		}
		else {
		
			st = new StatsTable(details,types, parent);
			
			tp.removeAll();
			//tp = addPanel("자세한 분석",BorderLayout.CENTER);
			//tp = addPanel("자세한 분석");
			//tp.add(test);
			//tp.addPanel(testPanel);
			tp.add(new JScrollPane(st.jt), BorderLayout.NORTH);
			filterPanel.removeAll();
			
			//filterPanel1.removeAll();
			for(Iterator<JComboBox>i=st.filters.iterator();i.hasNext();) {
				JComboBox cb = i.next();
				if (cb!=null) filterPanel.add(cb);
			}	
		}
	}
	
	public void setFilterNote(int x, int y) {
		fnote.setText("["+FTDriver.nf.format(x)+ " 의 " + FTDriver.nf.format(y) + " 보여지는 중]");//of		showing
	}
}

