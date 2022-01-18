import com.techreturners.bubbleteaordersystem.model.*;
import com.techreturners.bubbleteaordersystem.service.BubbleTeaMessenger;
import com.techreturners.bubbleteaordersystem.service.BubbleTeaOrderService;
import com.techreturners.bubbleteaordersystem.model.BubbleTeaTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import testhelper.DummySimpleLogger;

import static com.techreturners.bubbleteaordersystem.model.BubbleTeaTypeEnum.OolongMilkTea;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DifferentBubbleTeaOrderServiceTest {
    private DebitCard testDebitCard;
    private PaymentDetails paymentDetails;
    private DummySimpleLogger dummySimpleLogger;
    private BubbleTeaMessenger mockMessenger;
    private BubbleTeaOrderService bubbleTeaOrderService;

    @BeforeEach
    public void setup() {
        testDebitCard = new DebitCard("0123456789");
        paymentDetails = new PaymentDetails("hello kitty", "sanrio puroland", testDebitCard);
        dummySimpleLogger = new DummySimpleLogger();
        mockMessenger = mock(BubbleTeaMessenger.class);
        bubbleTeaOrderService = new BubbleTeaOrderService(dummySimpleLogger, mockMessenger);
    }

    @ParameterizedTest
    @MethodSource("getBubbleTea")
    public void shouldCreateBubbleTeaOrderRequestWithDifferentTeaRequest(BubbleTeaTypeEnum bubbleTeaTypeValue) {

        //Arrange
        BubbleTea bubbleTea = new BubbleTea(bubbleTeaTypeValue, 4.5);
        BubbleTeaRequest bubbleTeaRequest = new BubbleTeaRequest(paymentDetails, bubbleTea);

        BubbleTeaOrderRequest expectedResult = new BubbleTeaOrderRequest(
                "hello kitty",
                "sanrio puroland",
                "0123456789",
                bubbleTeaTypeValue
        );

        //Act
        BubbleTeaOrderRequest result = bubbleTeaOrderService.createOrderRequest(bubbleTeaRequest);

        //Assert
        assertEquals(expectedResult.getName(), result.getName());
        assertEquals(expectedResult.getAddress(), result.getAddress());
        assertEquals(expectedResult.getDebitCardDigits(), result.getDebitCardDigits());
        assertEquals(expectedResult.getBubbleTeaType(), result.getBubbleTeaType());

        //Verify Mock was called with the BubbleTeaOrderRequest result object
        verify(mockMessenger).sendBubbleTeaOrderRequestEmail(result);
        verify(mockMessenger, times(1)).sendBubbleTeaOrderRequestEmail(result);
    }

    private static BubbleTeaTypeEnum[] getBubbleTea() {
        return new BubbleTeaTypeEnum[] { BubbleTeaTypeEnum.OolongMilkTea,BubbleTeaTypeEnum.PeachIceTea,BubbleTeaTypeEnum.JasmineMilkTea,
                BubbleTeaTypeEnum.MatchaMilkTea,BubbleTeaTypeEnum.LycheeIceTea };
    }
}
