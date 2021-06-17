package gov.nara.nwts.ftapp;

import gov.nara.nwts.ftapp.importer.Importer;
import gov.nara.nwts.ftapp.importer.ImporterRegistry;

import java.io.File;
import java.io.IOException;

/**
 * Driver for the command line version of the File Analyzer (for performing file imports)
 * This application was originally created by Terry Brady in NARA's Digitization Services Branch.
 * @author TBrady
 *
 */
public class BatchImporter {
	FTDriver dt;
	File infile;
	File outdir;
	String outfile;
	Importer impsel;
	ImporterRegistry ar;
	boolean overwrite = true;
	
	public ImporterRegistry getImporterRegistry(FTDriver dt) {
		return new ImporterRegistry(dt);
	}
	public BatchImporter() {
		dt = new FTDriver(infile);
		ar = getImporterRegistry(dt);
		outdir = new File(System.getProperty("user.dir"));
		outfile = "";
	}
	
	public String getArg(String[] args, int i) {
		if (i >= args.length) reportError("Missing argument for "+args[args.length-1]);
		String s = args[i];
		if ((s.length() > 2) && (s.startsWith("\"")) && (s.endsWith("\""))) {
			return s.substring(1,s.length()-2);
		}
		return s;
	}
	
	public void parse(String[] args) {
		String ftname = null;
		for(int i=0; i<args.length; i++) {
			String s = args[i];
			if (s.equalsIgnoreCase("-����")) {
				reportUsage();
				System.exit(0);
			} else if (s.equalsIgnoreCase("-�����")) {
				overwrite = !getArg(args,++i).equals("����");
			} else if (s.equalsIgnoreCase("-�Է� ����")) {//infile
				infile = new File(getArg(args,++i));
			} else if (s.equalsIgnoreCase("-��� ���丮")) {
				outdir = new File(getArg(args,++i));				
			} else if (s.equalsIgnoreCase("-�������")) {
				outfile = getArg(args,++i);
			} else if (s.startsWith("-")) {
				reportError("�߸��� �ɼ�: "+s);
			} else if (ftname == null){
				ftname = s;
				for(Importer imp: ar) {
					if (ftname.equalsIgnoreCase(imp.getShortNameNormalized())){
						impsel = imp;
					}
				}
				if (impsel == null) {
					reportError("��ȿ���� ���� ���� �м� �̸�: "+s);//Invalid File Test name
				}
			} else {
				reportError("�������� ���� �Ű�����: "+s);
			}
		}
		
		if (infile == null) {
			reportError("�Է� ������ ��õ��� ����");
		}
		if (impsel == null) {
			reportError("���� �м��� ��õ��� ����");
		}
		
	}
	
	public void reportError(String s) {
		System.err.println("*����:"+s);
		System.err.println("��ɹ��� ����ϱ� ���� ���� ���");
		System.exit(10);		
	}
	
	public boolean run() {
		dt.overwrite = overwrite;
		dt.saveDir = outdir;
		dt.saveFile = outfile;
		try {
			dt.importFile(impsel, infile);
		} catch (IOException e) {
			reportError(e.getMessage());
			return false;
		}
		return true;
	}

	public void reportUsage() {
		System.out.println("");
		System.out.println("Usage:");
		System.out.println("\t�ѹ��� �������� [-�ɼǵ�] -infile inputfile importer");
		//BatchImporter		infile inputfile importer
		System.out.println("");
		System.out.println("�ɼǵ��� ���ԵǴ� ��");//where options include
		System.out.println("\t-��� ���丮   \t��� ���ϵ��� ������ ���丮.\n\t\t�۾� ���丮�� �⺻ ����");
		//Directory to which output files will be written.\n\t\tDefaults to working directory
		System.out.println("\t-��� ����  \t��� ���� �̸�, �⺻ ������ �ý��ۿ��� ������ �̸��� ����");
		//Output file name, defaults to a system generated name
		System.out.println("\t-�ִ밪      \t�⺻ ������ 500000 ����");
		System.out.println("\t-�����\t�⺻ ���� ������ ����.");
		//overwrite\tdefaults to true
		System.out.println("");
		System.out.println("��������");//importer
		for(Importer imp: ar) {
			System.out.println("\t\t"+imp.getShortNameFormatted()+"\t"+imp.toString());
		}
	}
	public void report() {
		System.out.println("�Է� ����:      \t" +infile.getAbsolutePath());
		//Input File
		System.out.println("��� ���丮:\t" +outdir.getAbsolutePath());
		//Output Directory
		System.out.println("��� ����:     \t" +outfile);
		//Output File
		System.out.println("��������:        \t" + impsel.getShortNameNormalized()+": "+impsel.toString());
		//Importer
		System.out.println("�����:       \t" + overwrite);
		//Overwrite
		System.out.flush();
	}

	public static void main(String[] args) {
		BatchImporter ba = new BatchImporter();
		ba.parse(args);
		ba.report();
		boolean b = ba.run();
		System.out.println("�ϼ�:      \t" + b);
		//Completion
		System.out.flush();
	}

}
