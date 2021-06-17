package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.FileCountStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;
/**
 * Count items by file extension; this is the most basic and easy to understand File Analyzer rule.
 * @author TBrady
 *
 */
class CountByType extends DefaultFileTest {

	public CountByType(FTDriver dt) {
		super(dt);
	}

	public Object fileTest(File f) {
		return null;
	}

	public String toString() {
		return "확장자 별 파일 수";//Count Files By Type
	}

    public Stats createStats(String key){ 
    	return new FileCountStats(key);
    }
    public Object[][] getStatsDetails() {
    	return FileCountStats.details;
    }
    public String getShortName(){return "파일확장자";}
	public void initFilters() {
		initAllFilters();
	}

	public String getDescription() {
		return "파일 확장명마다 발견된 파일 개수를 계산합니다.";
	}
}
