package com.inboxapp.inbox.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inboxapp.inbox.emaillist.EmailListItemRepository;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.inboxapp.inbox.emaillist.EmailListItem;
import com.inboxapp.inbox.folders.Folder;
import com.inboxapp.inbox.folders.FolderRepository;
import com.inboxapp.inbox.folders.FolderService;
import com.inboxapp.inbox.folders.UnreadEmailStats;
import com.inboxapp.inbox.folders.UnreadEmailStatsRepository;

@Controller
public class InboxController {
	@Autowired
	FolderRepository folderRepo;
	@Autowired
	FolderService folderSer;
	
	@Autowired
	EmailListItemRepository emailRepo;
	
	@Autowired
	UnreadEmailStatsRepository unreadRepo;
	
	@GetMapping(value="/")
	public String homePage(@AuthenticationPrincipal OAuth2User principal,@RequestParam(required=false) String folder,Model model) {
		if(principal==null || !StringUtils.hasText( principal.getAttribute("login"))) {
		return "index";
		}
		//Fetch Folders
		String userId=principal.getAttribute("login");
		List<Folder> findAllById=folderRepo.findAllById(userId);
		model.addAttribute("userFolders",findAllById);
		
		List<Folder> defaultFolders=folderSer.fetchDefaultFolders(userId);
		model.addAttribute("defaultFolders",defaultFolders);
		
		model.addAttribute("stats",folderSer.mapCount(userId));
		//Fetch messages
		
		if(!StringUtils.hasText(folder)) {
			folder="Inbox";
		}
		String folderlabel ="Inbox";
		List<EmailListItem> emailList=emailRepo.findAllByKey_IdAndKey_Label(userId, folder);
		
		PrettyTime p =new PrettyTime();
		emailList.stream().forEach(emailItem->
		{ 
			UUID timeUuid= emailItem.getKey().getTimeUUID();
			Date date=new Date(Uuids.unixTimestamp(timeUuid));
		//p.format(date);
		emailItem.setAgoTimeString(p.format(date));
		});
		System.out.println("emailList"+emailList);
		model.addAttribute("emailList",emailList);
		model.addAttribute("folderName",folder);
		
		return "inbox-page";
	}
	

}
