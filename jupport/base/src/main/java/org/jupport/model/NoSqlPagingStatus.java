package org.jupport.model;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.web.bind.ServletRequestUtils;


public class NoSqlPagingStatus {

	private Logger logger = Logger.getLogger(NoSqlPagingStatus.class);
	
	protected int pageSize = 100;//for test
	protected long startUid = 0;
	protected String startUids;
	protected boolean hasNext = false;
	protected boolean reversed = false;
	protected int pageIndex;
	
	
	public int getPageIndex() {
		return pageIndex;
	}

	public void getValuse(HttpServletRequest request)
	{
		this.pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", pageSize);
		this.startUid = ServletRequestUtils.getLongParameter(request, "startUid", 0);
		this.startUids = ServletRequestUtils.getStringParameter(request, "startUids", "");
		this.reversed = ServletRequestUtils.getBooleanParameter(request, "reversed", reversed);
		
		if(this.startUids.equals("") )
		{
			this.startUids += this.startUid;
		}
		else if(!this.startUids.contains("|"+this.startUid))
		{
			this.startUids += "|"+this.startUid;
		}
		
		String sStartUids[] = this.startUids.split("\\|");
		for (int i = 0; i < sStartUids.length; i++) {
			if(this.startUid == Integer.valueOf(sStartUids[i]))
			{
				pageIndex = i;
				if(this.reversed)
				{
					this.startUid = Integer.valueOf(sStartUids[i==0?0:(i-1)]);
				}
				break;
			}
		}
		
		logger.info("this.startUid " + this.startUid + " reversed " + this.reversed);
	}
	
	/*
	 * Properties
	 */
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public long getStartUid() {
		return startUid;
	}
	public void setStartUid(long startUid) {
		this.startUid = startUid;
	}
	public boolean isHasNext() {
		return hasNext;
	}
	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	public String getStartUids() {
		return startUids;
	}
	public void setStartUids(String startUids) {
		this.startUids = startUids;
	}
	
	
	
}
