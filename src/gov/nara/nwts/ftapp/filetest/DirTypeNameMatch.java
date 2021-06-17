package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.DirTypeStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;

/**
 * Create FileAnalyzer statistics by directory and by FileType.
 * This routine is useful for understanding the contents of a large volume of material.
 * This routine was built as a sample rule for NWME.
 * @author TBrady
 *
 */
class DirTypeNameMatch extends DirMatch {

	public DirTypeNameMatch(FTDriver dt) {
		super(dt);
	}
	public String toString() {
		return "파일확장자와 디렉토리의 수";//Count By Type and Dir
	}
	public String getKey(File f) {
		return getKey(f, f.getParentFile());
	}
	
	public String getKey(File f, Object parentdir) {
		String key = getExt(f);
		if (parentdir instanceof File) {
			key = getExt(f)+": " + ((File)parentdir).getAbsolutePath().substring(getRoot().getAbsolutePath().length());
		}
		return key;		
	}
	
    public String getShortName(){return "파일확장자&디렉토리";}
    public Stats createStats(String key){ 
    	return new DirTypeStats(key);
    }
    public Object[][] getStatsDetails() {
    	return DirTypeStats.details;
    }
	public String getDescription() {
		return "디렉토리 내에서 특정 파일 확장자(타입)의 개수를 셉니다.  \n상위 디렉토리에 대해 누적 개수가 취합됩니다.";
	}

}
