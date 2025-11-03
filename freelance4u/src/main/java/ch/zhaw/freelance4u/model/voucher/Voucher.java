package ch.zhaw.freelance4u.model.voucher;

import java.util.List;

import ch.zhaw.freelance4u.model.Job;

public interface Voucher {

    double getDiscount(List<Job> jobs);
}
