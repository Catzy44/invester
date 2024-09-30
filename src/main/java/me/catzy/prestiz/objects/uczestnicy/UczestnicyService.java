package me.catzy.prestiz.objects.uczestnicy;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.Data;
import me.catzy.prestiz.objects.attendersGroupChanges.AttenderGroupChange;
import me.catzy.prestiz.objects.grupy.Grupa;
import me.catzy.prestiz.objects.miasta.closedMonths.ClosedMonth;
import me.catzy.prestiz.objects.miasta.closedMonths.ClosedMonthService;
import me.catzy.prestiz.objects.miejsca.Miejsce;
import me.catzy.prestiz.objects.miejsca.Miejsce.PlaceType;
import me.catzy.prestiz.objects.platnosci.PaymentsService;
import me.catzy.prestiz.objects.seasons.Season;
import me.catzy.prestiz.objects.sms.SMSRepository;
import me.catzy.prestiz.objects.zajecia.Zajecia;
import me.catzy.prestiz.objects.zajecia.ZajeciaService;
import me.catzy.prestiz.objects.zajecia.presences.Presence;
import me.catzy.prestiz.objects.zajecia.presences.PresenceService;

@Service
@Transactional
public class UczestnicyService {
  @Autowired UczestnicyRepository repo;
  @Autowired ZajeciaService serviceClasses;
  @Autowired PaymentsService servicePayments;
  @Autowired PresenceService servicePresence;
  @Autowired ClosedMonthService serviceClosedMonth;
  @Autowired SMSRepository repoSMS;
  @Autowired CacheManager cacheManager;
  
  public Uczestnik byIdLazy(int id) {
    return this.repo.getOne(id);
  }
  
  public Uczestnik byId(int id) {
    return this.repo.findById(id);
  }
  
  @CacheEvict({"uczestnicyByNr"})
  public Uczestnik save(Uczestnik ucz) {
    fixPNumbner(ucz);
    return (Uczestnik)this.repo.saveAndFlush(ucz);
  }
  
  public void fixPNumbner(Uczestnik ucz) {
    String pnStr = ucz.getTelefonOpiekuna();
    if (pnStr.length() != 12) {
      PhoneNumber pn = new PhoneNumber(pnStr);
      if (pn.isValid())
        ucz.setTelefonOpiekuna(pn.getNumberFixed()); 
    } 
  }
  
  public Uczestnik byNumber(PhoneNumber pn) {
    Uczestnik u = this.repo.byPhoneNumberExact(pn.getNumberFixed());
    return u;
  }
  
  public List<Uczestnik> getByIdsIn(int[] id) {
    return this.repo.getByIdIn(id);
  }
  
  public List<Uczestnik> getChetni(int miejsce) {
    return this.repo.getChetni(miejsce);
  }
  
  @Cacheable(value = {"uczestnicyByNr"}, key = "#number")
  public List<Uczestnik> getUczestnicyByNumberCached(String number) {
    PhoneNumber pn = new PhoneNumber(number);
    if (!pn.isValid())
      return new ArrayList<>(); 
    return this.repo.getUczestnicyByPn(number);
  }
  
  public Uczestnik copyUczestnikTo(Uczestnik u, Miejsce m, Grupa g) {
    Uczestnik n = u.copySomeOfDataIdk();
    n.setPlace(m);
    n.setGroup(g);
    n.setTimestamp(new Timestamp(System.currentTimeMillis()));
    return save(n);
  }
  
  public List<Uczestnik> findByString(String name) {
    if (name.length() < 3)
      return null; 
    return this.repo.findUczestnikByString(name, (Pageable)PageRequest.of(0, 10));
  }
  
