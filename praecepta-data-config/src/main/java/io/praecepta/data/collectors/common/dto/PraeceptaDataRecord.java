package io.praecepta.data.collectors.common.dto;

import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 
 * @author rajasrikar
 *
 */

public class PraeceptaDataRecord {

	private LinkedBlockingDeque<RecordEntry> entries;
	
	public PraeceptaDataRecord(int recordCount) {
		entries = new LinkedBlockingDeque<>(recordCount);
	}
	
	public final void addRecordEntry(RecordEntry recordEntry) {
		entries.add(recordEntry);
	}

	public final void addRecordEntry(String messageText, String messageTextId, Map<String, Object> metaData) {
		addRecordEntry(new RecordEntry(messageText, messageTextId, metaData));
	}
	
	public final LinkedBlockingDeque<RecordEntry> getRecordEntries(){
		return entries;
	}
	
	public static class RecordEntry{
		
		private String messageText;
		
		private Map<String, Object> metaData;
		
		private String messageTextId;
		
		public RecordEntry(String messageText) {
			this(messageText, null);
		}

		public RecordEntry(String messageText, String messageTextId) {
			this(messageText, messageTextId, null);
		}
		
		public RecordEntry(String messageText, String messageTextId, Map<String, Object> metaData) {
			this.messageText = messageText;
			this.messageTextId = messageTextId;
			this.metaData = metaData;
		}
		
		public String getMessageText() {
			return messageText;
		}

		public void setMessageText(String messageText) {
			this.messageText = messageText;
		}

		public Map<String, Object> getMetaData() {
			return metaData;
		}

		public void setMetaData(Map<String, Object> metaData) {
			this.metaData = metaData;
		}

		public String getMessageTextId() {
			return messageTextId;
		}

		public void setMessageTextId(String messageTextId) {
			this.messageTextId = messageTextId;
		}

		@Override
		public String toString() {
			return "RecordEntry [messageText=" + messageText + ", metaData=" + metaData + ", messageTextId="
					+ messageTextId + "]";
		}
		
	}
}
