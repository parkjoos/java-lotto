package lotto.controller;

import lotto.domain.Cash;
import lotto.domain.Round;
import lotto.domain.enums.Currency;
import lotto.service.AutoPickService;
import lotto.service.LottoPrizePackager;
import lotto.service.LottoService;
import lotto.service.RoundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoundControllerTest {
    private RoundController roundController;

    @BeforeEach
    void setup(){
        LottoService lottoService = new LottoService(new Cash(1000L, Currency.WON), new LottoPrizePackager());
        RoundService roundService = new RoundService(new AutoPickService(), lottoService);
        roundController = new RoundController(roundService, lottoService);
    }

    @Test
    void testBuy(){
        Round round = roundController.autoBuy(14000L);

        assertThat(round).isNotNull();
        assertThat(round.getMyPicks()).hasSize(14);
    }
}
