package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.DirStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;

/**
 * Create FileAnalyzer statistics by directory.
 * @author TBrady
 *
 */
class DirMatch extends DefaultFileTest {

	public DirMatch(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "경로별 총계";//Match By Path
	}
	public String getKey(File f) {
		return getKey(f, f.getParentFile());
	}
	
	public String getKey(File f, Object parentdir) {
		String key = "";
		if (parentdir instanceof File) {
			key = ((File)parentdir).getAbsolutePath().substring(getRoot().getAbsolutePath().length());
		}
		return key;		
	}
	
    public String getShortName(){return "경로";}

	public Object fileTest(File f) {
		return null;
	}
    public Stats createStats(String key){ 
    	return new DirStats(key);
    }
    public Object[][] getStatsDetails() {
    	return DirStats.details;
    }
	public void initFilters() {
		initAllFilters();
	}

	public String getDescription() {
		return "특정 폴더 안에 있는 항목의 총계가 계산됩니다.  \n또한, 스캔된 각 폴더에 대해 발견된 누적 총계를 계산해줄 것 입니다.";
	}

}
