package ch.zhaw.freelance4u.model.voucher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.zhaw.freelance4u.model.Job;
import ch.zhaw.freelance4u.model.JobType;

public class PercentageVoucherTest {

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 5, 20, 49, 50 })
    public void testSingleJobWithVariousPercentages(int percentage) {
        PercentageVoucher voucher = new PercentageVoucher(percentage);
        List<Job> jobs = new ArrayList<>();

        Job job = new Job("Test Job", "Test Description", JobType.IMPLEMENT, 50.0, "");
        jobs.add(job);

        double discount = voucher.getDiscount(jobs);
        double expected = 50.0 * (percentage / 100.0);

        assertEquals(expected, discount);
    }

    @Test
    public void testTwoJobsWith42PercentDiscount() {
        PercentageVoucher voucher = new PercentageVoucher(42);
        List<Job> jobs = new ArrayList<>();

        Job job1 = new Job("Test Job 1", "Test Description 1", JobType.IMPLEMENT, 42.0, "");
        Job job2 = new Job("Test Job 2", "Test Description 2", JobType.IMPLEMENT, 77.0, "");
        jobs.add(job1);
        jobs.add(job2);

        double discount = voucher.getDiscount(jobs);

        assertEquals(49.98, discount);
    }

    @Test
    public void testPercentageGreaterThan50ThrowsException() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            new PercentageVoucher(51);
        });
        assertEquals("Error: Discount value must less or equal 50.", ex.getMessage());
    }

    @Test
    public void testPercentageEqualToZeroThrowsException() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            new PercentageVoucher(0);
        });
        assertEquals("Error: Discount value must be greater zero.", ex.getMessage());
    }

    @Test
    public void testPercentageLessThanZeroThrowsException() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            new PercentageVoucher(-1);
        });
        assertEquals("Error: Discount value must be greater zero.", ex.getMessage());
    }

    @Test
    public void testTwoMockedJobsWith42PercentDiscount() {
        PercentageVoucher voucher = new PercentageVoucher(42);
        List<Job> jobs = new ArrayList<>();

        Job job1 = mock(Job.class);
        when(job1.getEarnings()).thenReturn(42.0);

        Job job2 = mock(Job.class);
        when(job2.getEarnings()).thenReturn(77.0);

        jobs.add(job1);
        jobs.add(job2);

        double discount = voucher.getDiscount(jobs);

        assertEquals(49.98, discount);
    }
}
