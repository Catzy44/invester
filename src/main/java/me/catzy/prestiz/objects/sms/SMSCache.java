package me.catzy.prestiz.objects.sms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.catzy.prestiz.objects.uczestnicy.UczestnicyService;
import me.catzy.prestiz.objects.uczestnicy.Uczestnik;

@Transactional
@Data
public class SMSCache {
	private final int PART_SIZE = 25;
	UczestnicyService serviceUcz;
	SMSRepository repo;
	private List<SMSCEntry> cache;
	private int changeItterator;

	public List<SMSCEntry> getCache() {
		return this.cache;
	}

	public int getChangeItterator() {
		return this.changeItterator;
	}

	@Getter
	@Setter
	@JsonView({ SMSService.vSMSCEntry.class })
	static class SMSCEntry {
		private int changeItterator = 0;
		private SMS latestMessage = null;
		private int unreadMessagesCount = 0;
		private String number = null;
		private List<Uczestnik> attenders = null;
	}

	public SMSCache(UczestnicyService ucz, SMSRepository repo) {
		this.serviceUcz = null;
		this.repo = null;
		this.cache = new ArrayList<>();
		this.changeItterator = 0;
		this.serviceUcz = ucz;
		this.repo = repo;
	}

	private void sortCache() {
		synchronized (this.cache) {
			List<SMSCEntry> read = new ArrayList<>();
			List<SMSCEntry> unread = new ArrayList<>();
			for (SMSCEntry e : this.cache) {
				if (e.getUnreadMessagesCount() > 0) {
					unread.add(e);
					continue;
				}
				read.add(e);
			}
			Collections.sort(read, new Comparator<SMSCEntry>() {
				public int compare(SMSCache.SMSCEntry o1, SMSCache.SMSCEntry o2) {
					return o2.getLatestMessage().getCreatedTimestamp().compareTo(o1.getLatestMessage().getCreatedTimestamp());
				}
			});
			Collections.sort(unread, new Comparator<SMSCEntry>() {
				public int compare(SMSCache.SMSCEntry o1, SMSCache.SMSCEntry o2) {
					return o2.getLatestMessage().getCreatedTimestamp().compareTo(o1.getLatestMessage().getCreatedTimestamp());
				}
			});
			this.cache.clear();
			this.cache.addAll(unread);
			this.cache.addAll(read);
		}
	}

	public List<SMSCEntry> getCachedSMSNewsPart(int startingPartNum, int partsCount) {
		List<SMSCEntry> part = new ArrayList<>();
		for (int i = startingPartNum * 25, startingPoint = i; i < startingPoint + 25 * partsCount; i++) {
			SMSCEntry sms = this.cache.get(i);
			sms.setAttenders(this.serviceUcz.getUczestnicyByNumberCached(sms.getNumber()));
			part.add(sms);
		}
		return part;
	}

	public SMSCEntry getByNumber(String number) {
		SMSCEntry cachedMessage = null;
		for (int i = 0; i < this.cache.size(); i++) {
			SMSCEntry cachedMessageX = this.cache.get(i);
			if (cachedMessageX.getNumber().equals(number)) {
				cachedMessage = cachedMessageX;
				break;
			}
		}
		return cachedMessage;
	}

	public void messageChangedEvent(SMS newm) {
		messageChangedEvent(null, newm);
	}

	public void messageChangedEvent(SMS oldm, SMS newm) {
		boolean objectNOTAlreadyExisting = (newm.getId() == null);// in DB -> thats something new!
		String number = newm.getNumber();
		SMSCEntry e = getByNumber(number);// find entry in cache by mesage sender phone number

		synchronized (this.cache) {
			if (e == null) {// no entry in cache, create new
				e = new SMSCEntry();
				// e.setNumber(number);
				e.setLatestMessage(newm);
				if (newm.getType() == SMS.Type.IN)
					e.setUnreadMessagesCount(e.getUnreadMessagesCount() + 1);

			} else if (objectNOTAlreadyExisting) {//new received sms processed here
				e.setLatestMessage(newm);
				if (newm.getType() == 1)
					e.setUnreadMessagesCount(e.getUnreadMessagesCount() + 1);

			} else if (newm.getType() == SMS.Type.IN && newm.getReadStatus() != oldm.getReadStatus()) {
				if (newm.getReadStatus() == 1) {
					e.setUnreadMessagesCount(e.getUnreadMessagesCount() - 1);
				} else {
					e.setUnreadMessagesCount(e.getUnreadMessagesCount() + 1);
				}

			}
			e.setChangeItterator(e.getChangeItterator() + 1);
			this.changeItterator++;
		}
		sortCache();
	}

	public void preloadCache() {
		List<SMS> xsmsNewsCache = this.repo.getSMSNews();
		for (SMS s : xsmsNewsCache) {
			SMSCEntry e = new SMSCEntry();
			e.setNumber(s.getNumber());
			e.setLatestMessage(s);
			e.setUnreadMessagesCount((s.getUnreadSMSCountFromThisNumber() != null) ? s.getUnreadSMSCountFromThisNumber().intValue() : 0);
			this.cache.add(e);
		}
		sortCache();
	}

	public void markAllRead(String number) {
		SMSCEntry e = getByNumber(number);
		synchronized (this.cache) {
			e.setChangeItterator(e.getChangeItterator() + 1);
			e.setUnreadMessagesCount(0);
			e.getLatestMessage().setReadStatus(1);
		}
		sortCache();
		this.changeItterator++;
	}
}