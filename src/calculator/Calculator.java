package calculator;

public class Calculator
{
    enum operations
    {
        addition,
        subtraction,
        multiplication,
        division
    }

    public static double addition(double a, double b)
    {
        return a + b;
    }

    public static double subtraction(double a, double b)
    {
        return a - b;
    }

    public static double multiplication(double a, double b)
    {
        return a * b;
    }

    public static double division(double a, double b)
    {
        return a / b;
    }
}