  public float sumPriceForClasses(Uczestnik u, int monthNum) {
    List<Zajecia> classesInMonth = new ArrayList<>();
    for (AttenderGroupChange c : u.getGroupChanges()) {
      for (Zajecia z : this.serviceClasses.getClassesByAGCCache(c)) {
        LocalDate data = z.getDate();
        int month = data.get(ChronoField.MONTH_OF_YEAR);
        if (month == monthNum)
          classesInMonth.add(z); 
      } 
    } 
    float rabatPrc = u.getRabatPrc();
    float priceForThisMonth = 0.0F;
    boolean individualPayment = u.isIndividualPayment();
    for (Zajecia z2 : classesInMonth) {
      if (individualPayment) {
        Presence p = this.servicePresence.findByClassAndAttender(u, z2);
        if (p.getPresence() != 1)
          continue; 
      } 
      float priceForOneClassInThisGr = z2.getGroup().getPriceForOneClass();
      priceForThisMonth += priceForOneClassInThisGr * (1.0F - rabatPrc / 100.0F);
    } 
    return priceForThisMonth;
  }
  
  public Map<Integer, PaymentStatus> getPaymentStatuses(Uczestnik u) {
    return getPaymentStatuses(u, false);
  }
  
  public Map<Integer, PaymentStatus> getPaymentStatuses(Uczestnik u, boolean fresh) {
    int type = u.getPlace().getType();
    if (type == 0)
      return getPaymentStatusesKids(u, fresh); 
    if (type == 2)
      return getPaymentStatusesCyclic(u, getCycles(u), fresh); 
    return null;
  }
  
  public float getSumToPay(Uczestnik u) {
    int type = u.getPlace().getType();
    LocalDate ld = LocalDate.now();
    if (type == PlaceType.KIDS) {
      PaymentStatus ps;
      Map<Integer, PaymentStatus> statuses = getPaymentStatusesKids(u, false);
      if (ld.getDayOfMonth() > 15) {
        ps = statuses.get(Integer.valueOf(ld.get(ChronoField.MONTH_OF_YEAR) + 1));
      } else {
        ps = statuses.get(Integer.valueOf(ld.get(ChronoField.MONTH_OF_YEAR)));
      } 
      if (ps.getStatus() != 0) {
        float sumToPay = 0.0F;
        for (Map.Entry<Integer, PaymentStatus> pee : statuses.entrySet()) {
          PaymentStatus p = pee.getValue();
          if (p.getStatus() == 2) {
            sumToPay += p.getAmountToPay();
            continue;
          } 
          if (p.getStatus() != 1)
            continue; 
          sumToPay += p.getAmountToPay() - p.getAmountPaid();
        } 
        return sumToPay;
      } 
    } else {
      if (type == PlaceType.ADULTS) {
        int currentCycleNum;
        List<Cycle> cycles = getCycles(u);
        Map<Integer, PaymentStatus> statuses2 = getPaymentStatusesCyclic(u, cycles, false);
        try {
          currentCycleNum = Cycle.getCurrentCycleNum(cycles);
        } catch (Exception e) {
          return 0.0F;
        } 
        float sumToPay2 = 0.0F;
        for (Iterator<Integer> iterator = statuses2.keySet().iterator(); iterator.hasNext(); ) {
          int i = ((Integer)iterator.next()).intValue();
          if (i > currentCycleNum)
            continue; 
          PaymentStatus p2 = statuses2.get(Integer.valueOf(i));
          if (p2.getStatus() == 2) {
            sumToPay2 += p2.getAmountToPay();
            continue;
          } 
          if (p2.getStatus() != 1)
            continue; 
          sumToPay2 += p2.getAmountToPay() - p2.getAmountPaid();
        } 
        return sumToPay2;
      } 
      if (type == PlaceType.PLACE) {
    	  
      }
    } 
    return 0.0F;
  }
  
