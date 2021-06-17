package gov.nara.nwts.ftapp.gui;

/**
 * Helper class that reports on File Analyzer progress (on the Progress Tab)
 * @author TBrady
 *
 */
class MyProgress {
	int dircount;
	int lastdircount;
	int rptgap = 1;
	int rptgapmult = 5;
	int dirfound = 0;
	boolean processing;
	GuiFileTraversal gft;
	DirectoryTable dt;

	public MyProgress(GuiFileTraversal gft) {
		this.gft = gft;
		dt = gft.gftSW.dt;
	}
	
	public void resetDirCount() {
		dircount = 0;
		lastdircount = 0;
		rptgap = 1;
	}
	
	public void increment() {
		dircount++;
		testDirCount(false,processing ? "디렉토리가 처리됨" : "디렉토리가 발견됨");// directories processed	: directories found
	}

	public void testDirCount(boolean b, String note) {
		if (processing) {
			gft.gftSW.publish("");
		} else if (b || (dircount >= lastdircount + rptgap)) {
			gft.gftSW.publish(dircount + " dirs "+note);//dirs
			lastdircount = dircount;
			if (dircount > rptgap*rptgapmult*2) {
				rptgap *= rptgapmult;
				gft.gftSW.publish("... 보고 간격 "+ rptgap);//... Reporting every
			}
		}		
	}
	
	public void complete(boolean processed) {
		testDirCount(true, processed ? " 총 개수 디렉토리 처리됨" : " 총 개수 디렉토리 발견됨");//total directories processed" : " total directories found
		if (!processed){
		dt.progressPanel.progress.setMaximum(dircount);
		dirfound = dircount;
		resetDirCount();
		processing = !processed;
		}
	}
}