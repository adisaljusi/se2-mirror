package ch.zhaw.freelance4u.model.voucher;

import java.util.List;

import ch.zhaw.freelance4u.model.Job;

public class PercentageVoucher implements Voucher {

    private final int discount;

    public PercentageVoucher(int discount) {
        this.discount = discount;
    }

    @Override
    public double getDiscount(List<Job> jobs) {
        double totalEarnings = jobs.stream()
                .mapToDouble(Job::getEarnings)
                .sum();

        return totalEarnings != 0 ? totalEarnings * (discount / 100.0) : 0;
    }

}
