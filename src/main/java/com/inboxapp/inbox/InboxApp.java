package com.inboxapp.inbox;

import java.nio.file.Path;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.inboxapp.inbox.emaillist.EmailListItemRepository;
import com.inboxapp.inbox.email.Email;
import com.inboxapp.inbox.email.EmailRepository;
import com.inboxapp.inbox.email.EmailService;
import com.inboxapp.inbox.emaillist.EmailListItem;
import com.inboxapp.inbox.emaillist.EmailListItemKey;
import com.inboxapp.inbox.folders.Folder;
import com.inboxapp.inbox.folders.FolderRepository;
import com.inboxapp.inbox.folders.UnreadEmailStatsRepository;


@SpringBootApplication
@RestController
@EnableCassandraRepositories
public class InboxApp {
	@Autowired
	FolderRepository folderRepo;
	/*
	 * @Autowired EmailListItemRepository emailRepo;
	 * 
	 * @Autowired EmailRepository eRepo;
	 * 
	 * @Autowired UnreadEmailStatsRepository unreadRepo;
	 */

@Autowired
EmailService emailService;

public static void main(String[] args) {
		SpringApplication.run(InboxApp.class, args);
	}

	/*@RequestMapping("/user")
	public String user(@AuthenticationPrincipal OAuth2User principal) {
		System.out.println(principal);
		return principal.getAttribute("name");
	}*/
	
	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProp) {
		Path bundle= astraProp.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}
	
	@PostConstruct
	public void init() {
		
		folderRepo.save(new Folder("snehasismondal","Home","blue"));
		folderRepo.save(new Folder("snehasismondal","Work","green"));
		folderRepo.save(new Folder("snehasismondal","Mahh","yellow"));


		
		for (int i=0;i<10;i++) {
			/*
			 * EmailListItemKey key= new EmailListItemKey(); key.setId("snehasismondal");
			 * key.setLabel("Inbox"); key.setTimeUUID(Uuids.timeBased());
			 * 
			 * EmailListItem item =new EmailListItem(); item.setKey(key);
			 * item.setTo(Arrays.asList("snehasismondal")); item.setSubject("Subejct "+i);
			 * item.setUnread(true);
			 * 
			 * emailRepo.save(item);
			 * 
			 * Email email=new Email();
			 * 
			 * email.setId(key.getTimeUUID()); email.setFrom("snehasis");
			 * email.setSubject(item.getSubject()); email.setBody("Body :"+i);
			 * email.setTo(item.getTo());
			 * 
			 * eRepo.save(email);
			 */
			emailService.sendEmail("snehasismondal", Arrays.asList("snehasismondal","abc"), "Hello"+i, "boddy");
		}
	}
	

}
