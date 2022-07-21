package com.inboxapp.inbox.email;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.inboxapp.inbox.emaillist.EmailListItem;
import com.inboxapp.inbox.emaillist.EmailListItemKey;
import com.inboxapp.inbox.emaillist.EmailListItemRepository;
import com.inboxapp.inbox.folders.UnreadEmailStatsRepository;

@Service
public class EmailService {

	@Autowired
	private EmailRepository emailRepo;
	@Autowired
	private EmailListItemRepository emailLRepo;
	@Autowired
	private UnreadEmailStatsRepository unreadRepo;
	
	public void sendEmail(String from,List<String> to,String subject,String body) {
		Email email= new Email();
		email.setTo(to);
		email.setBody(body);
		email.setFrom(from);
		email.setSubject(subject);
		email.setId(Uuids.timeBased());
	    emailRepo.save(email);
	    to.forEach(toId-> {
	    	String folder="Inbox";
	    	EmailListItem item = createEmailListItem(to, subject, email, toId,folder);
	    emailLRepo.save(item);
	    unreadRepo.increamentUnreadCount(toId, "Inbox");
	    });
	
	
	EmailListItem sentItemsEntry=createEmailListItem(to,subject,email,from,"Sent");
	sentItemsEntry.setUnread(false);
	emailLRepo.save(sentItemsEntry);
	
	}
	private EmailListItem createEmailListItem(List<String> to, String subject, Email email, String itemOwner,String folder) {
		EmailListItemKey key= new EmailListItemKey();
		key.setId(itemOwner);
		key.setLabel(folder);
		key.setTimeUUID(email.getId());
		
		
		EmailListItem item= new EmailListItem();
		item.setKey(key);
		item.setTo(to);
		item.setSubject(subject);
		item.setUnread(true);
		
		return item;
	}
}

