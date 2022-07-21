package com.inboxapp.inbox.folders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderService {
	
	@Autowired 
	UnreadEmailStatsRepository unreadRepo;
	public List<Folder> fetchDefaultFolders(String userId){
		return Arrays.asList(
				
				new Folder(userId,"Inbox","blue"),
				new Folder(userId,"Sent","green"),				
				new Folder(userId,"Important","red"));
	}
	
	public Map<String,Integer> mapCount(String userId){
		List<UnreadEmailStats> stats= unreadRepo.findAllById(userId);
		return stats.stream().collect(Collectors.toMap(UnreadEmailStats::getLabel, UnreadEmailStats::getUnreadCount));
		
	}

}
