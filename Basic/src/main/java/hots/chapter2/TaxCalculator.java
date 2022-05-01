package hots.chapter2;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaxCalculator {

    private double salary;
    private double bonus;

    private CalculatorCalculator calculatorCalculator;

    public TaxCalculator(double salary, double bouns, CalculatorCalculator calculatorCalculator) {
        this.salary = salary;
        this.bonus = bouns;
        this.calculatorCalculator = calculatorCalculator;
    }

    public double calcTax() {
        return calculatorCalculator.calcTax(this.getSalary(), this.getBonus());
    }

    public double calculate() {
        return this.calcTax();
    }
}
