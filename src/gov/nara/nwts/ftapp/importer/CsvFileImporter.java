package gov.nara.nwts.ftapp.importer;

import gov.nara.nwts.ftapp.FTDriver;

/**
 * Importer for comma delimited files
 * @author TBrady
 *
 */
public class CsvFileImporter extends DelimitedFileImporter {

	public CsvFileImporter(FTDriver dt) {
		super(dt);
	}

	public String getSeparator() {
		return ",";
	}

	public String toString() {
		return "쉼표로 구분된 파일 가져오기";//Import Comma-Separated File
	}
	public String getDescription() {
		return "쉼표로 구분된 파일을 가져오고 첫 번째 열을 고유키로 사용합니다.";
	}//This rule will import a comma separated file and use the first column as a unique key
	public String getShortName() {
		return "CSV";// comma-separated variables
	}
}
