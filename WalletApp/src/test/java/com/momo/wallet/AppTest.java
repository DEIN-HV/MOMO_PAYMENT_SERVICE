package com.momo.wallet;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.momo.wallet.model.Bill;
import com.momo.wallet.model.Payment;
import com.momo.wallet.service.AccountService;
import com.momo.wallet.service.BillService;
import com.momo.wallet.service.PaymentService;
import com.momo.wallet.service.ScheduledPaymentService;
import com.momo.wallet.service.impl.AccountServiceImpl;
import com.momo.wallet.service.impl.BillServiceImpl;
import com.momo.wallet.service.impl.PaymentServiceImpl;
import com.momo.wallet.service.impl.ScheduledPaymentServiceImpl;

public class AppTest {

    private AccountService accountService; // field, không phải local
    private BillService billService;
    private PaymentService paymentService;
    private ScheduledPaymentService scheduledPaymentService;

    @BeforeEach
    public void setup() {
        accountService = new AccountServiceImpl();
        billService = new BillServiceImpl();
        paymentService = new PaymentServiceImpl(accountService, billService);
        scheduledPaymentService = new ScheduledPaymentServiceImpl(paymentService, billService);
    }

    // CASH IN
    @Test
    public void testCashInIncreasesBalance() {

        // ban đầu số dư = 0
        assertEquals(0, accountService.getBalance());

        // nạp 1000
        accountService.cashIn(1000);
        assertEquals(1000, accountService.getBalance());

        // nạp thêm 500
        accountService.cashIn(500);
        assertEquals(1500, accountService.getBalance());

    }

    // CREATE BILL
    @Test
    public void testCreateAndGetBill() {
        billService = new BillServiceImpl();

        Bill bill = billService.createBill("ELECTRIC", 200000, LocalDate.of(2025, 10, 25), "EVN HCMC");

        assertNotNull(bill);
        assertEquals("ELECTRIC", bill.getType());
        assertEquals(200000, bill.getAmount());
        assertEquals("EVN HCMC", bill.getProvider());

        Bill found = billService.viewBill(bill.getId());
        assertEquals(bill, found);
    }

    // DELETE BILL
    @Test
    public void testDeleteBill() {
        Bill bill = billService.createBill("WATER", 150000, LocalDate.of(2025, 10, 30), "SAVACO HCMC");
        assertNotNull(billService.viewBill(bill.getId()));

        boolean deleted = billService.deleteBill(bill.getId());
        assertTrue(deleted);

        assertNull(billService.viewBill(bill.getId()));
    }

    // LIST BILL
    @Test
    public void testListBills() {
        billService.createBill("ELECTRIC", 200000, LocalDate.of(2025, 10, 25), "EVN HCMC");
        billService.createBill("WATER", 150000, LocalDate.of(2025, 10, 30), "SAVACO HCMC");

        List<Bill> bills = billService.listBills();
        assertEquals(2, bills.size());
    }

    // SEARCH BILL BY PROVIDER
    @Test
    public void testSearchBillsByProvider() {
        billService.createBill("ELECTRIC", 200000, LocalDate.of(2025, 10, 25), "EVN HCMC");
        billService.createBill("INTERNET", 500000, LocalDate.of(2025, 11, 5), "VNPT");

        List<Bill> vnptBills = billService.searchByProvider("VNPT");
        assertEquals(1, vnptBills.size());
        assertEquals("INTERNET", vnptBills.get(0).getType());
    }

    // PAY SINGLE BILL
    @Test
    public void testPayValidBillWithEnoughBalance() {

        // nạp tiền vào tài khoản
        accountService.cashIn(500000);

        // tạo bill mới
        Bill bill = billService.createBill("ELECTRIC", 200000, LocalDate.of(2025, 10, 25), "EVN HCMC");

        // trước khi thanh toán
        assertFalse(bill.getPaid());
        assertEquals(500000, accountService.getBalance());

        // tiến hành thanh toán
        paymentService.payBills(bill.getId());

        // sau khi thanh toán: bill phải được mark là paid
        assertTrue(bill.getPaid());

        // số dư giảm đúng
        assertEquals(300000, accountService.getBalance());

        // kiểm tra có 1 record payment trong service
        assertEquals(1, paymentService.getPayments().size());
        Payment p = paymentService.getPayments().get(0);
        assertEquals(bill.getId(), p.getBillId());
        assertEquals(200000, p.getAmount());
        assertEquals(Payment.PaymentState.PROCESSED, p.getState());
    }

