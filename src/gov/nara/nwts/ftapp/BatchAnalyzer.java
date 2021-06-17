package gov.nara.nwts.ftapp;

import gov.nara.nwts.ftapp.filetest.ActionRegistry;
import gov.nara.nwts.ftapp.filetest.FileTest;
import gov.nara.nwts.ftapp.filter.FileTestFilter;
import gov.nara.nwts.ftapp.ftprop.FTProp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Driver for the command line version of the File Analyzer (for performing File Tests)
 * This application was originally created by Terry Brady in NARA's Digitization Services Branch.
 * @author TBrady
 *
 */
public class BatchAnalyzer {
	FTDriver dt;
	File root;
	File batch;
	File outdir;
	String outfile;
	HashMap<String,String>params;
	
	FileTest ft;
	FileTestFilter ftf;
	String filtername;
	int max = 500000;
	ActionRegistry ar;
	boolean overwrite = true;
	boolean listfilters = false;
	boolean listparams = false;
	
	public ActionRegistry getActionRegistry(FTDriver dt) {
		return new ActionRegistry(dt, false);
	}
	public BatchAnalyzer() {
		root = new File(System.getProperty("user.dir"));
		dt = new FTDriver(root);
		ar = getActionRegistry(dt);
		outdir = new File(System.getProperty("user.dir"));
		outfile = "";
		params = new HashMap<String,String>();
	}
	
	public String getArg(String[] args, int i) {
		if (i >= args.length) reportError("Missing argument for "+args[args.length-1]);//Missing argument for
		String s = args[i];
		if ((s.length() > 2) && (s.startsWith("\"")) && (s.endsWith("\""))) {
			return s.substring(1,s.length()-2);
		}
		return s;
	}
	
	public void parse(String[] args) {
		String ftname = null;
		root = null;
		for(int i=0; i<args.length; i++) {
			String s = args[i];
			if (s.equalsIgnoreCase("-����")) {//help
				reportUsage();
				System.exit(0);
			} else if (s.equalsIgnoreCase("-��� �Ű�����")) {//listparams
				listparams = true;
			} else if (s.equalsIgnoreCase("-��� ����")) {//listfilters
				listfilters = true;
			} else if (s.equalsIgnoreCase("-��� �м�")) {//listtests
				listTests();
				System.exit(0);

			} else if (s.equalsIgnoreCase("-�����")) {//overwrite
				overwrite = !getArg(args,++i).equals("����");//false

			} else if (s.equalsIgnoreCase("-��������")) {//root
				root = new File(getArg(args,++i));
			} else if (s.equalsIgnoreCase("-����")) {//filter
				filtername = getArg(args,++i);
			} else if (s.equalsIgnoreCase("-�ϰ� ����")) {//batchfile
				batch = new File(getArg(args,++i));
			} else if (s.equalsIgnoreCase("-��� ���丮")) {//outdir
				outdir = new File(getArg(args,++i));				
			} else if (s.equalsIgnoreCase("-��� ����")) {//outfile
				outfile = getArg(args,++i);

			} else if (s.equalsIgnoreCase("-���λ�")) {//prefix
				dt.prefix = getArg(args,++i);
			} else if (s.equalsIgnoreCase("-���̻�")) {//suffix
				dt.suffix = getArg(args,++i);
			} else if (s.equalsIgnoreCase("-�����ϴ�")) {//contains
				dt.contains = getArg(args,++i);
			} else if (s.equalsIgnoreCase("-�����ϴ�")) {//excludes
				dt.excludes = getArg(args,++i);

			} else if (s.equalsIgnoreCase("-rf")) {
				String colstr = getArg(args,++i);
				String val = getArg(args,++i);
				try {
					int col = Integer.parseInt(colstr);
					if (col < 2) {
						reportError("���� ���� 1���� Ŀ���մϴ�.");//Column number must be greater than 1
					}
					dt.myresfilter.add(col-2, val);
				} catch (NumberFormatException e) {
					reportError("���� ���� ������ -rf <��> <��>");//Column must be a number -rf <col> <row>
				}

			} else if (s.equalsIgnoreCase("-�Ű�����")) {//param
				String pname = getArg(args,++i);
				String pval = getArg(args,++i);
				params.put(pname, pval);

			} else if (s.equalsIgnoreCase("-�ִ�ũ��")) {//max
				try {
					max = Integer.parseInt(getArg(args,++i));
				} catch (NumberFormatException e) {
					reportError("�ִ� ũ��� ���� ǥ�õǾ�� �մϴ�.");//Max value must be a number
				}
			} else if (s.startsWith("-")) {
				reportError("�߸��� �ɼ�: "+s);//Invalid Option
			} else if (ftname == null){
				ftname = s;
				for(FileTest ftest: ar) {
					if (ftname.equalsIgnoreCase(ftest.getShortNameNormalized())){
						ft = ftest;
					}
				}
				if (ft == null) {
					reportError("�߸��� ���� �м� �̸�: "+s);//Invalid File Test name
				}
			} else {
				reportError("������� ���� �μ�: "+s);//Unexpected argument
			}
		}
		
		if ((batch != null) && ((root !=null) || !outfile.equals(""))){
			reportError("�ϰ������� ������ ��, ���� ���丮�� ��� ������ �������� �ʾƾ��մϴ�.");			
		}//When specifying -batchfile, neither a root dir nor an output file should be specified
		
		if (root == null) {
			root = new File(System.getProperty("user.dir"));
		}
		
		if (ft == null) {
			reportError("������ ���� �м� ����");//No File Test Specified
		}
		
		if (listfilters) {
			reportFilters(ft);
			System.exit(0);
		}
		
		if (listparams) {
			reportParams(ft);
			System.exit(0);
		}
		if (filtername !=null) {
			for(FileTestFilter fil: ft.getFilters()){
				if (fil.getShortNameNormalized().equalsIgnoreCase(filtername)) {
					ftf = fil;
				}
			}
			if (ftf == null) {
				reportFilters(ft);
				reportError("���� �м��� ���� �߸��� ���� �̸�: "+ft);
			} else {//Invalid Filter Name for File Test
				dt.myfilter = ftf;
			}
		}
		for(String param: params.keySet()) {
			ft.setProperty(param, params.get(param));
		}
	}
	
