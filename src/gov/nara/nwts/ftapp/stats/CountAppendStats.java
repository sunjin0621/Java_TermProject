package gov.nara.nwts.ftapp.stats;

import java.io.File;

import gov.nara.nwts.ftapp.filetest.FileTest;

/**
 * Stats class that accumulates counts as well as accumulates notes into a text buffer as processing continues.
 * When using a checksum algorithm to find duplicate values, this routine will provide details about instances in which more than one file share the same checksum.
 * @author TBrady
 *
 */
public class CountAppendStats extends CountStats {
	public static Object[][] details = {
			{String.class,"����Ȯ����",100},//Type
			{Long.class,"�� ����",100},//Count
			{String.class,"�ڼ��Ѻм�",2000},//Details
		};

	
	public CountAppendStats(String key) {
		super(key);
		vals.add("");
	}
	
	public Object compute(File f, FileTest fileTest) {
		vals.set(1, vals.get(1) + f.getAbsolutePath()+"; ");
		return super.compute(f, fileTest);
	}
}
