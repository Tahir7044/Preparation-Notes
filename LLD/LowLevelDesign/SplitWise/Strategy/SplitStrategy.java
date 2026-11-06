package LowLevelDesign.SplitWise.Strategy;

import LowLevelDesign.SplitWise.model.Split;
import LowLevelDesign.SplitWise.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SplitStrategy {
    List<Split> split(BigDecimal amount, List<User> participants, Map<String, BigDecimal> metadata);
}