  public List<Cycle> getCycles(Uczestnik u) {
    List<Cycle> cycleList = new ArrayList<>();
    for (AttenderGroupChange c : u.getGroupChanges()) {
      for (Zajecia z : this.serviceClasses.getClassesByAGCCache(c)) {
        int cykl = z.getCykl().intValue();
        LocalDate classDate = z.getDate();
        Optional<Cycle> cycleOpt = cycleList.stream().filter(cyc -> (cyc.getNum() == cykl)).findFirst();
        if (cycleOpt.isPresent()) {
          Cycle cycle = cycleOpt.get();
          cycle.getClassList().add(z);
          if (cycle.getFirstClassDate() == null || cycle.getFirstClassDate().isAfter(classDate))
            cycle.setFirstClassDate(classDate); 
          if (cycle.getLastClassDate() == null || cycle.getLastClassDate().isBefore(classDate))
            cycle.setLastClassDate(classDate); 
        } else {
          Cycle cycle = new Cycle(cykl);
          cycle.getClassList().add(z);
          cycle.setFirstClassDate(classDate);
          cycle.setLastClassDate(classDate);
          cycleList.add(cycle);
        } 
      } 
    } 
    return cycleList;
  }
  
  public Map<Integer, PaymentStatus> getPaymentStatusesCyclic(Uczestnik u, List<Cycle> cycles, boolean fresh) {
    cycles.sort((o1, o2) -> o1.getNum() - o2.getNum());
    Map<Integer, PaymentStatus> response = new LinkedHashMap<>();
    float totalMoneyPaid = this.servicePayments.getTotalMoneyPaidByAttender(u);
    float rabatPrc = u.getRabatPrc();
    boolean individualPayment = u.isIndividualPayment();
    List<ClosedMonth> cms = u.getClosedMonths();
    for (Cycle cc : cycles) {
      List<Zajecia> inThisCycle = cc.getClassList();
      int cycle = cc.getNum();
      if (!fresh) {
        Optional<ClosedMonth> cOpt = cms.stream().filter(cm -> (cm.getCycle().intValue() == cycle)).findFirst();
        if (cOpt.isPresent()) {
          ClosedMonth cm = cOpt.get();
          float priceForThisMonth = cm.getAmount();
          response.put(Integer.valueOf(cycle), new PaymentStatus(0, true, priceForThisMonth, priceForThisMonth));
          totalMoneyPaid -= priceForThisMonth;
          continue;
        } 
      } 
      if (inThisCycle.size() == 0) {
        response.put(Integer.valueOf(cycle), new PaymentStatus(3));
        continue;
      } 
      float priceForThisMonth2 = 0.0F;
      for (Zajecia z : inThisCycle) {
        if (individualPayment) {
          Presence p = this.servicePresence.findByClassAndAttender(u, z);
          if (p.getPresence() != 1)
            continue; 
        } 
        float priceForOneClassInThisGr = z.getGroup().getPriceForOneClass();
        priceForThisMonth2 += priceForOneClassInThisGr * (1.0F - rabatPrc / 100.0F);
      } 
      float amountPaidForThisClass = 0.0F;
      if (priceForThisMonth2 <= totalMoneyPaid) {
        amountPaidForThisClass = priceForThisMonth2;
      } else if (totalMoneyPaid > 0.0F) {
        amountPaidForThisClass = totalMoneyPaid;
      } 
      if (LocalDate.now().isBefore(cc.getFirstClassDate())) {
        response.put(Integer.valueOf(cycle), new PaymentStatus(4, amountPaidForThisClass, priceForThisMonth2));
      } else if (amountPaidForThisClass == priceForThisMonth2) {
        response.put(Integer.valueOf(cycle), new PaymentStatus(0, amountPaidForThisClass, priceForThisMonth2));
        if (!fresh) {
          ClosedMonth cm2 = new ClosedMonth();
          cm2.setAttender(u);
          cm2.setAmount(amountPaidForThisClass);
          cm2.setSeason(u.getSeason());
          cm2.setYear(Integer.valueOf(-1));
          cm2.setCycle(Integer.valueOf(cycle));
          this.serviceClosedMonth.save(cm2);
        } 
      } else if (amountPaidForThisClass > 0.0F) {
        response.put(Integer.valueOf(cycle), new PaymentStatus(1, amountPaidForThisClass, priceForThisMonth2));
      } else {
        response.put(Integer.valueOf(cycle), new PaymentStatus(2, amountPaidForThisClass, priceForThisMonth2));
      } 
      totalMoneyPaid -= priceForThisMonth2;
    } 
    return response;
  }
  
