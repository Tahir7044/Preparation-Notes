package LowLevelDesign.SplitWise.Strategy;

import LowLevelDesign.SplitWise.model.Split;
import LowLevelDesign.SplitWise.model.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EqualSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> split(BigDecimal amount, List<User> participants, Map<String, BigDecimal> metadata) {
        BigDecimal share = amount.divide(BigDecimal.valueOf(participants.size()), 2, RoundingMode.DOWN);
        List<Split> splits = new ArrayList<>();
        for (User user : participants) {
            splits.add(new Split(user, share));
        }
        return splits;
    }
}
