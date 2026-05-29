package ec.edu.epn.skyroute.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaggageFeeCalculatorTest {

    @Mock
    private PassengerService passengerService;
    Long vipPassengerId = 3L;

    @InjectMocks
    private BaggageFeeCalculator baggageFeeCalculator;

    @Test
    @DisplayName("Prueba unitaria para la maleta estandar")
    void shouldChargeBaseFeeWhenBagIsStandard() {
        // Arrange
        when(passengerService.isVip(1L)).thenReturn(false);

        // Act
        double fee = baggageFeeCalculator.calculateFee(20.0, 1, 1L);

        // Assert
        assertEquals(30.0, fee, 0.0001);
    }

    @Test
    @DisplayName("Prueba unitaria para cobrar por exceso de peso")
    void shouldChargeOverweight() {
        // Arrange
        when(passengerService.isVip(2L)).thenReturn(false);

        // Act
        double fee = baggageFeeCalculator.calculateFee(25.0, 1, 2L);

        // Assert
        assertEquals(80.0, fee, 0.0001);
    }
    @Test
    @DisplayName("Prueba unitaria para probrar el beneficio VIP")
    void shouldNotChargeVIPFirstBag(){
        //Arrange
        when(passengerService.isVip(vipPassengerId)).thenReturn(true);

        //Act
        double fee = baggageFeeCalculator.calculateFee(15,1,vipPassengerId);

        //Assert
        assertEquals(0,fee);
        verify(passengerService).isVip(vipPassengerId);
    }
    @Test
    @DisplayName("Prueba unitaria para probar el caso limite VIP")
    void firstBagShouldBeFree(){
    //Arrange
        Long vipPassengerId = 4L;
        when(passengerService.isVip(vipPassengerId)).thenReturn(true);
    //Act
        double fee = baggageFeeCalculator.calculateFee(15,2,vipPassengerId);
    //Assert
        assertEquals(30,fee);
        verify(passengerService).isVip(vipPassengerId);
    }

    @Test
    @DisplayName("Prueba unitaria para probar peso = 0")
    void weightCantBeZero() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> baggageFeeCalculator.calculateFee(0, 1, 5L));
    }
}

