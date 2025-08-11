import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.domain.Currency.EUR;
import static ru.otus.domain.Currency.RUB;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.atm.AtmCreator;
import ru.otus.atm.AtmCreatorDefault;
import ru.otus.atm.factory.Atm;
import ru.otus.domain.Banknote;
import ru.otus.domain.Currency;
import ru.otus.exceptions.AtmInteractionException;

class ApplicationTest {
    private Atm atm;

    @BeforeEach
    void setUp() {
        List<Currency> currencies = new ArrayList<>() {
            {
                add(RUB);
                add(EUR);
            }
        };

        AtmCreator atmCreator = new AtmCreatorDefault(currencies);
        atm = atmCreator.create();
    }

    @Test
    void insertBanknotes() {
        atm.insertMoney(List.of(
                new Banknote(1000, RUB),
                new Banknote(500, RUB),
                new Banknote(500, RUB),
                new Banknote(100, RUB),
                new Banknote(100, RUB)));
        assertEquals(2200, atm.getBalanceForCurrency(RUB));
    }

    @Test
    void requestMoney() throws AtmInteractionException {
        atm.insertMoney(List.of(
                new Banknote(1000, RUB),
                new Banknote(500, RUB),
                new Banknote(500, RUB),
                new Banknote(500, RUB),
                new Banknote(100, RUB),
                new Banknote(100, RUB),
                new Banknote(100, RUB),
                new Banknote(100, RUB)));

        var expected = List.of(new Banknote(100, RUB), new Banknote(500, RUB));

        List<Banknote> banknotes = atm.requestMoney(RUB, "600");
        assertTrue(banknotes.containsAll(expected));
    }
}
