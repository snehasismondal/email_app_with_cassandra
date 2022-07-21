package com.inboxapp.inbox.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.inboxapp.inbox.email.EmailService;
import com.inboxapp.inbox.emaillist.EmailListItemRepository;
import com.inboxapp.inbox.folders.Folder;
import com.inboxapp.inbox.folders.FolderRepository;
import com.inboxapp.inbox.folders.FolderService;

@Controller
public class ComposeController {

	@Autowired
	FolderRepository folderRepo;
	@Autowired
	FolderService folderSer;
	
	@Autowired
	EmailListItemRepository emailRepo;
	
	@Autowired
	EmailService emailSer;
	
	
	@GetMapping("/compose")
	public String getComposePage(@AuthenticationPrincipal OAuth2User principal, Model model,
			@RequestParam(required = false) String to
			) {
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

				
				if(StringUtils.hasText(to))
				{
				List<String> uniqueToIds = splitId(to);
				model.addAttribute("to",String.join(",",uniqueToIds));
				}
				return "email-page";
	}
	
	
	private List<String> splitId(String to) {
		if(!StringUtils.hasText(to)) {
			return new ArrayList<String>();
		}
		String splitIds[]=to.split(",");
		List<String> uniqueToIds=Arrays.asList(splitIds).stream().map(id->StringUtils.trimWhitespace(id))
		.filter(id->StringUtils.hasText(id)).distinct().collect(Collectors.toList());
		return uniqueToIds;
	}
	
	@PostMapping(value="/sendEmail")
	 public ModelAndView sendEmail(@RequestBody MultiValueMap<String,String> formData, @AuthenticationPrincipal OAuth2User principal) {
		 if(principal==null || !StringUtils.hasText( principal.getAttribute("login"))) {
				return new ModelAndView("redirect:/");
				}
		 
		 String from= principal.getAttribute("login");
		List< String> toIds=splitId(formData.getFirst("toIds"));
		 String subject=formData.getFirst("subject");	
		 String body=formData.getFirst("body");
		 emailSer.sendEmail(from, toIds, subject, body);
		 
			return new ModelAndView("redirect:/");

		 
	 }
	
}