  public List<ClassMonth> getClassMonths(Uczestnik u) {
    List<ClassMonth> classMonthList = new ArrayList<>();
    List<AttenderGroupChange> agcs = u.getGroupChanges();
    for (AttenderGroupChange c : agcs) {
      if (c.getGroup() == null)
        continue; 
      Zajecia[] zs = this.serviceClasses.getClassesByAGCCache(c);
      Season s = u.getSeason();
      LocalDate ld = s.getDateFrom();
      final int stnm = ld.get(ChronoField.MONTH_OF_YEAR);
      Arrays.sort(zs, new Comparator<Zajecia>() {
            public int compare(Zajecia o1, Zajecia o2) {
              LocalDate o1d = o1.getDate();
              LocalDate o2d = o2.getDate();
              int month1 = o1d.get(ChronoField.MONTH_OF_YEAR);
              int month2 = o2d.get(ChronoField.MONTH_OF_YEAR);
              if (month1 == stnm && month2 != stnm)
                return -1; 
              if (month1 != stnm && month2 == stnm)
                return 1; 
              return o1d.compareTo(o2d);
            }
          });
      for (Zajecia z : zs) {
        LocalDate data = z.getDate();
        int monthNum = data.get(ChronoField.MONTH_OF_YEAR);
        Optional<ClassMonth> cycleOpt = classMonthList.stream().filter(cyc -> (cyc.getNum() == monthNum)).findFirst();
        if (cycleOpt.isPresent()) {
          ClassMonth cycle = cycleOpt.get();
          cycle.getClassList().add(z);
        } else {
          ClassMonth cycle = new ClassMonth(data);
          cycle.getClassList().add(z);
          classMonthList.add(cycle);
        } 
      } 
    } 
    Season s2 = u.getSeason();
    LocalDate ld2 = s2.getDateFrom();
    final int stnm = ld2.get(ChronoField.MONTH_OF_YEAR);
    for (int i = 1; i <= 12; i++) {
      LocalDate ldd = ld2.plusMonths(1L);
      Optional<ClassMonth> cmO = classMonthList.stream().filter(c -> (c.getNum() == ldd.get(ChronoField.MONTH_OF_YEAR))).findFirst();
      if (!cmO.isPresent())
        classMonthList.add(new ClassMonth(ld2)); 
    } 
    Collections.sort(classMonthList, new Comparator<ClassMonth>() {
          public int compare(UczestnicyService.ClassMonth o1, UczestnicyService.ClassMonth o2) {
            LocalDate o1d = o1.getPaymentsDeadlineDate();
            LocalDate o2d = o2.getPaymentsDeadlineDate();
            int month1 = o1d.get(ChronoField.MONTH_OF_YEAR);
            int month2 = o2d.get(ChronoField.MONTH_OF_YEAR);
            if (month1 == stnm && month2 != stnm)
              return -1; 
            if (month1 != stnm && month2 == stnm)
              return 1; 
            return o1d.compareTo(o2d);
          }
        });
    return classMonthList;
  }
  
  public Map<Integer, Float> sumPricesForClasses(Uczestnik u) {
    List<ClassMonth> months = getClassMonths(u);
    Map<Integer, Float> response = new LinkedHashMap<>();
    float rabatPrc = u.getRabatPrc();
    boolean individualPayment = u.isIndividualPayment();
    for (ClassMonth month : months) {
      float priceForThisMonth = 0.0F;
      List<Zajecia> inThisMonth = month.getClassList();
      int monthNum = month.getNum();
      for (Zajecia z : inThisMonth) {
        if (individualPayment) {
          Presence p = this.servicePresence.findByClassAndAttender(u, z);
          if (p.getPresence() != 1)
            continue; 
        } 
        float priceForOneClassInThisGr = z.getGroup().getPriceForOneClass();
        priceForThisMonth += priceForOneClassInThisGr * (1.0F - rabatPrc / 100.0F);
      } 
      response.put(monthNum, Float.valueOf(priceForThisMonth));
    } 
    return response;
  }
  
