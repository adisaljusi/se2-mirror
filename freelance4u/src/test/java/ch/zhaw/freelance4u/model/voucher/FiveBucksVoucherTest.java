package ch.zhaw.freelance4u.model.voucher;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.zhaw.freelance4u.model.Job;
import ch.zhaw.freelance4u.model.JobType;

public class FiveBucksVoucherTest {

    @Test
    public void testEmpty() {
        FiveBucksVoucher voucher = new FiveBucksVoucher();
        List<Job> jobs = new ArrayList<>();

        double discount = voucher.getDiscount(jobs);

        assertEquals(0.0, discount);
    }

    @Test
    public void testTen() {
        FiveBucksVoucher voucher = new FiveBucksVoucher();
        List<Job> jobs = new ArrayList<>();

        Job job = new Job("Test Job", "Test Description", JobType.IMPLEMENT, 10.0, "");
        jobs.add(job);

        double discount = voucher.getDiscount(jobs);

        assertEquals(5.0, discount);
    }
}
