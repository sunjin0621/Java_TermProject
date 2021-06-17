package gov.nara.nwts.ftapp.stats;

import gov.nara.nwts.ftapp.filetest.FileTest;
import gov.nara.nwts.ftapp.nameValidation.RenameDetails;
import gov.nara.nwts.ftapp.nameValidation.RenameStatus;
import gov.nara.nwts.ftapp.nameValidation.RenamePassFail;

import java.io.File;

/**
 * Stats object displaying the results of a filename test.
 * @linkplain gov.nara.nwts.ftapp.filetest.NameValidationTest} contains the base logic that makes use of this Stats object.
 * @author TBrady
 *
 */
public class NameValidationStats extends Stats {

	public static Object[][] details = {
		{String.class,"경로",450},//Path
		{String.class,"Pass/Fail",50,RenamePassFail.values()},
		{String.class,"상태",150,RenameStatus.values()},//Status
		{String.class,"메세지",250},//Message
		{Object.class,"추천 경로",450},//Recommended Path
	};

	
	public NameValidationStats(String key) {
		super(key);
		vals.add("");
		vals.add("");
		vals.add("");
		vals.add("");
	}
	
	public Object compute(File f, FileTest fileTest) {
		Object ret = fileTest.fileTest(f);
		if (ret instanceof RenameDetails) {
			RenameDetails rdet = (RenameDetails)ret;
			vals.set(0, rdet.getPassFail());
			vals.set(1, rdet.getRenameStatus());
			vals.set(2, rdet.getMessage());
			vals.set(3, rdet.getDetailNote(fileTest.getRoot()));
		}
		return ret;
	}

	
}