  public Map<Integer, PaymentStatus> getPaymentStatusesKids(Uczestnik u, boolean fresh) {
    List<ClosedMonth> cms = u.getClosedMonths();
    List<ClassMonth> months = getClassMonths(u);
    Map<Integer, PaymentStatus> statuses = new LinkedHashMap<>();
    float totalMoneyPaid = this.servicePayments.getTotalMoneyPaidByAttender(u);
    boolean individualPayment = u.isIndividualPayment();
    float rabatPrc = u.getRabatPrc();
    LocalDate dateNow = LocalDate.now();
    boolean hasUnpaidClasses = false;
    
    for (ClassMonth classMonth : months) {
    	
      int monthNum = classMonth.getNum();
      LocalDate paymentsDeadline = classMonth.getPaymentsDeadlineDate();
      
      if (!fresh) {//try to find if month is closed
        Optional<ClosedMonth> cOpt = cms.stream().filter(cm -> (cm.getMonth().intValue() == monthNum)).findFirst();
        if (cOpt.isPresent()) {
          ClosedMonth cm = cOpt.get();
          Float priceForThisMonth = cm.getAmount();
          if(priceForThisMonth != null) {
        	  statuses.put(monthNum, new PaymentStatus(PaymentStatus.PAID, true, priceForThisMonth, priceForThisMonth));
              totalMoneyPaid -= priceForThisMonth;
              continue;
          }
        } 
      } 
      
      //no classes in this month
      List<Zajecia> inThisMonth = classMonth.getClassList();
      if (inThisMonth.size() == 0) {
        statuses.put(monthNum, new PaymentStatus(PaymentStatus.NO_CLASSES));
        continue;
      } 
      
      //sum price for all classes in this month
      float priceForThisMonth = 0.0F;
      for (Zajecia z : inThisMonth) {
        if (individualPayment) {
          Presence p = this.servicePresence.findByClassAndAttender(u, z);
          if (p == null)
            continue; 
          if (p.getPresence() != 1)
            continue; 
        } 
        float priceForOneClassInThisGr = z.getGroup().getPriceForOneClass();
        priceForThisMonth += priceForOneClassInThisGr * (1.0F - rabatPrc / 100.0F);
      } 
      
      //calc how much of class price is paid (cannot be bigger than class price)
      float amountPaidForThisClass = 0;
      if (priceForThisMonth <= totalMoneyPaid) {
        amountPaidForThisClass = priceForThisMonth;
      } else if (totalMoneyPaid > 0) {
        amountPaidForThisClass = totalMoneyPaid;
      } 
      
      
      if (dateNow.isBefore(paymentsDeadline)) {
    	//not yet to check payments in this class month
        statuses.put(monthNum, new PaymentStatus(PaymentStatus.NOT_YET_CHECKED, amountPaidForThisClass, priceForThisMonth));
      } else if (amountPaidForThisClass == priceForThisMonth && !hasUnpaidClasses) {
    	//classe paid
        if (fresh) {
          statuses.put(monthNum, new PaymentStatus(PaymentStatus.PAID, amountPaidForThisClass, priceForThisMonth));
        } else {
          statuses.put(monthNum, new PaymentStatus(PaymentStatus.PAID, true, amountPaidForThisClass, priceForThisMonth));
          ClosedMonth cm2 = new ClosedMonth();
          cm2.setAttender(u);
          cm2.setAmount(amountPaidForThisClass);
          cm2.setSeason(u.getSeason());
          cm2.setYear(Integer.valueOf(-1));
          cm2.setMonth(monthNum);
          serviceClosedMonth.save(cm2);
        } 
      } else if (amountPaidForThisClass > 0) {
    	//class paid partially
        statuses.put(monthNum, new PaymentStatus(PaymentStatus.PAID_PARTIALLY, amountPaidForThisClass, priceForThisMonth));
        hasUnpaidClasses = true;
      } else {
    	//class not paid
        statuses.put(monthNum, new PaymentStatus(PaymentStatus.NOT_PAID, amountPaidForThisClass, priceForThisMonth));
        hasUnpaidClasses = true;
      }
      
      
      totalMoneyPaid -= priceForThisMonth;
    }
    
    //fill gaps
    for (int i = 1; i < 13; i++) {
      if (!statuses.containsKey(Integer.valueOf(i))) {
    	  statuses.put(i, new PaymentStatus(PaymentStatus.NO_CLASSES));
      }
    } 
    return statuses;
  }
  
