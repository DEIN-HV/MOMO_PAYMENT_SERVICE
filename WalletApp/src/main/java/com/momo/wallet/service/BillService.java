package com.momo.wallet.service;

import java.time.LocalDate;
import java.util.List;
import com.momo.wallet.model.Bill;

public interface BillService {
    Bill createBill(String type, long amount, LocalDate dueDate, String provider);
    List<Bill> listBills();
    Bill viewBill(int id);
    boolean updateBill(int id, String type, long amount, LocalDate dueDate, String provider);
    boolean deleteBill(int id);
    List<Bill> searchByProvider(String provider);
    List<Bill> listDueBills();
}