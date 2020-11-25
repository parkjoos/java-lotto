package lotto.service;

import lotto.domain.Cash;
import lotto.domain.LottoBalls;
import lotto.domain.LottoNumber;
import lotto.domain.LottoReport;
import lotto.domain.Pick;
import lotto.domain.Round;
import lotto.domain.WinningLottoBalls;
import lotto.domain.enums.Currency;
import lotto.domain.enums.PickType;
import lotto.domain.enums.Rank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RoundServiceTest {

    private RoundService roundService;
    private LottoService lottoService;

    @BeforeEach
    void makeTestRoundService() {
        AbstractPrizePackager prizePackager = new DefaultPrizePackager();
        lottoService = new LottoService(new Cash(1000L, Currency.WON), prizePackager);
        roundService = new RoundService(new AutoPickService(), lottoService);
    }

    @Test
    void testBuy() {
        Set<Pick> myPicks = new HashSet<>();
        myPicks.add(new Pick(PickType.AUTO, new LottoBalls(3, 5, 6, 7, 8, 9)));
        Round round = roundService.buy(myPicks);

        assertThat(round).isNotNull();
        assertThat(round.getMyPicks()).containsAll(myPicks);
    }

    @Test
    void testAutoBuy() {
        Round round = roundService.autoBuy(14);

        assertThat(round).isNotNull();
        assertThat(round.getMyPicks()).hasSize(14);
    }

    @Test
    void checkWinning() {
        Round round = roundService.autoBuy(14);
        roundService.checkWinning(new WinningLottoBalls(new LottoBalls(1,2,3,4,5,6), new LottoNumber(7)));
        assertThat(round.getMyPicks()).allSatisfy(pick -> {
            assertThat(pick.getRank()).isEqualTo(Rank.LOSE);
        });
    }

    @Test
    void testGenerateReport() {
        Round round = roundService.autoBuy(14);
        roundService.checkWinning(new WinningLottoBalls(new LottoBalls(1,2,3,4,5,6), new LottoNumber(7)));
        LottoReport lottoReport = roundService.generateReport();
        assertThat(lottoReport).isNotNull();
        assertThat(lottoReport.getRankMap()).extractingByKey(Rank.LOSE).isEqualTo(14);
    }
}
