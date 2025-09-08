package com.wallet.bill;

import java.time.LocalDate;
import java.util.*;

public class BillService {
    private final Map<Integer, Bill> bills = new HashMap<>();
    private int nextId = 1;

    // Tạo bill mới
    public Bill createBill(String type, long amount, LocalDate dueDate, String provider) {
        Bill bill = new Bill(nextId++, type, amount, dueDate, provider);
        bills.put(bill.getId(), bill);
        return bill;
    }

    // Xem tất cả bill
    public List<Bill> listBills() {
        return new ArrayList<>(bills.values());
    }

    // Tìm bill theo id
    public Bill viewBill(int id) {
        return bills.get(id);
    }

    // Cập nhật bill
    public boolean updateBill(int id, String type, long amount, LocalDate dueDate, String provider) {
        Bill bill = bills.get(id);
        if (bill == null)
            return false;
        bill.setType(type);
        bill.setAmount(amount);
        bill.setDueDate(dueDate);
        bill.setProvider(provider);
        return true;
    }

    // Xóa bill
    public boolean deleteBill(int id) {
        return bills.remove(id) != null;
    }

    // Tìm bill theo provider
    public List<Bill> searchByProvider(String provider) {
        List<Bill> result = new ArrayList<>();
        for (Bill bill : bills.values()) {
            if (bill.getProvider().equalsIgnoreCase(provider)) {
                result.add(bill);
            }
        }
        return result;
    }
}