package io.praecepta.dao.elastic.model.execution;

import java.util.Date;

public class PraeceptaProcessTracer {
	
	private Date startTime;
	
	private Date endTime;
	
	private String traceId;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	@Override
	public String toString() {
		return "PraeceptaProcessTracer [startTime=" + startTime + ", endTime=" + endTime + ", traceId=" + traceId + "]";
	}
	
}
