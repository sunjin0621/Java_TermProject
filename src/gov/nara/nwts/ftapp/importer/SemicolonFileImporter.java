package gov.nara.nwts.ftapp.importer;

import gov.nara.nwts.ftapp.FTDriver;

/**
 * Importer for semicolon delimited files
 * @author TBrady
 *
 */
public class SemicolonFileImporter extends DelimitedFileImporter {

	public SemicolonFileImporter(FTDriver dt) {
		super(dt);
	}

	public String getSeparator() {
		return ";";
	}

	public String toString() {
		return "세미콜론으로 분리된 파일 가져오기";//Import Semicolon-Separated File
	}
	public String getDescription() {
		return "세미콜론으로 구분된 파일을 가져오고 첫 번째 열을 고유한 키로 사용합니다.";
	}//This rule will import a semicolon separated file and use the first column as a unique key
	public String getShortName() {
		return "SEMI";
	}

}
