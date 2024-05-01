package domain.bank;

import domain.game.NotEnoughResourcesException;
import domain.player.Player;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoanTest {
    private Player player;

    private Bank mockBank;

    @BeforeEach
    public void setupMocks() {
        player = EasyMock.partialMockBuilder(Player.class)
                .addMockedMethod("addResources", Resource[].class)
                .mock();
        mockBank = EasyMock.mock(Bank.class);
    }

    private void replayMocks() {
        EasyMock.replay(mockBank);
    }

    private void verifyMocks() {
        EasyMock.verify(mockBank);
    }

    @Test
    public void testCalcResourcesDue_withOnlyOre_expectExtraOre() {
        Resource[] borrowed = {Resource.ORE, Resource.ORE};
        Resource[] due = {Resource.ORE, Resource.ORE, Resource.ORE};
        Loan loan = new Loan(player, borrowed);

        Assertions.assertArrayEquals(due, loan.calcResourcesDue());
    }

    @Test
    public void testCalcResourcesDue_withTwoOreOneWool_expectExtraWool() {
        Resource[] borrowed = {Resource.ORE, Resource.ORE, Resource.WOOL};
        Resource[] due = {Resource.ORE, Resource.ORE, Resource.WOOL, Resource.WOOL};
        Loan loan = new Loan(player, borrowed);

        Assertions.assertArrayEquals(due, loan.calcResourcesDue());
    }

    @Test
    public void testCalcResourcesDue_withOneOreOneWoolOneLumber_expectExtraLumber() {
        Resource[] borrowed = {Resource.ORE, Resource.WOOL, Resource.LUMBER};
        Resource[] due = {Resource.ORE, Resource.WOOL, Resource.LUMBER, Resource.LUMBER};
        Loan loan = new Loan(player, borrowed);

        Assertions.assertArrayEquals(due, loan.calcResourcesDue());
    }

    @Test
    public void testGiveLoan_withOneOreBankHas_expectOreTransferred() {
        Resource[] borrowed = {Resource.ORE};
        Loan loan = new Loan(player, borrowed);

        EasyMock.expect(mockBank.removeResources(borrowed)).andReturn(true);
        EasyMock.expect(player.addResources(borrowed)).andReturn(true);

        replayMocks();

        try {
            loan.giveLoan(mockBank, player);
        } catch (NotEnoughResourcesException e) {
            fail();
        }
        
        verifyMocks();
    }

    @Test
    public void testGiveLoan_withOneOreBankDoesntHave_expectNotEnoughResourcesException() {
        Resource[] borrowed = {Resource.ORE};
        Loan loan = new Loan(player, borrowed);

        EasyMock.expect(mockBank.removeResources(borrowed)).andReturn(false);

        replayMocks();

        Assertions.assertThrows(NotEnoughResourcesException.class, () -> loan.giveLoan(mockBank, player));

        verifyMocks();
    }

    @Test
    public void testLoanIsValid_withThreeResources_expectTrue() {
        Resource[] borrowed = {Resource.ORE, Resource.WOOL, Resource.LUMBER};

        Assertions.assertTrue(Loan.loanIsValid(borrowed));
    }

    @Test
    public void testLoanIsValid_withFourResources_expectFalse() {
        Resource[] borrowed = {Resource.ORE, Resource.WOOL, Resource.LUMBER, Resource.WOOL};

        Assertions.assertFalse(Loan.loanIsValid(borrowed));
    }

    @Test
    public void testGetRemainingResourcesDue_withOneOreLeft_expectOneOre() {
        Resource[] due = {Resource.ORE, Resource.WOOL, Resource.LUMBER};
        Resource[] removed = {Resource.WOOL, Resource.LUMBER};

        Resource[] remaining = {Resource.ORE};

        Assertions.assertArrayEquals(remaining, Loan.getRemainingResourcesDue(due, removed));
    }

    @Test
    public void testGetRemainingResourcesDue_withNoneLeft_expectEmptyList() {
        Resource[] due = {Resource.WOOL, Resource.LUMBER};
        Resource[] removed = {Resource.WOOL, Resource.LUMBER};

        Resource[] remaining = {};

        Assertions.assertArrayEquals(remaining, Loan.getRemainingResourcesDue(due, removed));
    }
}
