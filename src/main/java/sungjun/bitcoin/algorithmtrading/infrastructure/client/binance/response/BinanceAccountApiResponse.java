package sungjun.bitcoin.algorithmtrading.infrastructure.client.binance.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
public class BinanceAccountApiResponse {

    private int makerCommission;
    private int takerCommission;
    private int buyerCommission;
    private int sellerCommission;
    private boolean canTrade;
    private boolean canWithdraw;
    private boolean canDeposit;
    private BinanceCommissionRates commissionRates;
    private boolean brokered;
    private boolean requireSelfTradePrevention;
    private boolean preventSor;
    private Long updateTime;
    private String accountType;
    @Singular
    private List<BinanceBalance> balances;
    private List<String> permissions;
    
    @Builder
    private BinanceAccountApiResponse(int makerCommission, int takerCommission, int buyerCommission, 
                                     int sellerCommission, Boolean canTrade, Boolean canWithdraw, 
                                     Boolean canDeposit, BinanceCommissionRates commissionRates, 
                                     Boolean brokered, Boolean requireSelfTradePrevention, 
                                     Boolean preventSor, Long updateTime, String accountType, 
                                     List<BinanceBalance> balances, List<String> permissions) {
        this.makerCommission = makerCommission;
        this.takerCommission = takerCommission;
        this.buyerCommission = buyerCommission;
        this.sellerCommission = sellerCommission;
        this.canTrade = canTrade;
        this.canWithdraw = canWithdraw;
        this.canDeposit = canDeposit;
        this.commissionRates = commissionRates;
        this.brokered = brokered;
        this.requireSelfTradePrevention = requireSelfTradePrevention;
        this.preventSor = preventSor;
        this.updateTime = updateTime;
        this.accountType = accountType;
        this.balances = balances;
        this.permissions = permissions;
    }
}
