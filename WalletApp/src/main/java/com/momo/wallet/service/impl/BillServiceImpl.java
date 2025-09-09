// BillServiceImpl.java
package com.momo.wallet.service.impl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import com.momo.wallet.model.Bill;
import com.momo.wallet.service.BillService;

public class BillServiceImpl implements BillService {
    private final Map<Integer, Bill> bills = new HashMap<>();
    private int nextId = 1;

    @Override
    public Bill createBill(String type, long amount, LocalDate dueDate, String provider) {
        Bill bill = new Bill(nextId++, type, amount, dueDate, provider);
        bills.put(bill.getId(), bill);
        return bill;
    }

    @Override
    public List<Bill> listBills() {
        return new ArrayList<>(bills.values());
    }

    @Override
    public Bill viewBill(int id) {
        return bills.get(id);
    }

    @Override
    public boolean updateBill(int id, String type, long amount, LocalDate dueDate, String provider) {
        Bill bill = bills.get(id);
        if (bill == null) return false;
        bill.setType(type);
        bill.setAmount(amount);
        bill.setDueDate(dueDate);
        bill.setProvider(provider);
        return true;
    }

    @Override
    public boolean deleteBill(int id) {
        return bills.remove(id) != null;
    }

    @Override
    public List<Bill> searchByProvider(String provider) {
        return bills.values().stream()
                .filter(b -> b.getProvider().equalsIgnoreCase(provider))
                .collect(Collectors.toList());
    }

    @Override
    public List<Bill> listDueBills() {
        return bills.values().stream()
                .filter(b -> !b.getPaid())
                .sorted(Comparator.comparing(Bill::getDueDate))
                .collect(Collectors.toList());
    }
}
