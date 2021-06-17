package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generate SHA1 checksums for a set of files.
 * @author TBrady
 *
 */
class NameSha1Checksum extends NameChecksum {

	public NameSha1Checksum(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "Get SHA1 Checksum By Name";
	}
	
    public String getShortName(){return "SHA-1 ";}

    public MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
    	return MessageDigest.getInstance("SHA-1 ");
    }

	public String getDescription() {
		return "주어진 파일명으로 SHA1 체크섬을 보고합니다.\n주의, 파일이 두 번 이상 발견되면 체크섬을 덮어씁니다.";
	}

}
