package LowLevelDesign.SplitWise.Strategy;

import LowLevelDesign.SplitWise.model.Split;
import LowLevelDesign.SplitWise.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExactSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> split(BigDecimal amount, List<User> participants, Map<String, BigDecimal> metadata) {
        BigDecimal total = BigDecimal.ZERO;
        List<Split> splits = new ArrayList<>();
        for (User user : participants) {
            BigDecimal share = metadata.getOrDefault(user.getUserId(), BigDecimal.ZERO);
            total = total.add(share);
            splits.add(new Split(user, share));
        }
        if (total.compareTo(amount) != 0) {
            throw new IllegalArgumentException("Exact amounts should add up to " + amount);
        }
        return splits;
    }
}
