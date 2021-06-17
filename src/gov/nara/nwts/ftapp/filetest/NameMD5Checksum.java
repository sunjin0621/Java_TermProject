package gov.nara.nwts.ftapp.filetest;

import gov.nara.nwts.ftapp.FTDriver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generate MD5 checksums for a set of files.
 * @author TBrady
 *
 */
class NameMD5Checksum extends NameChecksum {

	public NameMD5Checksum(FTDriver dt) {
		super(dt);
	}

	public String toString() {
		return "Get MD5 Checksum By Name";
	}
    public String getShortName(){return "MD5 ";}

    public MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
    	return MessageDigest.getInstance("MD5");
    }

	public String getDescription() {
		return "�־��� ���ϸ� ���� MD5 üũ���� �����մϴ�.\n����, ������ �� �� �̻� �߰ߵǸ� üũ���� ����ϴ�.";
	}
}
