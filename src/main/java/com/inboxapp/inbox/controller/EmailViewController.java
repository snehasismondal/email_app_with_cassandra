package com.inboxapp.inbox.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.inboxapp.inbox.email.Email;
import com.inboxapp.inbox.email.EmailRepository;
import com.inboxapp.inbox.email.EmailService;
import com.inboxapp.inbox.emaillist.EmailListItem;
import com.inboxapp.inbox.emaillist.EmailListItemKey;
import com.inboxapp.inbox.emaillist.EmailListItemRepository;
import com.inboxapp.inbox.folders.Folder;
import com.inboxapp.inbox.folders.FolderRepository;
import com.inboxapp.inbox.folders.FolderService;
import com.inboxapp.inbox.folders.UnreadEmailStatsRepository;

@Controller
public class EmailViewController {
	@Autowired
	FolderRepository folderRepo;
	
	@Autowired
	FolderService folderSer;
	
	@Autowired
	EmailService emailSer;
	
	@Autowired
	EmailRepository eRepo;
	@Autowired
	private EmailListItemRepository emailLRepo;
	@Autowired
	private UnreadEmailStatsRepository unreadRepo;
	
	@GetMapping(value="/email/{id}")
	public String emailView(@AuthenticationPrincipal OAuth2User principal, @RequestParam(required = false) String folder, @PathVariable UUID id,Model model) {
		
		
		if(principal==null || !StringUtils.hasText( principal.getAttribute("login"))) {
		return "index";
		}
		//Fetch Folders
		String userId=principal.getAttribute("login");
		List<Folder> findAllById=folderRepo.findAllById(userId);
		model.addAttribute("userFolders",findAllById);
		
		List<Folder> defaultFolders=folderSer.fetchDefaultFolders(userId);
		model.addAttribute("defaultFolders",defaultFolders);
		

		
		Optional<Email> opEmail=eRepo.findById(id);
		System.out.println("opEmail :"+opEmail.get().getSubject());
		if(opEmail.isEmpty()) {
			return "inbox-page";
		}
		
		
		Email email=opEmail.get();
		String toId= String.join(",", email.getTo());
		model.addAttribute("email",opEmail.get());
		model.addAttribute("emailToId",toId);
		
		EmailListItemKey key= new EmailListItemKey();
		key.setId(userId);
		key.setLabel(folder==null?"Inbox":folder);
		key.setTimeUUID(email.getId());
		Optional<EmailListItem> optionalEmailListItem = emailLRepo.findById(key);
		if(optionalEmailListItem.isPresent()) {
			EmailListItem emailListItem= optionalEmailListItem.get();
			if(emailListItem.isUnread()) {
				emailListItem.setUnread(false);
				emailLRepo.save(emailListItem);
				unreadRepo.decreamentUnreadCount(userId, folder==null?"Inbox":folder);
			}
		}
		
		model.addAttribute("stats",folderSer.mapCount(userId));

		return "compose-page";
	}
	
	
	
}
