package gov.nara.nwts.ftapp.gui;

import gov.nara.nwts.ftapp.filetest.FileTest;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * User interface component defining a File Test to be performed
 * @author TBrady
 *
 */
class CriteriaPanel extends MyPanel {
	public static final int[] LIMITS = {10000,20000,40000,100000,500000,1000000,2000000,5000000,100,200,500,1000,2000,5000};
	private static final long serialVersionUID = 1L;
	JTextField rootLabel;
	JButton analyze;

	JTabbedPane propFilter;
	JPanel propPanel;
	JTabbedPane filterTabs;
	JComboBox actions;
	JComboBox limit;
	JCheckBox ignorePeriods;
	JTextArea description;
	JCheckBox autoSave;
	DirSelectChooser fsc;
	JTextField fctf;
	DirectoryTable parent;
	
	CriteriaPanel(DirectoryTable dt) {
		this.parent = dt;
		JPanel p = addPanel("스캔할 루트 디렉토리 선택.");//Root Directory to Scan
		rootLabel = new JTextField(50);
		rootLabel.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent arg0) {
			}
			public void focusLost(FocusEvent arg0) {
				parent.setSelectedFile();
			}});
		p.add(rootLabel);
		JButton jb = new JButton("...");
		jb.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					new FileCatalog(parent);
				}
			}
		);
		p.add(jb);
		jb = new JButton("이전목록");//Recent
		p.add(jb);
		jb.addActionListener(
				new ActionListener(){
					@SuppressWarnings("unchecked")
					public void actionPerformed(ActionEvent arg0) {
						ArrayList<File> recentrev = (ArrayList<File>)parent.recent.clone();
						Collections.reverse(recentrev);
						File o = (File)JOptionPane.showInputDialog(
							parent.frame,
							"최신에 스캔된 디렉토리를 선택합니다",//Select a recently scanned directory
							"최신 티렉토리",//Recent Directories
							JOptionPane.INFORMATION_MESSAGE,
							null,
							recentrev.toArray(),
							null
						);
						if (o != null) {
							parent.criteriaPanel.rootLabel.setText(o.getAbsolutePath());
							parent.setSelectedFile();								
						}
					}
				}
			);

		p = addPanel("디렉토리 식별");//Directory Identification
		ignorePeriods = new JCheckBox();
		ignorePeriods.setSelected(true);
		ignorePeriods.setText("디렉토리 이름에 마침표가 없다고 가정합니다.(더 빠르게 분석)");//Assume directory names do not contain periods (faster)
		p.add(ignorePeriods);

		p = addBorderPanel("자동 결과물 저장 디렉토리");//Auto-save Output directory
		fsc = new DirSelectChooser(parent.frame, "결과물 저장할 폴더", parent.preferences, "출력디렉토리", "");//Output dir for results,	outdir
		p.add(fsc, BorderLayout.CENTER);
		JPanel pp = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(pp, BorderLayout.SOUTH);
		autoSave = new JCheckBox("자동으로 결과물 저장");//Auto-save Results
		autoSave.setSelected(parent.preferences.getBoolean("자동저장", false));//autoSave
		autoSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				parent.preferences.putBoolean("자동저장하기", autoSave.isSelected());//autoSave
				fctf.setEnabled(autoSave.isSelected());
			}
		});
		autoSave.setToolTipText("장시간 실행되는 프로세스의 결과를 자동으로 저장할 수 있습니다. \n선택되지 않은 경우, 수동으로 결과를 저장해야합니다.");//The system has the ability to auto-save the results of long running processes.If not selected, you must explicitly export results.
		pp.add(autoSave);
		fctf = new JTextField(10);
		fctf.setToolTipText("출력 파일에 할당할 파일 이름(확장자 없음)입니다. 공백인 경우 생성된 파일이름이 사용됩니다.");//Base filename (WITHOUT extension) to assign to output file. If blank, a generated filename will be used.
		fctf.setEnabled(autoSave.isSelected());
		pp.add(fctf);
		
		p = addPanel("파일에 수행할 작업");//Action to perform on Files
		actions = new JComboBox(parent.actionRegistry);
		
		String action = parent.preferences.get("action", "");
		for(int i=0; i<actions.getItemCount(); i++){
			FileTest ft = (FileTest)actions.getItemAt(i);
			if (ft.toString().equals(action)) {
				actions.setSelectedIndex(i);
				break;
			}
		}
		
		p.add(actions);
		actions.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					parent.setFilters();
				}
			}
		);

		
		limit = new JComboBox();
		for(int i=0; i<LIMITS.length; i++) {
			limit.addItem(LIMITS[i]);
		}
		int deflimit = parent.preferences.getInt("limit", 0);
		limit.setSelectedItem(deflimit);
		limit.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					parent.preferences.putInt("limit", (Integer)limit.getSelectedItem());
				}
			}
		);
		
		
		p.add(new JLabel("최대 분석개수: "));//Max Items
		p.add(limit);

		
		p = addPanel("수행될 작업에 대한 설명입니다.");//Description of test to be performed
		description = new JTextArea(4,60);
		description.setEditable(false);
		description.setLineWrap(true);
		description.setBackground(parent.frame.getBackground());
		description.setFont(description.getFont().deriveFont(Font.ITALIC));
		p.add(new JScrollPane(description));

		JPanel propFilterPanel = addPanel();
		propFilter = new JTabbedPane();
		propFilterPanel.add(propFilter, BorderLayout.CENTER);
		filterTabs = new JTabbedPane();
		propFilter.add(filterTabs,"파일 필터 조건");//File Filter Criteria

		p = addPanel();
		analyze = new JButton("분석하기");//Analyze
		analyze.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					try {
						parent.initiateFileTest();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		);
		analyze.setEnabled(false);
		p.add(analyze);

		propPanel = new JPanel();
		JScrollPane sp = new JScrollPane(propPanel);
		sp.setPreferredSize(filterTabs.getPreferredSize());
		propFilter.add(sp,"파일 테스트 속성");//File Test Properties
		
	}
}