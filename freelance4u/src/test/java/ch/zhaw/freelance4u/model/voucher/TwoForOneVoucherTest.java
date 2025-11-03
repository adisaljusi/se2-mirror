package ch.zhaw.freelance4u.model.voucher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import ch.zhaw.freelance4u.model.Job;
import ch.zhaw.freelance4u.model.JobType;

public class TwoForOneVoucherTest {

    @Test
    public void testTwoDifferentJobTypes() {
        TwoForOneVoucher voucher = new TwoForOneVoucher(JobType.IMPLEMENT);
        List<Job> jobs = List.of(
                new Job("Test Job 1", "Test title 1", JobType.IMPLEMENT, 100.0, ""),
                new Job("Test Job 2", "Test title 2", JobType.TEST, 100.0, ""));
        assertEquals(0.0, voucher.getDiscount(jobs));
    }

    @Test
    public void testTwoSameTypeJobs() {
        TwoForOneVoucher voucher = new TwoForOneVoucher(JobType.TEST);
        List<Job> jobs = List.of(
                new Job("Test Job 1", "Test Description 1", JobType.TEST, 77.0, ""),
                new Job("Test Job 2", "Test Description 2", JobType.TEST, 33.0, ""));
        assertEquals(55.0, voucher.getDiscount(jobs));
    }

    @Test
    public void testThreeSameTypeJobs() {
        TwoForOneVoucher voucher = new TwoForOneVoucher(JobType.REVIEW);
        List<Job> jobs = List.of(
                new Job("Test Job 1", "Test Description 1", JobType.REVIEW, 77.0, ""),
                new Job("Test Job 2", "Test Description 2", JobType.REVIEW, 33.0, ""),
                new Job("Test Job 3", "Test Description 3", JobType.REVIEW, 99.0, ""));
        assertEquals(104.5, voucher.getDiscount(jobs));
    }

    @Test
    public void testThreeJobsWithOneDifferentType() {
        TwoForOneVoucher voucher = new TwoForOneVoucher(JobType.REVIEW);
        List<Job> jobs = List.of(
                new Job("Test Job 1", "Test Description 1", JobType.REVIEW, 77.0, ""),
                new Job("Test Job 2", "Test Description 2", JobType.REVIEW, 33.0, ""),
                new Job("Test Job 3", "Test Description 3", JobType.TEST, 99.0, ""));
        assertEquals(55.0, voucher.getDiscount(jobs));
    }

    @ParameterizedTest
    @CsvSource({ "0,0", "1,0", "2,77", "3,115.5", "4,154" })
    public void testVariableNumberOfJobs(ArgumentsAccessor arguments) {
        int numberOfJobs = arguments.getInteger(0);
        double expectedDiscount = arguments.getDouble(1);

        TwoForOneVoucher voucher = new TwoForOneVoucher(JobType.IMPLEMENT);
        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i < numberOfJobs; i++) {
            jobs.add(new Job("Test Job " + i, "Test Description " + i, JobType.IMPLEMENT, 77.0, ""));
        }

        assertEquals(expectedDiscount, voucher.getDiscount(jobs));
    }
}
