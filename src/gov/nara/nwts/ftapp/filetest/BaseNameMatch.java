package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.NameStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;
/**
 * Match files based on the base file name (without a file extension).
 * @author TBrady
 *
 */
class BaseNameMatch extends DefaultFileTest {

	public BaseNameMatch(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "기본 이름별 매치";
	}//Match By Base Name
	public String getKey(File f) {
		String s = f.getName();
		String[] sa = s.split("\\.");
		if (sa.length > 1) s = s.substring(0,s.length()-sa[sa.length-1].length()-1);
		return s; 
	}
	
    public String getShortName(){return "기본 파일이름";}//BASE NAME

	public Object fileTest(File f) {
		return null;
	}
    public Stats createStats(String key){ 
    	return new NameStats(key);
    }
    public Object[][] getStatsDetails() {
    	return NameStats.details;
    }
	public void initFilters() {
		initAllFilters();
	}

	public String getDescription() {
		return "파일의 디렉토리와 관계없이 기본 이름(확장자 없음)별 파일 크기에 대해 보고합니다.";
		//This test reports on file size by base name (no extension) regardless of the directory in a file name is found
	}

}
