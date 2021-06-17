package gov.nara.nwts.ftapp.stats;

import gov.nara.nwts.ftapp.filetest.FileTest;

import java.io.File;

/**
 * Stats object showing file counts and file sizes.
 * @author TBrady
 *
 */
public class FileCountStats extends CountStats {
	public static Object[][] details = {
			{String.class,"������/Ȯ���ڸ�",100},//Type
			{Long.class,"�� ����",100},//Count
			{Long.class,"���� ũ��",100},//Size
		};

	
	public FileCountStats(String key) {
		super(key);
		vals.add(new Long(0));
	}
	
	public Object compute(File f, FileTest fileTest) {
		Object ret = super.compute(f, fileTest);
		Long bytes = (Long)vals.get(1);
		vals.set(1, bytes.longValue()+f.length());
		return ret;
	}


}
