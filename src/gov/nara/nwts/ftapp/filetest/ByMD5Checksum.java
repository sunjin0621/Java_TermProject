package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import gov.nara.nwts.ftapp.stats.CountAppendStats;
import gov.nara.nwts.ftapp.stats.Stats;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * List files by MD5 checksum; this can be used to identify potentially duplicate digital objects.
 * @author TBrady
 *
 */
class ByMD5Checksum extends NameChecksum {

	public ByMD5Checksum(FTDriver dt) {
		super(dt);
	}
	public String getKey(File f) {
		return getChecksum(f);
	}
	public Object fileTest(File f) {
		return f.getAbsolutePath();
	}
    public Stats createStats(String key){ 
    	return new CountAppendStats(key);
    }
    public Object[][] getStatsDetails() {
    	return CountAppendStats.details;
    }

	public String toString() {
		return "MD5 체크섬별 나열";
	}
    public String getShortName(){return "By MD5 ";}

    public MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
    	return MessageDigest.getInstance("MD5 ");
    }

	public String getDescription() {
		return "MD5 체크섬별로 파일을 나열합니다.\n주의, 파일이 두 번 이상 발견되면 체크섬을 덮어씁니다.";
	}
}
