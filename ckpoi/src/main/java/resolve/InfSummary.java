package resolve;

import lombok.Data;

import java.util.List;

/**
 * @Title: InfSummary
 * @Description: 接口汇总信息
 * @Author: Chengkai
 * @Date: 2019/7/19 14:49
 * @Version: 1.0
 */
@Data
public class InfSummary {

    /**
     * 接口详细列表
     */
    private List<InfDetail> infDetails;

}
