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
		return "��� �м���� ����ϱ�";//All test Function use
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
	
    public String getShortName(){return "����ɼ���";}
    public Stats createStats(String key){ 
    	return new Allfunctions(key);
    }
    public Object[][] getStatsDetails() {
    	return Allfunctions.details;
    }
	public String getDescription() {
		return "������ ���丮�� ��� ���(��ȣȭ����)�� �̿��Ͽ� �м����մϴ�.  \n�м��� ���� �ð��� �Ҹ�� �� �ֽ��ϴ�.";
	}

}

