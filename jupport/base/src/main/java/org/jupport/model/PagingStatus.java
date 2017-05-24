package org.jupport.model;

import org.springframework.core.style.ToStringCreator;

public class PagingStatus {

	public PagingStatus() 
	{
		super();
	}

	public void config(int entryCount, int pageSize, int pageNumber)
	{
		setEntryCount(entryCount);
		setPageSize(pageSize);
		setPageNumber(pageNumber);
		updatePageCount();
	}

	// how many entrys in one page

	private int pageSize;

	// current page number
	private int pageNumber;

	// how many pages
	private int pageCount;

	// entry sum
	private int entryCount;

	private int defaultPageSize = 20;

	/*
	 * FirstResult
	 */
	public int getFirstResult() {
		return this.pageNumber==0?0:(this.pageNumber - 1) * this.pageSize;
	}

	public int getMaxResults() {
		return this.pageSize;
	}

	/*
	 * JavaBean Properties
	 */
	public int getEntryCount() {
		return entryCount;
	}

	public void setEntryCount(int entryCount) {
		this.entryCount = entryCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize > 0 ? pageSize : defaultPageSize;
	}
	
	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public void updatePageCount() 
	{		
		if(this.entryCount>0)
			this.pageSize = pageSize > this.entryCount ? this.entryCount : pageSize;	
		
		setPageCount((this.entryCount / this.pageSize)
				+ (this.entryCount % this.pageSize == 0 ? 0 : 1));
		
		this.pageNumber = this.pageNumber<=0?1:this.pageNumber;
		this.pageNumber = this.pageNumber>this.pageCount?this.pageCount:this.pageNumber;
	}

	public int getPageNumber() 
	{
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) 
	{
		this.pageNumber = pageNumber;				
	}
	
    @Override
    public String toString() {
        return new ToStringCreator(this)
            .append("pageNumber", pageNumber)
            .append("pageSize", pageSize)
            .append("pageCount", pageCount)
            .append("entryCount", entryCount)
            .toString();
    }

}