  @Data
  public static class PaymentStatus {
    int status;
    float amountPaid;
    float amountToPay;
    boolean closed;
    
    public static final int PAID = 0;
    public static final int PAID_PARTIALLY = 1;
    public static final int NOT_PAID = 2;
    public static final int NO_CLASSES = 3;
    public static final int NOT_YET_CHECKED = 4;
    
    public PaymentStatus(int status) {
      this.status = -1;
      this.amountPaid = 0.0F;
      this.amountToPay = 0.0F;
      this.closed = false;
      this.status = status;
    }
    
    public PaymentStatus(int status, float amountPaid) {
      this.status = -1;
      this.amountPaid = 0.0F;
      this.amountToPay = 0.0F;
      this.closed = false;
      this.status = status;
      this.amountPaid = amountPaid;
    }
    
    public PaymentStatus(int status, double amountPaid) {
      this.status = -1;
      this.amountPaid = 0.0F;
      this.amountToPay = 0.0F;
      this.closed = false;
      this.status = status;
      this.amountPaid = (float)amountPaid;
    }
    
    public PaymentStatus(int status, double amountPaid, double amountToPay) {
      this.status = -1;
      this.amountPaid = 0.0F;
      this.amountToPay = 0.0F;
      this.closed = false;
      this.status = status;
      this.amountPaid = (float)amountPaid;
      this.amountToPay = (float)amountToPay;
    }
    
    public PaymentStatus(int status, boolean closed, double amountPaid, double amountToPay) {
      this.status = -1;
      this.amountPaid = 0.0F;
      this.amountToPay = 0.0F;
      this.closed = false;
      this.status = status;
      this.closed = closed;
      this.amountPaid = (float)amountPaid;
      this.amountToPay = (float)amountToPay;
    }
  }
  
  @Data
  static class Cycle {
    int num;
    List<Zajecia> classList;
    LocalDate firstClassDate;
    LocalDate lastClassDate;
    
    public Cycle(int num) {
      this.classList = new ArrayList<>();
      this.num = num;
    }
    
    public static int getCurrentCycleNum(List<Cycle> cycles) throws Exception {
      cycles = new ArrayList<>(cycles);
      cycles.sort((o1, o2) -> o2.getNum() - o1.getNum());
      for (Cycle c : cycles) {
        if (c.getFirstClassDate().isBefore(LocalDate.now()))
          return c.getNum(); 
      } 
      throw new Exception("no cycle found!");
    }
  }
  
  @Data
  public static class ClassMonth {
    int num;
    List<Zajecia> classList;
    LocalDate paymentsDeadlineDate;
    
    public ClassMonth(LocalDate ld) {
      this.classList = new ArrayList<>();
      this.num = ld.get(ChronoField.MONTH_OF_YEAR);
      this.paymentsDeadlineDate = ld.with(ChronoField.DAY_OF_MONTH, 15L);
    }
  }
}
