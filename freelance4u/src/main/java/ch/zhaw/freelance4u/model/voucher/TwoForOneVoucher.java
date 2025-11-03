package ch.zhaw.freelance4u.model.voucher;

import java.util.List;

import ch.zhaw.freelance4u.model.Job;
import ch.zhaw.freelance4u.model.JobType;

public class TwoForOneVoucher implements Voucher {
    private JobType jobType;

    public TwoForOneVoucher(JobType jobType) {
        this.jobType = jobType;
    }

    @Override
    public double getDiscount(List<Job> jobs) {
        long applicableJobsCount = jobs.stream()
                .filter(job -> job.getJobType() == jobType)
                .count();

        if (applicableJobsCount < 2) {
            return 0.0;
        }

        double totalEarnings = jobs.stream()
                .filter(job -> job.getJobType() == jobType)
                .mapToDouble(Job::getEarnings)
                .sum();

        return totalEarnings / 2;
    }
}
