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
			if (s.equalsIgnoreCase("-도움말")) {
				reportUsage();
				System.exit(0);
			} else if (s.equalsIgnoreCase("-덮어쓰기")) {
				overwrite = !getArg(args,++i).equals("거짓");
			} else if (s.equalsIgnoreCase("-입력 파일")) {//infile
				infile = new File(getArg(args,++i));
			} else if (s.equalsIgnoreCase("-출력 디렉토리")) {
				outdir = new File(getArg(args,++i));				
			} else if (s.equalsIgnoreCase("-출력파일")) {
				outfile = getArg(args,++i);
			} else if (s.startsWith("-")) {
				reportError("잘못된 옵션: "+s);
			} else if (ftname == null){
				ftname = s;
				for(Importer imp: ar) {
					if (ftname.equalsIgnoreCase(imp.getShortNameNormalized())){
						impsel = imp;
					}
				}
				if (impsel == null) {
					reportError("유효하지 않은 파일 분석 이름: "+s);//Invalid File Test name
				}
			} else {
				reportError("예상하지 못한 매개변수: "+s);
			}
		}
		
		if (infile == null) {
			reportError("입력 파일이 명시되지 않음");
		}
		if (impsel == null) {
			reportError("파일 분석이 명시되지 않음");
		}
		
	}
	
	public void reportError(String s) {
		System.err.println("*오류:"+s);
		System.err.println("명령문을 사용하기 위한 도움말 통과");
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
		System.out.println("\t한번에 가져오기 [-옵션들] -infile inputfile importer");
		//BatchImporter		infile inputfile importer
		System.out.println("");
		System.out.println("옵션들이 포함되는 곳");//where options include
		System.out.println("\t-출력 디렉토리   \t출력 파일들이 쓰여질 디렉토리.\n\t\t작업 디렉토리로 기본 설정");
		//Directory to which output files will be written.\n\t\tDefaults to working directory
		System.out.println("\t-출력 파일  \t출력 파일 이름, 기본 값으로 시스템에서 생성된 이름을 설정");
		//Output file name, defaults to a system generated name
		System.out.println("\t-최대값      \t기본 값으로 500000 설정");
		System.out.println("\t-덮어쓰기\t기본 값을 참으로 설정.");
		//overwrite\tdefaults to true
		System.out.println("");
		System.out.println("가져오기");//importer
		for(Importer imp: ar) {
			System.out.println("\t\t"+imp.getShortNameFormatted()+"\t"+imp.toString());
		}
	}
	public void report() {
		System.out.println("입력 파일:      \t" +infile.getAbsolutePath());
		//Input File
		System.out.println("출력 디렉토리:\t" +outdir.getAbsolutePath());
		//Output Directory
		System.out.println("출력 파일:     \t" +outfile);
		//Output File
		System.out.println("가져오기:        \t" + impsel.getShortNameNormalized()+": "+impsel.toString());
		//Importer
		System.out.println("덮어쓰기:       \t" + overwrite);
		//Overwrite
		System.out.flush();
	}

	public static void main(String[] args) {
		BatchImporter ba = new BatchImporter();
		ba.parse(args);
		ba.report();
		boolean b = ba.run();
		System.out.println("완성:      \t" + b);
		//Completion
		System.out.flush();
	}

}
