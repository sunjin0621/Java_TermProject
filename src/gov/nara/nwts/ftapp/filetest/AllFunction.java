package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.DirTypeStats;
import gov.nara.nwts.ftapp.stats.NameStats;
import gov.nara.nwts.ftapp.stats.FileCountStats;
import gov.nara.nwts.ftapp.stats.DirStats;

import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;

import gov.nara.nwts.ftapp.stats.Allfunctions;

/**
 *
 * @author 
 *
 */
class AllFunction extends DirMatch{ //, DefaultFileTest{

	public AllFunction(FTDriver dt) {
		super(dt);
	}
	public String toString() {
		return "모든 분석기능 사용하기";//All test Function use
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
	
    public String getShortName(){return "모든기능수행";}
    public Stats createStats(String key){ 
    	return new Allfunctions(key);
    }
    public Object[][] getStatsDetails() {
    	return Allfunctions.details;
    }
	public String getDescription() {
		return "선택한 디렉토리에 모든 기능(암호화제외)을 이용하여 분석을합니다.  \n분석에 많은 시간이 소모될 수 있습니다.";
	}

}

