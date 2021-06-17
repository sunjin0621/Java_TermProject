package gov.nara.nwts.ftapp.nameValidation;

/**
 * Enumeration indicating all of the possible results of a file name/directory name validation test.
 * @author TBrady
 *
 */
public enum RenameStatus {
	NEXT("��п� �������� ���� �м�", RenamePassFail.PASS),//Test inconclusive
	VALID("���� �����̸��� �ùٸ����� �̸��� ������ �ʿ�����ϴ�.", RenamePassFail.PASS),//Original filename is valid, rename not required
	INVALID_MANUAL("���� �����̸��� �޴����� �����ؾ��մϴ�",RenamePassFail.FAIL),//Original filename must be manually renamed
	PARSE_ERR("���� �����̸��� ���� �м��� �� �����ϴ�.",RenamePassFail.FAIL),//Original filename could not be parsed
	NEW_NAME_INVALID("���� �̸��� ������� �ʾҽ��ϴ�. �� �̸��� �߸��Ǿ����ϴ�.",RenamePassFail.FAIL),//File not renamed, new name is invalid
	RENAMABLE("�� �̸��� ��ȿ�մϴ�.(�̸����� �������� �ʾƵ���)", RenamePassFail.PASS),//New filename is valid (rename not performed)
	RENAMED("�̸������",RenamePassFail.PASS),//Renamed
	RENAME_FILE_EXISTS("�̸����� ����, ������ �̹� �����մϴ�.", RenamePassFail.FAIL),//Rename failed, file already exists
	RENAME_FAILURE("�̸����� ����", RenamePassFail.FAIL),//Rename failed
	DIRECTORY("���丮: �м����� ����",RenamePassFail.PASS),//Directory: Not Tested
	DIRECTORY_VALID("��ȿ�� ���丮",RenamePassFail.PASS),//Valid Directory
	DIRECTORY_EMPTY("����ִ� ���丮",RenamePassFail.FAIL),//Empty Directory
	DIRECTORY_CHILD_INVALID("���丮�� �߸��� ������ �����մϴ�.",RenamePassFail.FAIL),//Directory contains Invalid File
	DIRECTORY_NAME_INVALID("���丮 �̸��� �߸��Ǿ����ϴ�.",RenamePassFail.FAIL),//Directory name is Invalid
	DIRECTORY_SEQUENCE_ERROR("���丮�� ���� ����",RenamePassFail.FAIL),//Sequence Error in Directory
	DIRECTORY_INCOMPLETE("�ҿ����� ���丮",RenamePassFail.FAIL);//Incomplete Directory
	String message;
	RenamePassFail passfail;
	RenameStatus(String message, RenamePassFail passfail) {
		this.message = message;
		this.passfail = passfail;
	}
	public RenamePassFail getPassFail() {return passfail;}
	public String getMessage() {return message;}
};
