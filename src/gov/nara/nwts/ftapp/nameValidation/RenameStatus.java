package gov.nara.nwts.ftapp.nameValidation;

/**
 * Enumeration indicating all of the possible results of a file name/directory name validation test.
 * @author TBrady
 *
 */
public enum RenameStatus {
	NEXT("결론에 도달하지 않은 분석", RenamePassFail.PASS),//Test inconclusive
	VALID("원본 파일이름이 올바름으로 이름을 변경할 필요없습니다.", RenamePassFail.PASS),//Original filename is valid, rename not required
	INVALID_MANUAL("원본 파일이름을 메뉴얼대로 변경해야합니다",RenamePassFail.FAIL),//Original filename must be manually renamed
	PARSE_ERR("원본 파일이름을 구문 분석할 수 없습니다.",RenamePassFail.FAIL),//Original filename could not be parsed
	NEW_NAME_INVALID("파일 이름이 변경되지 않았습니다. 새 이름이 잘못되었습니다.",RenamePassFail.FAIL),//File not renamed, new name is invalid
	RENAMABLE("새 이름이 유효합니다.(이름변경 수행하지 않아도됨)", RenamePassFail.PASS),//New filename is valid (rename not performed)
	RENAMED("이름변경됨",RenamePassFail.PASS),//Renamed
	RENAME_FILE_EXISTS("이름변경 실패, 파일이 이미 존재합니다.", RenamePassFail.FAIL),//Rename failed, file already exists
	RENAME_FAILURE("이름변경 실패", RenamePassFail.FAIL),//Rename failed
	DIRECTORY("디렉토리: 분석되지 않음",RenamePassFail.PASS),//Directory: Not Tested
	DIRECTORY_VALID("유효한 디렉토리",RenamePassFail.PASS),//Valid Directory
	DIRECTORY_EMPTY("비어있는 디렉토리",RenamePassFail.FAIL),//Empty Directory
	DIRECTORY_CHILD_INVALID("디렉토리에 잘못된 파일이 존재합니다.",RenamePassFail.FAIL),//Directory contains Invalid File
	DIRECTORY_NAME_INVALID("디렉토리 이름이 잘못되었습니다.",RenamePassFail.FAIL),//Directory name is Invalid
	DIRECTORY_SEQUENCE_ERROR("디렉토리의 순서 오류",RenamePassFail.FAIL),//Sequence Error in Directory
	DIRECTORY_INCOMPLETE("불완전한 디렉토리",RenamePassFail.FAIL);//Incomplete Directory
	String message;
	RenamePassFail passfail;
	RenameStatus(String message, RenamePassFail passfail) {
		this.message = message;
		this.passfail = passfail;
	}
	public RenamePassFail getPassFail() {return passfail;}
	public String getMessage() {return message;}
};
