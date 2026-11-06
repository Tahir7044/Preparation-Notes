package LowLevelDesign.SplitWise.Strategy;

import LowLevelDesign.SplitWise.model.Split;
import LowLevelDesign.SplitWise.model.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PercentageSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> split(BigDecimal amount, List<User> participants, Map<String, BigDecimal> metadata) {
        BigDecimal totalPercent = BigDecimal.ZERO;
        for (User user : participants) {
            totalPercent = totalPercent.add(metadata.getOrDefault(user.getUserId(), BigDecimal.ZERO));
        }
        if (totalPercent.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new IllegalArgumentException("Total percent should be 100");
        }

        List<Split> splits = new ArrayList<>();
        for (User user : participants) {
            BigDecimal percent = metadata.getOrDefault(user.getUserId(), BigDecimal.ZERO);
            BigDecimal share = amount.multiply(percent).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);
            splits.add(new Split(user, share));
        }
        return splits;
    }
}
