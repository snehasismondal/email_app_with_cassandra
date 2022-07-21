package com.inboxapp.inbox.folders;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value="folders_by_user")
public class Folder {
	
	public String getid() {
		return id;
	} 

	public void setid(String id) {
		this.id = id;
	}

	public Folder() {
		
	}

	public Folder(String id, String label, String color) {
		super();
		this.id = id;
		this.label = label;
		this.color = color;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@PrimaryKeyColumn(name="user_id",ordinal=0,type=PrimaryKeyType.PARTITIONED)
	@CassandraType(type=CassandraType.Name.TEXT)
	private String id;
	
	@PrimaryKeyColumn(name="label",ordinal=1,type=PrimaryKeyType.CLUSTERED)
	@CassandraType(type=CassandraType.Name.TEXT)
	private String label;
	
	@CassandraType(type=CassandraType.Name.TEXT)
	private String color;
	
}