	public void reportFilters(FileTest ft) {
		System.out.println("���Ϻм��� ���� ����: "+ft.toString());//Filters for File Test
		for(FileTestFilter fil: ft.getFilters()){
			System.out.println("\t"+fil.getShortNameFormatted()+"\t"+fil.getName());
		}
		System.out.flush();		
	}
	
	public void reportParams(FileTest ft) {
		System.out.println("���� �м��� ���� �Ű�����: "+ft.toString());//Parameters for File Test
		for(FTProp ftprop: ft.getPropertyList()){
			System.out.println("\t"+ftprop.getShortNameFormatted()+"\t"+ftprop.getName());
			System.out.println(ftprop.describeFormatted());
		}
		System.out.flush();		
	}
	
	public void reportError(String s) {
		System.err.println("*����:"+s);//ERROR
		System.err.println("��ɾ� ����� ���� ���� ���");
		System.exit(10);	//Pass -help for command line usage	
	}
	
	public boolean run() {
		dt.overwrite = overwrite;
		dt.root = root;
		dt.saveDir = outdir;
		dt.saveFile = outfile;
		dt.fileTraversal.setTraversal(ft, max);		
		if (batch!=null){
			try {
				dt.loadBatch(batch);
				dt.batchStart();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return dt.fileTraversal.traverseFile();					
		}
	}

	public void reportUsage() {
		System.out.println("");
		System.out.println("����:");//Usage
		System.out.println("\t�ϰ��м��� [-�ɼǵ�] [-�������� ���۵��丮] [-������� �������] ���Ϻм�");
		//BatchAnalyzer [-options] [-root rootdir] [-outfile outfile] filetest
		System.out.println("\t\t���� ���丮�� ó���Ϸ���, �ϳ��� ���Ͽ� ����� ����ϼ���.");
		//to process a single directory, writing results to one file
		System.out.println("\t\t���� �������� �ʴ´ٸ�, ���۵��丮�� �⺻������ ���� ���丮�� �����˴ϴ�. \n\t\t ���� �������� �ʴ´ٸ�, ��������� �����˴ϴ�.");
		//If absent, rootdir will default to current directory.\n\t\tIf absent, outfile will be generated
		System.out.println("Ȥ��");//or
		System.out.println("\t�ϰ��м��� [-�ɼǵ�] -�ϰ����� �ϰ� ���Ϻм�");
		//BatchAnalyer [-options] -batchfile batch filetest
		System.out.println("\t\t���丮���� ������ ó���ϴ� ");
		//to process a collection of directories
		System.out.println("\t\t�ϰ� ������ ������ ���е� �����Դϴ�:");
		//The batch file is a tab separated file containing
		System.out.println("\t\t\t���� ���丮 --> ��� ���� �̸�");
		//rootdir --> result filenames
		System.out.println("");
		System.out.println("�ɼǵ��� ���Ե� ���");
		//where options include
		System.out.println("\t-��� ���丮 <���丮>  \t��������� ����� ���丮.\n\t\t\t\t�۾� ���丮�� �⺻ ����");
		//-outdir <dir>  \tDirectory to which output files will be written.\n\t\t\t\tDefaults to working directory
		System.out.println("\t-�ִ� �� <����>     \t�⺻ ������ 500000 ����");
		//-max <num>     \tdefaults to 500000
		System.out.println("\t-�����     \t�⺻ ������ ���� ����");
		//overwrite     \tdefaults to true
		System.out.println("\t-�Ű����� ���    \t���� �м��� �����ִ� �Ű� �������� �����մϴ�.");
		//listparams    \tlists the parameters associated with a file test
		System.out.println("\t-���� ���   \t���� �м����� �����ִ� ���͵��� �����մϴ�.");
		//listfilters   \tlists the filters associated with a file test
		System.out.println("\t-���� <�̸�> \t���� ������ �̸�.\n\t\t\t\tó�� ���͸� �⺻ ������ ����");
		//filter <name> \tname of the filter to use.\n\t\t\t\tdefaults to the first filter
		System.out.println("");
		System.out.println("���� �м� �Ű�����");
		//filetest parameters
		System.out.println("\t-�Ű����� <�̸�> <��>\t���� �м� Ư�� �Ű����� ���");
		//-param <name> <val>\tPass filetest specific parameters
		System.out.println("\t               \t���� ���� �Ű����� ������ ������ �� �ֽ��ϴ�");
		//Multiple param vals may be provided
		System.out.println("");
		System.out.println("���� �ɼ��� ������ �⺻������ ������ �˴ϴ�");
		//the following options override filter defaults
		System.out.println("\t-���λ� <��>  \t���� �̸��� �� ������ �����ؾ��մϴ�.");
		//prefix <val>  \tFilenames must start with this value
		System.out.println("\t-���̻� <��>  \t���� �̸��� �� ������ ���� �����մϴ�.");
		//suffix <val>  \tFilenames must end with this value
		System.out.println("\t-���Ե� <��>\t���� �̸����� �� ���� ���ԵǾ�� �մϴ�.");
		//contains <val>\tFilenames must contain this value
		System.out.println("\t-���ܵ� <��>\t���� �̸��� �� ���� ������ �ȵ˴ϴ�.");
		//excludes <val>\tFilenames may not contain this value
		System.out.println("");
		System.out.println("��� ���͸� �ɼǵ�");
		//result filtering options
		System.out.println("\t-rf <��> <��>\t<��>�� <��>�� ��ġ�ϴ� ���� ��� �����");
		//-rf <col> <val>\tOnly output results where column <col> matches <val> ���� ��� ������� ���� <��> �� <��>��
		System.out.println("\t                    ���� rf ������ ���� �� �� �ִ�.\t");
		//Multiple rf vals may be provided
		listTests();
	}
	
	public void listTests() {
		System.out.println("���Ϻм�");//filetest
		for(FileTest ftest: ar) {
			System.out.println("\t"+ftest.getShortNameFormatted()+"\t"+ftest.toString());
		}
		System.out.flush();
	}
	
	public void report() {
		System.out.println("���� ���丮:  \t" +root.getAbsolutePath());
		//Root Directoreeeeeey
		System.out.println("��� ���丮:\t" +outdir.getAbsolutePath());
		//Output Directory
		System.out.println("��� ����:     \t" +outfile);
		//Output File
		System.out.println("���Ϻм�:        \t" + ft.getShortNameNormalized()+": "+ft.toString());
		//FileTest
		System.out.println("���� �ִ밪:       \t" + max);
		//Max Files
		System.out.println("�����:       \t" + overwrite);
		//Overwrite
		System.out.flush();
	}

	public static void main(String[] args) {
		BatchAnalyzer ba = new BatchAnalyzer();
		ba.parse(args);
		ba.report();
		boolean b = ba.run();
		System.out.println("�ϼ�:      \t" + b);
		//Completion
		System.out.flush();
	}

}
