package ch.zhaw.freelance4u.model.voucher;

import java.util.List;

import ch.zhaw.freelance4u.model.Job;

public class FiveBucksVoucher implements Voucher {

    @Override
    public double getDiscount(List<Job> jobs) {
        double totalEarnings = jobs.stream()
                .mapToDouble(Job::getEarnings)
                .sum();

        return totalEarnings >= 10.0 ? 5.0 : 0.0;
    }

}