    // PAY MULTIPLE BILLS
    @Test
    public void testPayMultipleBillsWithNotEnoughBalanceForAll() {
        // nạp tiền đủ để trả nhiều bill
        accountService.cashIn(1000000);

        // tạo các bill
        Bill electricBill = billService.createBill("ELECTRIC", 500000, LocalDate.of(2025, 10, 25), "EVN HCMC");
        Bill internetBill = billService.createBill("INTERNET", 200000, LocalDate.of(2025, 11, 5), "VNPT");
        Bill waterBill = billService.createBill("WATER", 400000, LocalDate.of(2025, 10, 24), "SAVACO HCMC");
        
        // trước khi thanh toán
        assertFalse(electricBill.getPaid());
        assertFalse(waterBill.getPaid());
        assertFalse(internetBill.getPaid());
        assertEquals(1000000, accountService.getBalance());

        // tiến hành thanh toán nhiều bill
        paymentService.payBills(electricBill.getId(), waterBill.getId(), internetBill.getId());

        // sau khi thanh toán: bill ELECTRIC và WATER phải được mark là paid do có due date gần hơn, 
        // bill INTERNET vẫn chưa paid do thiêu tiền
        assertTrue(electricBill.getPaid());
        assertTrue(waterBill.getPaid());
        assertFalse(internetBill.getPaid());

        // số dư giảm đúng
        long expectedBalance = 1000000 - (500000 + 400000);
        assertEquals(expectedBalance, accountService.getBalance());

        // có 2 record payment
        List<Payment> payments = paymentService.getPayments();
        assertEquals(2, payments.size());

        // kiểm tra từng billId có trong payments
        assertTrue(payments.stream().anyMatch(p -> p.getBillId() == electricBill.getId()));
        assertTrue(payments.stream().anyMatch(p -> p.getBillId() == waterBill.getId()));
        assertFalse(payments.stream().anyMatch(p -> p.getBillId() == internetBill.getId()));
    }

    // KEEP TRACK OF BILL
    @Test
    public void testListDueBillsSortedByDueDate() {
        // Tạo các bill với dueDate khác nhau
        Bill electricBill = billService.createBill("ELECTRIC", 200000,
                LocalDate.of(2025, 10, 25), "EVN HCMC");

        Bill waterBill = billService.createBill("WATER", 150000,
                LocalDate.of(2025, 10, 20), "SAVACO HCMC");

        Bill internetBill = billService.createBill("INTERNET", 300000,
                LocalDate.of(2025, 11, 5), "VNPT");

        // Thanh toán bill internet trước (không còn "due")
        internetBill.setPaid(true);

        // Lấy danh sách bill chưa thanh toán
        List<Bill> dueBills = billService.listDueBills();

        // chỉ còn 2 bill (electric, water) vì internet đã paid
        assertEquals(2, dueBills.size());

        // danh sách phải được sắp xếp theo dueDate (water trước, electric sau)
        assertEquals(waterBill, dueBills.get(0));
        assertEquals(electricBill, dueBills.get(1));
    }

    // ADD SCHEDULED PAYMENT
    @Test
    public void testScheduledPaymentExecutesOnDueDate() {
        // Nạp tiền đủ
        accountService.cashIn(500000);

        // Tạo bill
        Bill bill = billService.createBill("INTERNET", 300000,
                LocalDate.of(2025, 11, 5), "VNPT");

        // Lên lịch thanh toán hôm nay
        LocalDate today = LocalDate.now();
        scheduledPaymentService.schedulePayment(bill.getId(), today);

        // Trước khi process: bill chưa được thanh toán
        assertFalse(bill.getPaid());
        assertEquals(500000, accountService.getBalance());

        // Chạy process (giả lập đến ngày hôm nay)
        scheduledPaymentService.processScheduledPayments();

        // Sau khi process: bill đã được thanh toán
        assertTrue(bill.getPaid());
        assertEquals(200000, accountService.getBalance());

        // Có 1 payment record
        List<Payment> payments = paymentService.getPayments();
        assertEquals(1, payments.size());
        assertEquals(bill.getId(), payments.get(0).getBillId());
        assertEquals(300000, payments.get(0).getAmount());
    }

    // VIEW PAYMENT HISTORY
    @Test
    public void testTransactionHistoryAfterPayments() {
        // Nạp tiền
        accountService.cashIn(1000000);

        // Tạo 2 bill
        Bill electricBill = billService.createBill("ELECTRIC", 200000,
                LocalDate.of(2025, 10, 25), "EVN HCMC");
        Bill waterBill = billService.createBill("WATER", 150000,
                LocalDate.of(2025, 10, 30), "SAVACO HCMC");

        // Thanh toán 2 bill
        paymentService.payBills(electricBill.getId(), waterBill.getId());

        // Lấy danh sách payments
        List<Payment> payments = paymentService.getPayments();
        assertEquals(2, payments.size());

        // Kiểm tra payment đầu tiên
        Payment p1 = payments.get(0);
        assertEquals(electricBill.getId(), p1.getBillId());
        assertEquals(200000, p1.getAmount());
        assertEquals(Payment.PaymentState.PROCESSED, p1.getState());
        assertEquals(LocalDate.now(), p1.getPaymentDate());

        // Kiểm tra payment thứ hai
        Payment p2 = payments.get(1);
        assertEquals(waterBill.getId(), p2.getBillId());
        assertEquals(150000, p2.getAmount());
        assertEquals(Payment.PaymentState.PROCESSED, p2.getState());
        assertEquals(LocalDate.now(), p2.getPaymentDate());

    }
}
