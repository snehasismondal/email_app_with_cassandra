package com.inboxapp.inbox.folders;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value="unread_email_stats")
public class UnreadEmailStats {

	public UnreadEmailStats(String id, String label, int unreadCount) {
		super();
		this.id = id;
		this.label = label;
		this.unreadCount = unreadCount;
	}
	
	

	public UnreadEmailStats() {
		// TODO Auto-generated constructor stub
	}



	@PrimaryKeyColumn(name="user_id",ordinal=0,type=PrimaryKeyType.PARTITIONED)
	@CassandraType(type=CassandraType.Name.TEXT)
	private String id;
	
	@PrimaryKeyColumn(name="label",ordinal=1,type=PrimaryKeyType.CLUSTERED)
	@CassandraType(type=CassandraType.Name.TEXT)
	private String label;
	
	@CassandraType(type=CassandraType.Name.COUNTER)
	private int unreadCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}
	
	
	
}
