package ch.zhaw.freelance4u.model.voucher;

import java.util.List;

import ch.zhaw.freelance4u.model.Job;

public class PercentageVoucher implements Voucher {

    private final int discount;

    public PercentageVoucher(int discount) {
        if (discount > 50) {
            throw new RuntimeException("Error: Discount valü must less or equal 50.");
        }
        if (discount <= 0) {
            throw new RuntimeException("Error: Discount valü must be greater zero.");
        }
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
