package com.momo.wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.momo.wallet.model.Bill;
import com.momo.wallet.service.AccountService;
import com.momo.wallet.service.BillService;
import com.momo.wallet.service.impl.AccountServiceImpl;
import com.momo.wallet.service.impl.BillServiceImpl;

public class AppTest {

    private BillService billService;
    
    @Test
    public void testCashInIncreasesBalance() {
        AccountService accountService = new AccountServiceImpl();

        // ban đầu số dư = 0
        assertEquals(0, accountService.getBalance());

        // nạp 1000
        accountService.cashIn(1000);
        assertEquals(1000, accountService.getBalance());

        // nạp thêm 500
        accountService.cashIn(500);
        assertEquals(1500, accountService.getBalance());
        
    }

    @BeforeEach
    public void setup() {
        billService = new BillServiceImpl(); // dùng impl
    }

    @Test
    public void testCreateAndGetBill() {
        Bill bill = billService.createBill("ELECTRIC", 200000, LocalDate.of(2025, 10, 25), "EVN HCMC");

        assertNotNull(bill);
        assertEquals("ELECTRIC", bill.getType());
        assertEquals(200000, bill.getAmount());
        assertEquals("EVN HCMC", bill.getProvider());

        Bill found = billService.viewBill(bill.getId());
        assertEquals(bill, found);
    }

    @Test
    public void testDeleteBill() {
        Bill bill = billService.createBill("WATER", 150000, LocalDate.of(2025, 10, 30), "SAVACO HCMC");
        assertNotNull(billService.viewBill(bill.getId()));

        boolean deleted = billService.deleteBill(bill.getId());
        assertTrue(deleted);

        assertNull(billService.viewBill(bill.getId()));
    }

    @Test
    public void testListBills() {
        billService.createBill("ELECTRIC", 200000, LocalDate.of(2025, 10, 25), "EVN HCMC");
        billService.createBill("WATER", 150000, LocalDate.of(2025, 10, 30), "SAVACO HCMC");

        List<Bill> bills = billService.listBills();
        assertEquals(2, bills.size());
    }

    @Test
    public void testSearchBillsByProvider() {
        billService.createBill("ELECTRIC", 200000, LocalDate.of(2025, 10, 25), "EVN HCMC");
        billService.createBill("INTERNET", 500000, LocalDate.of(2025, 11, 5), "VNPT");

        List<Bill> vnptBills = billService.searchByProvider("VNPT");
        assertEquals(1, vnptBills.size());
        assertEquals("INTERNET", vnptBills.get(0).getType());
    }
}
