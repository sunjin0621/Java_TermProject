package gov.nara.nwts.ftapp.importer;

import gov.nara.nwts.ftapp.FTDriver;

/**
 * Importer for tab delimited files
 * @author TBrady
 *
 */
public class TabSepFileImporter extends DelimitedFileImporter {

	public TabSepFileImporter(FTDriver dt) {
		super(dt);
	}

	public String getSeparator() {
		return "\t";
	}
	public String toString() {
		return "탭으로 분리된 파일가져오기";//Import Tab-Separated File
	}
	public String getDescription() {
		return "탭으로 구분된 파일을 가져오고 첫 번째 열을 고유 키로 사용합니다.";
	}//This rule will import a tab separated file and use the first column as a unique key
	public String getShortName() {
		return "TAB";
	}

}
