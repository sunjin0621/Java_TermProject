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
			if (s.equalsIgnoreCase("-도움말")) {//help
				reportUsage();
				System.exit(0);
			} else if (s.equalsIgnoreCase("-목록 매개변수")) {//listparams
				listparams = true;
			} else if (s.equalsIgnoreCase("-목록 필터")) {//listfilters
				listfilters = true;
			} else if (s.equalsIgnoreCase("-목록 분석")) {//listtests
				listTests();
				System.exit(0);

			} else if (s.equalsIgnoreCase("-덮어쓰기")) {//overwrite
				overwrite = !getArg(args,++i).equals("실패");//false

			} else if (s.equalsIgnoreCase("-시작지점")) {//root
				root = new File(getArg(args,++i));
			} else if (s.equalsIgnoreCase("-필터")) {//filter
				filtername = getArg(args,++i);
			} else if (s.equalsIgnoreCase("-일괄 파일")) {//batchfile
				batch = new File(getArg(args,++i));
			} else if (s.equalsIgnoreCase("-출력 디렉토리")) {//outdir
				outdir = new File(getArg(args,++i));				
			} else if (s.equalsIgnoreCase("-출력 파일")) {//outfile
				outfile = getArg(args,++i);

			} else if (s.equalsIgnoreCase("-접두사")) {//prefix
				dt.prefix = getArg(args,++i);
			} else if (s.equalsIgnoreCase("-접미사")) {//suffix
				dt.suffix = getArg(args,++i);
			} else if (s.equalsIgnoreCase("-포함하다")) {//contains
				dt.contains = getArg(args,++i);
			} else if (s.equalsIgnoreCase("-제외하다")) {//excludes
				dt.excludes = getArg(args,++i);

			} else if (s.equalsIgnoreCase("-rf")) {
				String colstr = getArg(args,++i);
				String val = getArg(args,++i);
				try {
					int col = Integer.parseInt(colstr);
					if (col < 2) {
						reportError("열의 수는 1보다 커야합니다.");//Column number must be greater than 1
					}
					dt.myresfilter.add(col-2, val);
				} catch (NumberFormatException e) {
					reportError("열의 수는 무조건 -rf <열> <행>");//Column must be a number -rf <col> <row>
				}

			} else if (s.equalsIgnoreCase("-매개변수")) {//param
				String pname = getArg(args,++i);
				String pval = getArg(args,++i);
				params.put(pname, pval);

			} else if (s.equalsIgnoreCase("-최대크기")) {//max
				try {
					max = Integer.parseInt(getArg(args,++i));
				} catch (NumberFormatException e) {
					reportError("최대 크기는 수로 표시되어야 합니다.");//Max value must be a number
				}
			} else if (s.startsWith("-")) {
				reportError("잘못된 옵션: "+s);//Invalid Option
			} else if (ftname == null){
				ftname = s;
				for(FileTest ftest: ar) {
					if (ftname.equalsIgnoreCase(ftest.getShortNameNormalized())){
						ft = ftest;
					}
				}
				if (ft == null) {
					reportError("잘못된 파일 분석 이름: "+s);//Invalid File Test name
				}
			} else {
				reportError("예상되지 않은 인수: "+s);//Unexpected argument
			}
		}
		
		if ((batch != null) && ((root !=null) || !outfile.equals(""))){
			reportError("일괄파일을 지정할 때, 시작 디렉토리와 결과 파일을 지정하지 않아야합니다.");			
		}//When specifying -batchfile, neither a root dir nor an output file should be specified
		
		if (root == null) {
			root = new File(System.getProperty("user.dir"));
		}
		
		if (ft == null) {
			reportError("지정된 파일 분석 없음");//No File Test Specified
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
				reportError("파일 분석에 대한 잘못된 필터 이름: "+ft);
			} else {//Invalid Filter Name for File Test
				dt.myfilter = ftf;
			}
		}
		for(String param: params.keySet()) {
			ft.setProperty(param, params.get(param));
		}
	}
	
	public void reportFilters(FileTest ft) {
		System.out.println("파일분석에 대한 필터: "+ft.toString());//Filters for File Test
		for(FileTestFilter fil: ft.getFilters()){
			System.out.println("\t"+fil.getShortNameFormatted()+"\t"+fil.getName());
		}
		System.out.flush();		
	}
	
	public void reportParams(FileTest ft) {
		System.out.println("파일 분석을 위한 매개변수: "+ft.toString());//Parameters for File Test
		for(FTProp ftprop: ft.getPropertyList()){
			System.out.println("\t"+ftprop.getShortNameFormatted()+"\t"+ftprop.getName());
			System.out.println(ftprop.describeFormatted());
		}
		System.out.flush();		
	}
	
	public void reportError(String s) {
		System.err.println("*오류:"+s);//ERROR
		System.err.println("명령어 사용을 위한 도움말 통과");
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
		System.out.println("사용법:");//Usage
		System.out.println("\t일괄분석기 [-옵션들] [-시작지점 시작디렉토리] [-출력파일 출력피일] 파일분석");
		//BatchAnalyzer [-options] [-root rootdir] [-outfile outfile] filetest
		System.out.println("\t\t단일 디렉토리를 처리하려면, 하나의 파일에 결과를 기록하세요.");
		//to process a single directory, writing results to one file
		System.out.println("\t\t만약 존재하지 않는다면, 시작디렉토리는 기본적으로 현재 디렉토리로 설정됩니다. \n\t\t 만약 존재하지 않는다면, 출력파일이 생성됩니다.");
		//If absent, rootdir will default to current directory.\n\t\tIf absent, outfile will be generated
		System.out.println("혹은");//or
		System.out.println("\t일괄분석기 [-옵션들] -일괄파일 일괄 파일분석");
		//BatchAnalyer [-options] -batchfile batch filetest
		System.out.println("\t\t디렉토리들의 집단을 처리하다 ");
		//to process a collection of directories
		System.out.println("\t\t일괄 파일이 탭으로 구분된 파일입니다:");
		//The batch file is a tab separated file containing
		System.out.println("\t\t\t시작 디렉토리 --> 결과 파일 이름");
		//rootdir --> result filenames
		System.out.println("");
		System.out.println("옵션들이 포함된 장소");
		//where options include
		System.out.println("\t-출력 디렉토리 <디렉토리>  \t출력파일을 기록할 디렉토리.\n\t\t\t\t작업 디렉토리로 기본 설정");
		//-outdir <dir>  \tDirectory to which output files will be written.\n\t\t\t\tDefaults to working directory
		System.out.println("\t-최대 값 <숫자>     \t기본 값으로 500000 설정");
		//-max <num>     \tdefaults to 500000
		System.out.println("\t-덮어쓰기     \t기본 값으로 참을 설정");
		//overwrite     \tdefaults to true
		System.out.println("\t-매개변수 목록    \t파일 분석과 연관있는 매개 변수들을 나열합니다.");
		//listparams    \tlists the parameters associated with a file test
		System.out.println("\t-필터 목록   \t파일 분석과ㅏ 연관있는 필터들을 나열합니다.");
		//listfilters   \tlists the filters associated with a file test
		System.out.println("\t-필터 <이름> \t사용될 필터의 이름.\n\t\t\t\t처음 필터를 기본 값으로 지정");
		//filter <name> \tname of the filter to use.\n\t\t\t\tdefaults to the first filter
		System.out.println("");
		System.out.println("파일 분석 매개변수");
		//filetest parameters
		System.out.println("\t-매개변수 <이름> <값>\t파일 분석 특정 매개변수 통과");
		//-param <name> <val>\tPass filetest specific parameters
		System.out.println("\t               \t여러 개의 매개변수 값들을 제공할 수 있습니다");
		//Multiple param vals may be provided
		System.out.println("");
		System.out.println("다음 옵션은 필터의 기본값으로 재정의 됩니다");
		//the following options override filter defaults
		System.out.println("\t-접두사 <값>  \t파일 이름은 이 값으로 시작해야합니다.");
		//prefix <val>  \tFilenames must start with this value
		System.out.println("\t-접미사 <값>  \t파일 이름은 이 값으로 끝이 나야합니다.");
		//suffix <val>  \tFilenames must end with this value
		System.out.println("\t-포함된 <값>\t파일 이름에는 이 값이 포함되어야 합니다.");
		//contains <val>\tFilenames must contain this value
		System.out.println("\t-제외된 <값>\t파일 이름에 이 값이 들어가서는 안됩니다.");
		//excludes <val>\tFilenames may not contain this value
		System.out.println("");
		System.out.println("결과 필터링 옵션들");
		//result filtering options
		System.out.println("\t-rf <열> <값>\t<열>과 <값>이 일치하는 열의 출력 결과만");
		//-rf <col> <val>\tOnly output results where column <col> matches <val> 오직 출력 결과만이 열의 <열> 과 <값>이
		System.out.println("\t                    다중 rf 값들이 제공 될 수 있다.\t");
		//Multiple rf vals may be provided
		listTests();
	}
	
	public void listTests() {
		System.out.println("파일분석");//filetest
		for(FileTest ftest: ar) {
			System.out.println("\t"+ftest.getShortNameFormatted()+"\t"+ftest.toString());
		}
		System.out.flush();
	}
	
	public void report() {
		System.out.println("시작 디렉토리:  \t" +root.getAbsolutePath());
		//Root Directoreeeeeey
		System.out.println("출력 디렉토리:\t" +outdir.getAbsolutePath());
		//Output Directory
		System.out.println("출력 파일:     \t" +outfile);
		//Output File
		System.out.println("파일분석:        \t" + ft.getShortNameNormalized()+": "+ft.toString());
		//FileTest
		System.out.println("파일 최대값:       \t" + max);
		//Max Files
		System.out.println("덮어쓰기:       \t" + overwrite);
		//Overwrite
		System.out.flush();
	}

	public static void main(String[] args) {
		BatchAnalyzer ba = new BatchAnalyzer();
		ba.parse(args);
		ba.report();
		boolean b = ba.run();
		System.out.println("완성:      \t" + b);
		//Completion
		System.out.flush();
	}

}
